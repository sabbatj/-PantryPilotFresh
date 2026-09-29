package com.pantrypilot.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.database.*;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import java.util.*;

public class IngredientActivity extends Activity {

    private static final int DARK = 0xFF102F35;
    private static final int GREEN = 0xFF23744B;
    private static final int MUTED = 0xFF65746B;
    private static final int BG = 0xFFF8F9F6;

    DatabaseHelper db;
    EditText name, quantity;
    TextView expiry;
    Spinner unit;
    long id = -1;
    String selectedDate = "";

    final String[] units = {
        "pcs", "g", "kg", "ml", "l"
    };

    int dp(float value) {
        return (int) (
            value * getResources()
                .getDisplayMetrics().density + 0.5f
        );
    }

    GradientDrawable background(
        int color, int radius
    ) {
        GradientDrawable drawable =
            new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    TextView text(
        String value, int size,
        int color, boolean bold
    ) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold) {
            view.setTypeface(
                null, Typeface.BOLD
            );
        }

        return view;
    }

    void label(
        LinearLayout parent, String value
    ) {
        TextView view = text(
            value, 13, DARK, true
        );

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(
                -1, -2
            );
        lp.topMargin = dp(19);
        lp.bottomMargin = dp(9);

        parent.addView(view, lp);
    }

    EditText input(
        String hint, int inputType
    ) {
        EditText field = new EditText(this);

        field.setHint(hint);
        field.setTextSize(16);
        field.setTextColor(DARK);
        field.setHintTextColor(0xFF89948C);
        field.setSingleLine(true);
        field.setInputType(inputType);
        field.setFocusable(true);
        field.setFocusableInTouchMode(true);
        field.setClickable(true);
        field.setLongClickable(true);
        field.setSelectAllOnFocus(false);

        field.setPadding(
            dp(15), dp(13),
            dp(15), dp(13)
        );

        GradientDrawable bg =
            background(0xFFFFFFFF, 12);
        bg.setStroke(
            dp(1), 0xFFDCE5DD
        );
        field.setBackground(bg);

        return field;
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

        getWindow().setStatusBarColor(DARK);
        getWindow().setNavigationBarColor(
            0xFFF8F9F6
        );
        getWindow().setSoftInputMode(
            WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_RESIZE
        );

        db = new DatabaseHelper(this);
        id = getIntent().getLongExtra(
            "id", -1
        );

        LinearLayout screen =
            new LinearLayout(this);
        screen.setOrientation(
            LinearLayout.VERTICAL
        );
        screen.setBackgroundColor(BG);

        screen.setFitsSystemWindows(true);
        setContentView(screen);

        LinearLayout header =
            new LinearLayout(this);
        header.setGravity(
            Gravity.CENTER_VERTICAL
        );
        header.setPadding(
            dp(19), dp(19),
            dp(19), dp(19)
        );
        header.setBackgroundColor(DARK);
        header.setFitsSystemWindows(false);

        TextView back = text(
            "‹", 34, 0xFFFFFFFF, false
        );
        back.setGravity(Gravity.CENTER);
        back.setIncludeFontPadding(false);
        back.setTranslationY(-dp(3));
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
        header.addView(
            back,
            new LinearLayout.LayoutParams(
                dp(56), dp(56)
            )
        );

        TextView title = text(
            id == -1
                ? "Add ingredient"
                : "Edit ingredient",
            22, 0xFFFFFFFF, true
        );

        LinearLayout.LayoutParams titleLp =
            new LinearLayout.LayoutParams(
                -1, -2
            );
        titleLp.leftMargin = dp(10);
        header.addView(title, titleLp);

        screen.addView(header);

        ScrollView scroll =
            new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);

        screen.addView(
            scroll,
            new LinearLayout.LayoutParams(
                -1, 0, 1
            )
        );

        LinearLayout form =
            new LinearLayout(this);
        form.setOrientation(
            LinearLayout.VERTICAL
        );
        form.setPadding(
            dp(20), dp(23),
            dp(20), dp(30)
        );

        scroll.addView(form);

        TextView introduction = text(
            id == -1
                ? "Keep your pantry organised"
                : "Update your pantry item",
            21, DARK, true
        );
        form.addView(introduction);

        TextView description = text(
            "Enter the ingredient details below.",
            13, MUTED, false
        );
        description.setPadding(
            0, dp(7), 0, dp(8)
        );
        form.addView(description);

        label(form, "INGREDIENT NAME");

        name = input(
            "e.g. Eggs",
            InputType.TYPE_CLASS_TEXT |
            InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );
        form.addView(name);

        label(form, "QUANTITY");

        quantity = input(
            "e.g. 6",
            InputType.TYPE_CLASS_TEXT |
            InputType.TYPE_TEXT_VARIATION_NORMAL
        );
        quantity.setSelectAllOnFocus(false);
        quantity.setHint("e.g. 6 or 1.5");
        quantity.setImeOptions(
            android.view.inputmethod
                .EditorInfo.IME_ACTION_DONE
        );

        form.addView(quantity);

        label(form, "UNIT");

        unit = new Spinner(this);

        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(
                this,
                android.R.layout
                    .simple_spinner_item,
                units
            );

        adapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        );

        unit.setAdapter(adapter);

        GradientDrawable unitBg =
            background(0xFFFFFFFF, 12);
        unitBg.setStroke(
            dp(1), 0xFFDCE5DD
        );

        unit.setBackground(unitBg);
        unit.setPadding(
            dp(14), dp(4),
            dp(14), dp(4)
        );

        form.addView(
            unit,
            new LinearLayout.LayoutParams(
                -1, dp(55)
            )
        );

        label(form, "EXPIRY DATE");

        expiry = text(
            "Select expiry date (optional)",
            16, MUTED, false
        );
        expiry.setGravity(
            Gravity.CENTER_VERTICAL
        );
        expiry.setPadding(
            dp(15), 0,
            dp(15), 0
        );

        GradientDrawable dateBg =
            background(0xFFFFFFFF, 12);
        dateBg.setStroke(
            dp(1), 0xFFDCE5DD
        );
        expiry.setBackground(dateBg);

        form.addView(
            expiry,
            new LinearLayout.LayoutParams(
                -1, dp(55)
            )
        );

        expiry.setOnClickListener(
            v -> openDatePicker()
        );

        TextView dateHint = text(
            "Leave blank if the ingredient has no expiry date.",
            12, MUTED, false
        );
        dateHint.setPadding(
            0, dp(8), 0, dp(10)
        );
        form.addView(dateHint);

        if (id != -1) {
            load();
        }

        Button save = new Button(this);
        save.setText(
            id == -1
                ? "ADD TO PANTRY"
                : "SAVE CHANGES"
        );
        save.setTextSize(14);
        save.setTextColor(
            0xFFFFFFFF
        );
        save.setAllCaps(false);
        save.setTypeface(
            null, Typeface.BOLD
        );
        save.setBackgroundTintList(
            android.content.res.ColorStateList
                .valueOf(GREEN)
        );

        LinearLayout.LayoutParams saveLp =
            new LinearLayout.LayoutParams(
                -1, dp(55)
            );
        saveLp.topMargin = dp(23);
        form.addView(save, saveLp);

        save.setOnClickListener(
            v -> save()
        );

        if (id != -1) {
            Button delete =
                new Button(this);

            delete.setText(
                "Delete ingredient"
            );
            delete.setAllCaps(false);
            delete.setTextColor(
                0xFFB42318
            );
            delete.setBackgroundTintList(
                android.content.res.ColorStateList
                    .valueOf(0xFFFFEAE7)
            );

            LinearLayout.LayoutParams deleteLp =
                new LinearLayout.LayoutParams(
                    -1, dp(52)
                );
            deleteLp.topMargin = dp(10);

            form.addView(
                delete, deleteLp
            );

            delete.setOnClickListener(
                v -> new AlertDialog.Builder(this)
                    .setTitle(
                        "Delete ingredient?"
                    )
                    .setMessage(
                        "This action cannot be undone."
                    )
                    .setNegativeButton(
                        "Cancel", null
                    )
                    .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {
                            db.deleteIngredient(id);
                            finish();
                        }
                    )
                    .show()
            );
        }

        // Avoid opening the keyboard before the user selects a field.
        screen.setFocusableInTouchMode(true);
        screen.requestFocus();

        name.setImeOptions(
            android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
        );
        quantity.setImeOptions(
            android.view.inputmethod.EditorInfo.IME_ACTION_DONE
        );

        name.setNextFocusForwardId(quantity.getId());

        quantity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId ==
                android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                ((InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                        quantity.getWindowToken(), 0
                    );
                quantity.clearFocus();
                return true;
            }
            return false;
        });
    }

    void openDatePicker() {
        Calendar now =
            Calendar.getInstance();

        if (!selectedDate.isEmpty()) {
            try {
                String[] parts =
                    selectedDate.split("-");

                now.set(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]) - 1,
                    Integer.parseInt(parts[2])
                );
            } catch (Exception ignored) {
            }
        }

        new DatePickerDialog(
            this,
            (picker, year, month, day) -> {
                selectedDate = String.format(
                    Locale.US,
                    "%04d-%02d-%02d",
                    year, month + 1, day
                );

                expiry.setText(
                    selectedDate
                );
                expiry.setTextColor(
                    DARK
                );
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    void load() {
        try (
            Cursor c = db.getReadableDatabase()
                .rawQuery(
                    "SELECT name,quantity,unit,expiry " +
                    "FROM pantry WHERE id=?",
                    new String[]{
                        String.valueOf(id)
                    }
                )
        ) {
            if (c.moveToFirst()) {
                name.setText(
                    c.getString(0)
                );

                double amount =
                    c.getDouble(1);

                if (amount == Math.rint(amount)) {
                    quantity.setText(
                        String.valueOf(
                            (long) amount
                        )
                    );
                } else {
                    quantity.setText(
                        String.valueOf(amount)
                    );
                }

                String savedUnit =
                    c.getString(2);

                for (
                    int i = 0;
                    i < units.length;
                    i++
                ) {
                    if (units[i].equals(
                        savedUnit
                    )) {
                        unit.setSelection(i);
                        break;
                    }
                }

                String date =
                    c.getString(3);

                if (
                    date != null &&
                    !date.isEmpty()
                ) {
                    selectedDate = date;
                    expiry.setText(date);
                    expiry.setTextColor(
                        DARK
                    );
                }
            }
        }
    }

    void save() {
        String n = name.getText()
            .toString().trim();

        String q = quantity.getText()
            .toString().trim()
            .replace(',', '.');

        if (n.isEmpty()) {
            name.setError(
                "Ingredient name is required"
            );
            name.requestFocus();
            return;
        }

        double amount;

        try {
            amount =
                Double.parseDouble(q);
        } catch (Exception e) {
            quantity.setError(
                "Enter a valid quantity"
            );
            quantity.requestFocus();
            return;
        }

        if (
            !Double.isFinite(amount) ||
            amount <= 0
        ) {
            quantity.setError(
                "Quantity must be greater than zero"
            );
            quantity.requestFocus();
            return;
        }

        String selectedUnit =
            unit.getSelectedItem()
                .toString();

        if (id == -1) {
            db.addIngredient(
                n,
                amount,
                selectedUnit,
                selectedDate
            );
        } else {
            db.updateIngredient(
                id,
                n,
                amount,
                selectedUnit,
                selectedDate
            );
        }

        Toast.makeText(
            this,
            id == -1
                ? "Ingredient added"
                : "Ingredient updated",
            Toast.LENGTH_SHORT
        ).show();

        finish();
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
            "Pantry"
        );

        startActivity(intent);
        finish();
    }
}
