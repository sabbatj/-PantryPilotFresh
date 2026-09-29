package com.pantrypilot.app;

public final class RecipeImages {

    private RecipeImages() {}

    public static int get(String name) {
        if (name == null) return 0;

        switch (name.trim()) {
            case "Tomato Salad":
                return R.drawable.recipe_tomato_salad;
            case "Scrambled Eggs":
                return R.drawable.recipe_scrambled_eggs;
            case "Garlic Toast":
                return R.drawable.recipe_garlic_toast;
            case "Tomato Pasta":
                return R.drawable.recipe_tomato_pasta;
            case "Chicken Fried Rice":
                return R.drawable.recipe_chicken_fried_rice;
            case "Vegetable Omelette":
                return R.drawable.recipe_vegetable_omelette;
            case "Mashed Potatoes":
                return R.drawable.recipe_mashed_potatoes;
            case "Egg Sandwich":
                return R.drawable.recipe_egg_sandwich;
            case "Chicken Pasta":
                return R.drawable.recipe_chicken_pasta;
            case "Potato Wedges":
                return R.drawable.recipe_potato_wedges;
            case "French Toast":
                return R.drawable.recipe_french_toast;
            case "Tomato Rice":
                return R.drawable.recipe_tomato_rice;
            case "Chicken Sandwich":
                return R.drawable.recipe_chicken_sandwich;
            case "Garlic Potatoes":
                return R.drawable.recipe_garlic_potatoes;
            case "Cheese Omelette":
                return R.drawable.recipe_cheese_omelette;
            case "Carrot Soup":
                return R.drawable.recipe_carrot_soup;
            default:
                return 0;
        }
    }
}
