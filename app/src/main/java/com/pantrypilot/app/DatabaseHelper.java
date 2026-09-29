
package com.pantrypilot.app;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String NAME = "smartpantry.db";

    public DatabaseHelper(Context context) {
        super(context, NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE pantry (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "quantity REAL NOT NULL CHECK(quantity > 0)," +
            "unit TEXT NOT NULL," +
            "expiry TEXT)"
        );

        db.execSQL(
            "CREATE TABLE recipes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "minutes INTEGER NOT NULL," +
            "servings INTEGER NOT NULL," +
            "method TEXT NOT NULL)"
        );

        db.execSQL(
            "CREATE TABLE recipe_ingredients (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "recipe_id INTEGER NOT NULL," +
            "name TEXT NOT NULL," +
            "quantity REAL NOT NULL," +
            "unit TEXT NOT NULL," +
            "FOREIGN KEY(recipe_id) REFERENCES recipes(id))"
        );

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    }

    public long addIngredient(
        String name, double quantity, String unit, String expiry
    ) {
        ContentValues v = new ContentValues();
        v.put("name", name.trim());
        v.put("quantity", quantity);
        v.put("unit", unit);
        v.put("expiry", expiry);
        return getWritableDatabase().insert("pantry", null, v);
    }

    public int updateIngredient(
        long id, String name, double quantity,
        String unit, String expiry
    ) {
        ContentValues v = new ContentValues();
        v.put("name", name.trim());
        v.put("quantity", quantity);
        v.put("unit", unit);
        v.put("expiry", expiry);

        return getWritableDatabase().update(
            "pantry", v, "id=?",
            new String[]{String.valueOf(id)}
        );
    }

    public int deleteIngredient(long id) {
        return getWritableDatabase().delete(
            "pantry", "id=?",
            new String[]{String.valueOf(id)}
        );
    }

    public Cursor getPantry() {
        return getReadableDatabase().rawQuery(
            "SELECT * FROM pantry ORDER BY name COLLATE NOCASE",
            null
        );
    }

    public Cursor getRecipes() {
        return getReadableDatabase().rawQuery(
            "SELECT * FROM recipes ORDER BY name",
            null
        );
    }

    public Cursor getRecipeIngredients(long recipeId) {
        return getReadableDatabase().rawQuery(
            "SELECT name,quantity,unit FROM recipe_ingredients " +
            "WHERE recipe_id=?",
            new String[]{String.valueOf(recipeId)}
        );
    }

    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Tomato Salad", 10, 2,
            "Wash and slice the tomatoes.\n" +
            "Slice the onion.\n" +
            "Combine and serve.",
            "tomato:2:pcs|onion:1:pcs");

        addRecipe(db, "Scrambled Eggs", 10, 1,
            "Beat the eggs.\n" +
            "Heat the butter in a pan.\n" +
            "Cook the eggs while stirring.",
            "egg:2:pcs|butter:10:g");

        addRecipe(db, "Garlic Toast", 10, 2,
            "Mix the butter and crushed garlic.\n" +
            "Spread onto bread.\n" +
            "Toast until golden.",
            "bread:2:pcs|butter:20:g|garlic:2:pcs");

        addRecipe(db, "Tomato Pasta", 25, 2,
            "Boil the pasta.\n" +
            "Cook the tomatoes and garlic.\n" +
            "Combine with the drained pasta.",
            "pasta:200:g|tomato:3:pcs|garlic:2:pcs");

        addRecipe(db, "Chicken Fried Rice", 30, 2,
            "Cook the rice.\n" +
            "Cook the chicken thoroughly.\n" +
            "Add onion and carrot.\n" +
            "Stir in the rice and egg.",
            "rice:200:g|chicken:200:g|" +
            "onion:1:pcs|carrot:1:pcs|egg:1:pcs");

        addRecipe(db, "Vegetable Omelette", 15, 1,
            "Beat the eggs.\n" +
            "Chop the tomato and onion.\n" +
            "Cook everything in a pan.",
            "egg:2:pcs|tomato:1:pcs|onion:1:pcs");

        addRecipe(db, "Mashed Potatoes", 25, 2,
            "Peel and boil the potatoes.\n" +
            "Drain and mash with butter and milk.",
            "potato:4:pcs|butter:30:g|milk:100:ml");

        addRecipe(db, "Egg Sandwich", 15, 1,
            "Boil the eggs.\n" +
            "Slice and place between bread.",
            "egg:2:pcs|bread:2:pcs");

        addRecipe(db, "Chicken Pasta", 30, 2,
            "Boil the pasta.\n" +
            "Cook the chicken thoroughly.\n" +
            "Add milk and combine.",
            "chicken:200:g|pasta:200:g|milk:100:ml");

        addRecipe(db, "Potato Wedges", 35, 2,
            "Cut potatoes into wedges.\n" +
            "Coat with oil.\n" +
            "Bake until golden and cooked through.",
            "potato:4:pcs|oil:30:ml");

        addRecipe(db, "French Toast", 15, 2,
            "Beat eggs with milk.\n" +
            "Dip bread into the mixture.\n" +
            "Cook both sides until golden.",
            "bread:4:pcs|egg:2:pcs|milk:100:ml");

        addRecipe(db, "Tomato Rice", 25, 2,
            "Cook the rice.\n" +
            "Cook the chopped tomato and onion.\n" +
            "Combine and serve.",
            "rice:200:g|tomato:2:pcs|onion:1:pcs");

        addRecipe(db, "Chicken Sandwich", 20, 1,
            "Cook the chicken thoroughly.\n" +
            "Slice and place between bread.",
            "chicken:150:g|bread:2:pcs");

        addRecipe(db, "Garlic Potatoes", 35, 2,
            "Cut and boil the potatoes.\n" +
            "Cook with butter and crushed garlic.",
            "potato:4:pcs|garlic:2:pcs|butter:20:g");

        addRecipe(db, "Cheese Omelette", 15, 1,
            "Beat the eggs.\n" +
            "Cook in a pan.\n" +
            "Add cheese and fold.",
            "egg:2:pcs|cheese:50:g");

        addRecipe(db, "Carrot Soup", 35, 2,
            "Chop the carrots, potato and onion.\n" +
            "Boil in water until tender.\n" +
            "Blend and serve.",
            "carrot:4:pcs|potato:1:pcs|" +
            "onion:1:pcs|water:500:ml");
    }

    private void addRecipe(
        SQLiteDatabase db, String name,
        int minutes, int servings,
        String method, String ingredients
    ) {
        ContentValues recipe = new ContentValues();
        recipe.put("name", name);
        recipe.put("minutes", minutes);
        recipe.put("servings", servings);
        recipe.put("method", method);

        long id = db.insertOrThrow("recipes", null, recipe);

        for (String item : ingredients.split("\\|")) {
            String[] parts = item.split(":");

            ContentValues ingredient = new ContentValues();
            ingredient.put("recipe_id", id);
            ingredient.put("name", parts[0]);
            ingredient.put("quantity", Double.parseDouble(parts[1]));
            ingredient.put("unit", parts[2]);

            db.insertOrThrow(
                "recipe_ingredients", null, ingredient
            );
        }
    }
}
