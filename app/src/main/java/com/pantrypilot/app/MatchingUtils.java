
package com.pantrypilot.app;

import android.database.Cursor;
import java.util.*;

public class MatchingUtils {

    private static String normalize(String name) {
        String n = name == null ? "" : name.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");

        Map<String,String> aliases = new HashMap<>();
        aliases.put("tomatoes", "tomato");
        aliases.put("potatoes", "potato");
        aliases.put("eggs", "egg");
        aliases.put("onions", "onion");
        aliases.put("carrots", "carrot");
        aliases.put("cloves of garlic", "garlic");
        aliases.put("garlic cloves", "garlic");
        aliases.put("chicken breast", "chicken");

        if (aliases.containsKey(n)) return aliases.get(n);

        if (n.endsWith("ies") && n.length() > 4)
            return n.substring(0, n.length()-3) + "y";

        if (n.endsWith("s") && !n.endsWith("ss"))
            return n.substring(0, n.length()-1);

        return n;
    }

    private static String dimension(String unit) {
        switch (unit == null ? "" : unit.toLowerCase(Locale.ROOT).trim()) {
            case "kg":
            case "g":
                return "mass";
            case "l":
            case "ml":
                return "volume";
            case "pcs":
            case "piece":
            case "pieces":
                return "count";
            default:
                return unit == null ? "" : unit.toLowerCase(Locale.ROOT).trim();
        }
    }

    private static double baseQuantity(double qty, String unit) {
        switch (unit == null ? "" : unit.toLowerCase(Locale.ROOT).trim()) {
            case "kg":
            case "l":
                return qty * 1000.0;
            default:
                return qty;
        }
    }

    public static boolean canMake(
        DatabaseHelper db, long recipeId
    ) {
        Map<String,Double> pantry = new HashMap<>();

        try (Cursor c = db.getPantry()) {
            while (c.moveToNext()) {
                String name = normalize(
                    c.getString(c.getColumnIndexOrThrow("name"))
                );

                double qty = c.getDouble(
                    c.getColumnIndexOrThrow("quantity")
                );

                String unit = c.getString(
                    c.getColumnIndexOrThrow("unit")
                );

                String expiry = c.getString(
                    c.getColumnIndexOrThrow("expiry")
                );

                // Skip expired ingredients when matching recipes.
                if (expiry != null && !expiry.isEmpty()) {
                    String today = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd", Locale.US
                    ).format(new Date());

                    if (expiry.compareTo(today) < 0) continue;
                }

                String key = name + "|" + dimension(unit);

                pantry.put(
                    key,
                    pantry.getOrDefault(key, 0.0)
                        + baseQuantity(qty, unit)
                );
            }
        }

        // Add repeated ingredients together before checking quantities.
        Map<String,Double> needed = new HashMap<>();
        boolean hasRequirements = false;
        try (Cursor required = db.getRecipeIngredients(recipeId)) {
            while (required.moveToNext()) {
                hasRequirements = true;
                String name = normalize(required.getString(0));
                double qty = required.getDouble(1);
                String unit = required.getString(2);

                String key = name + "|" + dimension(unit);
                needed.put(key, needed.getOrDefault(key, 0.0)
                    + baseQuantity(qty, unit));
            }
        }
        if (!hasRequirements) return false;
        for (Map.Entry<String,Double> requirement : needed.entrySet()) {
            if (pantry.getOrDefault(requirement.getKey(), 0.0)
                    + 0.000001 < requirement.getValue()) return false;
        }
        return true;
    }

    public static List<Long> suggestedRecipes(
        DatabaseHelper db
    ) {
        List<Long> results = new ArrayList<>();

        try (Cursor recipes = db.getRecipes()) {
            while (recipes.moveToNext()) {
                long id = recipes.getLong(
                    recipes.getColumnIndexOrThrow("id")
                );

                if (canMake(db, id)) results.add(id);
            }
        }

        return results;
    }
}
