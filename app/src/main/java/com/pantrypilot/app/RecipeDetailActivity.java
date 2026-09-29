package com.pantrypilot.app;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class RecipeDetailActivity extends Activity {

    private static final int TEAL = 0xFF082F35;
    private static final int GREEN = 0xFF007747;
    private static final int PALE = 0xFFEAF7EF;
    private static final int BG = 0xFFF7F9F7;
    private static final int MUTED = 0xFF536A68;
    private static final int WHITE = Color.WHITE;

    private int dp(float value) {
        return Math.round(
            value * getResources().getDisplayMetrics().density
        );
    }

    private GradientDrawable background(
        int color, int radius
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private TextView text(
        String value, int size, int color, boolean bold
    ) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold) {
            view.setTypeface(null, Typeface.BOLD);
        }

        return view;
    }

    private LinearLayout vertical() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private LinearLayout horizontal() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        return layout;
    }

    private void space(LinearLayout parent, int height) {
        View spacer = new View(this);
        parent.addView(
            spacer,
            new LinearLayout.LayoutParams(1, dp(height))
        );
    }

    private void heading(
        LinearLayout parent, String title
    ) {
        TextView heading = text(title, 21, TEAL, true);
        parent.addView(heading);
    }

    private String quantity(double number) {
        if (number == Math.rint(number)) {
            return String.format(
                Locale.US, "%.0f", number
            );
        }

        return String.format(
            Locale.US, "%s", number
        );
    }

    private int photoFor(String name) {
        return RecipeImages.get(name);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(0);
        getWindow().clearFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            getWindow().setDecorFitsSystemWindows(true);
        }

        getWindow().setStatusBarColor(TEAL);
        getWindow().setNavigationBarColor(TEAL);

        DatabaseHelper db = new DatabaseHelper(this);

        long id = getIntent().getLongExtra("id", -1);

        try (Cursor recipe =
            db.getReadableDatabase().rawQuery(
                "SELECT name,minutes,servings,method " +
                "FROM recipes WHERE id=?",
                new String[]{String.valueOf(id)}
            )
        ) {
            if (!recipe.moveToFirst()) {
                finish();
                return;
            }

            String name = recipe.getString(0);
            int minutes = recipe.getInt(1);
            int servings = recipe.getInt(2);
            String method = recipe.getString(3);

            render(
                db, id, name, minutes, servings, method
            );
        }
    }

    private void render(
        DatabaseHelper db,
        long id,
        String name,
        int minutes,
        int servings,
        String method
    ) {
        LinearLayout root = vertical();
        root.setBackgroundColor(BG);

        root.setFitsSystemWindows(true);
        setContentView(root);

        LinearLayout toolbar = horizontal();
        toolbar.setBackgroundColor(TEAL);
        toolbar.setFitsSystemWindows(false);
        toolbar.setPadding(
            dp(16), dp(12), dp(18), dp(12)
        );

        TextView back = text("‹", 36, WHITE, false);
        back.setGravity(Gravity.CENTER);
        back.setIncludeFontPadding(false);
        back.setTranslationY(-dp(3));
        back.setContentDescription(
            "Back to suggested recipes"
        );
        back.setBackground(
            background(0xFF16434A, 14)
        );

        toolbar.addView(
            back,
            new LinearLayout.LayoutParams(
                dp(48), dp(48)
            )
        );

        back.setClickable(true);
        back.setFocusable(true);
        back.setContentDescription("Navigate back");
        back.setMinWidth(dp(48));
        back.setMinHeight(dp(48));
        back.setClickable(true);
        back.setFocusable(true);
        back.setContentDescription("Navigate back");
        back.setMinWidth(dp(56));
        back.setMinHeight(dp(56));
        back.setClickable(true);
        back.setFocusable(true);
        back.setContentDescription("Navigate back");
        back.setMinWidth(dp(56));
        back.setMinHeight(dp(56));
        back.setOnClickListener(v -> finish());

        LinearLayout titles = vertical();
        titles.setPadding(dp(14), 0, 0, 0);

        TextView eyebrow = text(
            "PANTRYPILOT FRESH",
            11,
            0xFFBCE6D4,
            true
        );

        TextView toolbarTitle = text(
            "Recipe details",
            20,
            WHITE,
            true
        );

        titles.addView(eyebrow);
        titles.addView(toolbarTitle);

        toolbar.addView(titles);

        root.addView(toolbar);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.setClipToPadding(false);

        root.addView(
            scroll,
            new LinearLayout.LayoutParams(
                -1, 0, 1
            )
        );

        LinearLayout content = vertical();
        content.setPadding(
            dp(16), dp(16), dp(16), dp(32)
        );

        scroll.addView(content);

        ImageView hero = new ImageView(this);
        hero.setImageResource(photoFor(name));
        hero.setScaleType(
            ImageView.ScaleType.CENTER_CROP
        );
        hero.setContentDescription(
            "Photograph representing " + name
        );

        hero.setBackground(background(WHITE, 20));
        hero.setClipToOutline(true);

        content.addView(
            hero,
            new LinearLayout.LayoutParams(
                -1, dp(210)
            )
        );

        space(content, 16);

        LinearLayout summary = vertical();
        summary.setPadding(
            dp(20), dp(20), dp(20), dp(20)
        );
        summary.setBackground(
            background(WHITE, 20)
        );

        TextView label = text(
            "RECIPE OVERVIEW",
            11,
            GREEN,
            true
        );

        summary.addView(label);
        space(summary, 8);

        TextView recipeTitle = text(
            name, 29, TEAL, true
        );

        summary.addView(recipeTitle);
        space(summary, 14);

        LinearLayout metrics = horizontal();

        TextView time = text(
            "◷  " + minutes + " min",
            15, MUTED, false
        );

        TextView serving = text(
            "♧  " + servings +
            (servings == 1
                ? " serving"
                : " servings"),
            15, MUTED, false
        );

        metrics.addView(time);

        View gap = new View(this);
        metrics.addView(
            gap,
            new LinearLayout.LayoutParams(
                dp(22), 1
            )
        );

        metrics.addView(serving);
        summary.addView(metrics);

        content.addView(summary);
        space(content, 24);

        heading(content, "Ingredients");
        space(content, 6);

        TextView ingredientHint = text(
            "Everything you need for this recipe",
            14, MUTED, false
        );

        content.addView(ingredientHint);
        space(content, 14);

        int ingredientCount = 0;

        try (Cursor ingredients =
            db.getRecipeIngredients(id)
        ) {
            while (ingredients.moveToNext()) {
                String ingredientName =
                    ingredients.getString(0);

                double required =
                    ingredients.getDouble(1);

                String unit =
                    ingredients.getString(2);

                LinearLayout row = horizontal();
                row.setPadding(
                    dp(16), dp(16), dp(16), dp(16)
                );
                row.setBackground(
                    background(WHITE, 16)
                );

                TextView check = text(
                    "✓", 19, GREEN, true
                );
                check.setGravity(Gravity.CENTER);
                check.setBackground(
                    background(PALE, 12)
                );

                row.addView(
                    check,
                    new LinearLayout.LayoutParams(
                        dp(42), dp(42)
                    )
                );

                TextView ingredientLabel = text(
                    ingredientName.substring(0, 1)
                        .toUpperCase(Locale.ROOT)
                    + ingredientName.substring(1),
                    16, TEAL, true
                );

                ingredientLabel.setPadding(
                    dp(12), 0, dp(8), 0
                );

                row.addView(
                    ingredientLabel,
                    new LinearLayout.LayoutParams(
                        0, -2, 1
                    )
                );

                TextView amount = text(
                    quantity(required) + " " + unit,
                    15, GREEN, true
                );

                row.addView(amount);

                content.addView(row);
                space(content, 10);

                ingredientCount++;
            }
        }

        space(content, 12);

        heading(content, "Preparation");
        space(content, 6);

        TextView preparationHint = text(
            "Follow these steps to make your meal",
            14, MUTED, false
        );

        content.addView(preparationHint);
        space(content, 14);

        String[] steps = method.split("\\n");
        int stepNumber = 0;

        for (String rawStep : steps) {
            String step = rawStep.trim();

            if (step.isEmpty()) {
                continue;
            }

            stepNumber++;

            LinearLayout stepCard = horizontal();
            stepCard.setGravity(
                Gravity.TOP
            );
            stepCard.setPadding(
                dp(16), dp(18), dp(16), dp(18)
            );
            stepCard.setBackground(
                background(WHITE, 16)
            );

            TextView number = text(
                String.valueOf(stepNumber),
                17, WHITE, true
            );
            number.setGravity(Gravity.CENTER);
            number.setBackground(
                background(GREEN, 13)
            );

            stepCard.addView(
                number,
                new LinearLayout.LayoutParams(
                    dp(42), dp(42)
                )
            );

            LinearLayout stepText = vertical();
            stepText.setPadding(
                dp(14), 0, 0, 0
            );

            TextView stepLabel = text(
                "STEP " + stepNumber,
                11, GREEN, true
            );

            TextView description = text(
                step, 16, TEAL, false
            );
            description.setLineSpacing(
                dp(3), 1
            );

            stepText.addView(stepLabel);
            space(stepText, 5);
            stepText.addView(description);

            stepCard.addView(
                stepText,
                new LinearLayout.LayoutParams(
                    0, -2, 1
                )
            );

            content.addView(stepCard);
            space(content, 10);
        }

        space(content, 14);

        LinearLayout footer = vertical();
        footer.setPadding(
            dp(20), dp(20), dp(20), dp(20)
        );
        footer.setBackground(
            background(PALE, 18)
        );

        TextView footerTitle = text(
            "Ready to get cooking?",
            19, TEAL, true
        );

        TextView footerDescription = text(
            "You have the recipe and all " +
            ingredientCount +
            " ingredient requirements. " +
            "Follow the steps above and enjoy your meal.",
            14, MUTED, false
        );

        footer.addView(footerTitle);
        space(footer, 8);
        footer.addView(footerDescription);

        content.addView(footer);
    }

    private void navigateBack() {
        if (!isTaskRoot()) {
            finish();
            return;
        }

        android.content.Intent intent =
            new android.content.Intent(
                this, MainActivity.class
            );

        intent.putExtra(
            "navigation_destination",
            "Recipes"
        );

        startActivity(intent);
        finish();
    }
}
