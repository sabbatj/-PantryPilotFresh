package com.pantrypilot.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import android.text.*;
import java.util.*;

public class MainActivity extends Activity {

    final int NAVY = 0xFF102F35;
    final int TEAL = 0xFF23744B;
    final int GREEN = 0xFF226D43;
    final int CREAM = 0xFFF8F9F6;
    final int MUTED = 0xFF65747A;
    final int CORAL = 0xFFE66C50;

    DatabaseHelper db;
    LinearLayout root, body;
    String page = "Home";

    int dp(int value) {
        return (int)(value *
            getResources().getDisplayMetrics().density);
    }

    GradientDrawable shape(int color,int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    TextView text(String value,int size,int color,
                  boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if(bold)t.setTypeface(null,Typeface.BOLD);
        return t;
    }

    LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    void pad(View v,int n) {
        v.setPadding(dp(n),dp(n),dp(n),dp(n));
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        db = new DatabaseHelper(this);
        show("Home");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null && root != null) {
            if (returningFromIngredient) {
                returningFromIngredient = false;
                show("Pantry");
            } else {
                show(page);
            }
        }
    }

    private boolean returningFromIngredient = false;

    @Override
    public void onBackPressed() {
        if (!"Home".equals(page)) {
            show("Home");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(
        android.content.Intent intent
    ) {
        super.onNewIntent(intent);
        setIntent(intent);

        String destination = intent.getStringExtra(
            "navigation_destination"
        );

        if ("Pantry".equals(destination) ||
            "Recipes".equals(destination)) {
            show(destination);
        }
    }

    private boolean initialDestinationHandled = false;

    void show(String destination) {
        if (!initialDestinationHandled) {
            initialDestinationHandled = true;

            String requested = getIntent()
                .getStringExtra("navigation_destination");

            if ("Pantry".equals(requested) ||
                "Recipes".equals(requested)) {
                destination = requested;
            }
        }

        page=destination;

        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);
        getWindow().getDecorView()
            .setSystemUiVisibility(0);

        root=column();
        root.setBackgroundColor(NAVY);

        // Keep the screen clear of the system bars.
        root.setOnApplyWindowInsetsListener((v,insets)->{
            if(Build.VERSION.SDK_INT>=30) {
                android.graphics.Insets bars=insets.getInsets(
                    android.view.WindowInsets.Type.statusBars()
                    | android.view.WindowInsets.Type.navigationBars()
                );
                v.setPadding(0,bars.top,0,bars.bottom);
                return android.view.WindowInsets.CONSUMED;
            }
            return insets;
        });

        setContentView(root);


        LinearLayout header = column();
        header.setBackgroundColor(NAVY);
        header.setPadding(
            dp(17), dp(12), dp(17), dp(17)
        );

        String title =
            "Home".equals(destination)
                ? "Hello there!"
                : "Pantry".equals(destination)
                    ? "My Pantry"
                    : "Recipes".equals(destination)
                        ? "Suggested Recipes"
                        : "Settings";

        String subtitle =
            "Home".equals(destination)
                ? "Here's what's happening in your kitchen."
                : "Pantry".equals(destination)
                    ? "Manage your ingredients and quantities."
                    : "Recipes".equals(destination)
                        ? "Meals you can make with your ingredients."
                        : "Make your pantry work for you.";

        android.widget.FrameLayout banner =
            new android.widget.FrameLayout(this);

        android.graphics.drawable.GradientDrawable
            rounded =
            new android.graphics.drawable.GradientDrawable();

        rounded.setColor(NAVY);
        rounded.setCornerRadius(dp(17));

        banner.setBackground(rounded);
        banner.setClipToOutline(true);

        ImageView photograph = new ImageView(this);

        int headerImage =
            "Home".equals(destination)
                ? R.drawable.header_home
                : "Pantry".equals(destination)
                    ? R.drawable.header_pantry
                    : "Recipes".equals(destination)
                        ? R.drawable.header_recipes
                        : R.drawable.header_settings;

        photograph.setImageResource(headerImage);
        photograph.setScaleType(
            ImageView.ScaleType.CENTER_CROP
        );
        photograph.setContentDescription(
            "Decorative photograph"
        );

        banner.addView(
            photograph,
            new android.widget.FrameLayout.LayoutParams(
                -1, -1
            )
        );

        // Make the heading easier to read over the image.
        View overlay = new View(this);
        overlay.setBackgroundColor(0x9908292C);

        banner.addView(
            overlay,
            new android.widget.FrameLayout.LayoutParams(
                -1, -1
            )
        );

        LinearLayout bannerContent = column();
        bannerContent.setGravity(
            Gravity.CENTER_VERTICAL
        );
        bannerContent.setPadding(
            dp(19), dp(17), dp(19), dp(17)
        );

        TextView brand = text(
            "SMART PANTRY MANAGER",
            11,
            0xFFC9E6D6,
            true
        );
        bannerContent.addView(brand);

        TextView pageTitle = text(
            title,
            "Recipes".equals(destination) ? 23 : 27,
            0xFFFFFFFF,
            true
        );
        pageTitle.setPadding(
            0, dp(13), 0, dp(6)
        );
        bannerContent.addView(pageTitle);

        TextView description = text(
            subtitle,
            13,
            0xFFE5F0EA,
            false
        );
        bannerContent.addView(description);

        banner.addView(
            bannerContent,
            new android.widget.FrameLayout.LayoutParams(
                -1, -1
            )
        );

        header.addView(
            banner,
            new LinearLayout.LayoutParams(
                -1,
                "Recipes".equals(destination)
                    ? dp(162)
                    : dp(155)
            )
        );

        root.addView(header);

        ScrollView scroll=new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(CREAM);

        body=column();
        body.setPadding(
            dp(17),dp(19),dp(17),dp(20)
        );

        scroll.addView(body);
        root.addView(scroll,
            new LinearLayout.LayoutParams(-1,0,1));

        switch(destination) {
            case "Pantry": pantry();break;
            case "Recipes": recipes();break;
            case "Settings": settings();break;
            default:home();
        }

        bottomNavigation();
    }

    void bottomNavigation() {
        LinearLayout nav=new LinearLayout(this);
        nav.setBackgroundColor(0xFFFFFFFF);
        nav.setElevation(dp(10));

        String[] pages={
            "Home","Pantry","Recipes","Settings"
        };

        String[] icons={
            "⌂","▣","▤","⚙"
        };

        for(int i=0;i<pages.length;i++) {
            final String target=pages[i];
            boolean selected=page.equals(target);

            LinearLayout tab=column();
            tab.setGravity(Gravity.CENTER);
            tab.setPadding(0,dp(8),0,dp(8));

            TextView icon=text(
                icons[i],23,
                selected?TEAL:MUTED,false
            );
            icon.setGravity(Gravity.CENTER);
            tab.addView(icon);

            TextView name=text(
                target,11,
                selected?TEAL:MUTED,selected
            );
            name.setGravity(Gravity.CENTER);
            tab.addView(name);

            nav.addView(tab,
                new LinearLayout.LayoutParams(0,dp(62),1));

            tab.setOnClickListener(v->show(target));
        }

        root.addView(nav);
    }

    void heading(String title) {
        TextView t=text(title,20,NAVY,true);
        t.setPadding(0,dp(5),0,dp(15));
        body.addView(t);
    }

    void card(String title,String subtitle,
              Runnable action) {
        LinearLayout c=column();
        c.setBackground(shape(0xFFFFFFFF,15));
        c.setElevation(dp(2));
        pad(c,17);

        c.addView(text(title,16,NAVY,true));

        if(subtitle!=null) {
            TextView d=text(
                subtitle,13,MUTED,false
            );
            d.setPadding(0,dp(7),0,0);
            c.addView(d);
        }

        LinearLayout.LayoutParams lp=
            new LinearLayout.LayoutParams(-1,-2);
        lp.bottomMargin=dp(12);
        body.addView(c,lp);

        if(action!=null)
            c.setOnClickListener(v->action.run());
    }

    void button(String title,Runnable action) {
        TextView b=text(
            title,15,0xFFFFFFFF,true
        );
        b.setGravity(Gravity.CENTER);
        b.setBackground(shape(TEAL,12));
        b.setPadding(dp(15),dp(15),dp(15),dp(15));

        LinearLayout.LayoutParams lp=
            new LinearLayout.LayoutParams(-1,-2);
        lp.bottomMargin=dp(15);

        body.addView(b,lp);
        b.setOnClickListener(v->action.run());
    }

    int pantryCount() {
        try(Cursor c=db.getPantry()) {
            return c.getCount();
        }
    }

    // Use the same expiry rule on Home and the expiry screen.
    // Turning reminders off should not affect recipe matching.
    boolean expiryRemindersEnabled() {
        return getSharedPreferences("smart_pantry", MODE_PRIVATE)
            .getBoolean("expiry_alerts", true);
    }

    int reminderDays() {
        return getSharedPreferences("smart_pantry", MODE_PRIVATE)
            .getInt("expiry_days", 3);
    }

    boolean withinReminderWindow(String date) {
        if (date == null || date.isEmpty()) return false;
        try {
            java.text.SimpleDateFormat fmt =
                new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);
            fmt.setLenient(false);
            java.util.Date expiryDate = fmt.parse(date);
            Calendar limit = Calendar.getInstance();
            limit.set(Calendar.HOUR_OF_DAY, 0);
            limit.set(Calendar.MINUTE, 0);
            limit.set(Calendar.SECOND, 0);
            limit.set(Calendar.MILLISECOND, 0);
            limit.add(Calendar.DAY_OF_YEAR, reminderDays());
            // Keep expired items visible so they can be removed.
            return !expiryDate.after(limit.getTime());
        } catch (java.text.ParseException e) {
            return false;
        }
    }

    int expiryCount() {
        if (!expiryRemindersEnabled()) return 0;
        int count = 0;
        try (Cursor c = db.getPantry()) {
            while (c.moveToNext()) {
                if (withinReminderWindow(c.getString(
                    c.getColumnIndexOrThrow("expiry")))) count++;
            }
        }
        return count;
    }

    void home() {
        final int ink = 0xFF103A35;
        final int muted = 0xFF64766F;
        final int green = 0xFF007B53;

        int ingredients = pantryCount();
        java.util.List<Long> ready =
            MatchingUtils.suggestedRecipes(db);
        int expiring = expiryCount();

        TextView overview = text(
            "YOUR OVERVIEW", 11, green, true
        );
        overview.setLetterSpacing(0.12f);
        overview.setPadding(0, dp(2), 0, dp(7));
        body.addView(overview);

        TextView overviewTitle = text(
            "Your kitchen at a glance", 22, ink, true
        );
        body.addView(overviewTitle);

        TextView overviewDescription = text(
            "Everything you need, all in one place.",
            13, muted, false
        );
        overviewDescription.setPadding(
            0, dp(5), 0, dp(19)
        );
        body.addView(overviewDescription);

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);

        dashboardStat(
            stats,
            String.valueOf(ingredients),
            "Ingredients",
            "▣",
            () -> show("Pantry")
        );

        dashboardStat(
            stats,
            String.valueOf(ready.size()),
            "Ready recipes",
            "▤",
            () -> show("Recipes")
        );

        dashboardStat(
            stats,
            String.valueOf(expiring),
            "Expiring soon",
            "◷",
            () -> expiry()
        );

        LinearLayout.LayoutParams statsParams =
            new LinearLayout.LayoutParams(-1, -2);
        statsParams.bottomMargin = dp(24);
        body.addView(stats, statsParams);

        LinearLayout pantryCard = column();
        pantryCard.setBackground(
            shape(0xFFEAF5EE, 20)
        );
        pantryCard.setPadding(
            dp(20), dp(21), dp(20), dp(20)
        );

        TextView pantryEyebrow = text(
            "PANTRY MANAGEMENT", 10, green, true
        );
        pantryEyebrow.setLetterSpacing(0.12f);
        pantryCard.addView(pantryEyebrow);

        TextView pantryTitle = text(
            "Keep your pantry organised",
            21, ink, true
        );
        pantryTitle.setPadding(
            0, dp(10), 0, dp(7)
        );
        pantryCard.addView(pantryTitle);

        TextView pantryDescription = text(
            "Track your ingredients, update quantities " +
            "and keep expiry dates in check.",
            13, muted, false
        );
        pantryDescription.setLineSpacing(dp(3), 1f);
        pantryCard.addView(pantryDescription);

        TextView add = text(
            "+  Add ingredient",
            15, 0xFFFFFFFF, true
        );
        add.setGravity(Gravity.CENTER);
        add.setMinHeight(dp(52));
        add.setPadding(
            dp(15), dp(15), dp(15), dp(15)
        );
        add.setBackground(shape(green, 12));
        add.setContentDescription("Add ingredient");
        add.setClickable(true);
        add.setOnClickListener(v -> addIngredient());

        LinearLayout.LayoutParams addParams =
            new LinearLayout.LayoutParams(-1, -2);
        addParams.topMargin = dp(20);
        pantryCard.addView(add, addParams);

        LinearLayout.LayoutParams pantryParams =
            new LinearLayout.LayoutParams(-1, -2);
        pantryParams.bottomMargin = dp(27);
        body.addView(pantryCard, pantryParams);

        LinearLayout sectionHeader =
            new LinearLayout(this);
        sectionHeader.setOrientation(
            LinearLayout.HORIZONTAL
        );
        sectionHeader.setGravity(
            Gravity.CENTER_VERTICAL
        );

        TextView readyTitle = text(
            "Ready to cook", 21, ink, true
        );
        sectionHeader.addView(
            readyTitle,
            new LinearLayout.LayoutParams(0, -2, 1)
        );

        TextView viewAll = text(
            "View all  →", 12, green, true
        );
        viewAll.setPadding(
            dp(8), dp(10), dp(2), dp(10)
        );
        viewAll.setOnClickListener(
            v -> show("Recipes")
        );
        sectionHeader.addView(viewAll);

        LinearLayout.LayoutParams sectionParams =
            new LinearLayout.LayoutParams(-1, -2);
        sectionParams.bottomMargin = dp(12);
        body.addView(sectionHeader, sectionParams);

        LinearLayout recipeCard = column();
        recipeCard.setBackground(
            shape(0xFFFFFFFF, 17)
        );
        recipeCard.setPadding(
            dp(19), dp(19), dp(19), dp(19)
        );

        TextView recipeBadge = text(
            ready.isEmpty()
                ? "BUILD YOUR PANTRY"
                : "✓  READY TO COOK",
            10,
            ready.isEmpty() ? muted : green,
            true
        );
        recipeBadge.setLetterSpacing(0.08f);
        recipeCard.addView(recipeBadge);

        TextView recipeCount = text(
            ready.isEmpty()
                ? "Discover your next meal"
                : ready.size() +
                    (ready.size() == 1
                        ? " recipe available"
                        : " recipes available"),
            19, ink, true
        );
        recipeCount.setPadding(
            0, dp(9), 0, dp(5)
        );
        recipeCard.addView(recipeCount);

        TextView recipeDescription = text(
            ready.isEmpty()
                ? "Add more ingredients to unlock " +
                  "recipes you can make."
                : "Meals matched to the ingredients " +
                  "and quantities in your pantry.",
            13, muted, false
        );
        recipeDescription.setLineSpacing(dp(2), 1f);
        recipeCard.addView(recipeDescription);

        TextView explore = text(
            "Explore recipes  →",
            14, green, true
        );
        explore.setPadding(
            0, dp(17), 0, dp(2)
        );
        explore.setOnClickListener(
            v -> show("Recipes")
        );
        recipeCard.addView(explore);

        recipeCard.setClickable(true);
        recipeCard.setOnClickListener(
            v -> show("Recipes")
        );

        LinearLayout.LayoutParams recipeParams =
            new LinearLayout.LayoutParams(-1, -2);
        recipeParams.bottomMargin = dp(20);
        body.addView(recipeCard, recipeParams);
    }


    
    void dashboardStat(
        LinearLayout parent,
        String value,
        String label,
        String symbol,
        Runnable action
    ) {
        final int ink = 0xFF103A35;
        final int muted = 0xFF64766F;
        final int green = 0xFF007B53;

        LinearLayout stat = column();
        stat.setGravity(Gravity.CENTER);
        stat.setBackground(
            shape(0xFFFFFFFF, 16)
        );
        stat.setPadding(
            dp(4), dp(15), dp(4), dp(15)
        );

        TextView icon = text(
            symbol, 19, green, true
        );
        icon.setGravity(Gravity.CENTER);
        stat.addView(icon);

        TextView number = text(
            value, 27, ink, true
        );
        number.setGravity(Gravity.CENTER);
        number.setPadding(
            0, dp(5), 0, dp(3)
        );
        stat.addView(number);

        TextView caption = text(
            label, 11, muted, false
        );
        caption.setGravity(Gravity.CENTER);
        caption.setMaxLines(2);
        stat.addView(caption);

        stat.setContentDescription(
            value + " " + label
        );
        stat.setClickable(true);
        stat.setOnClickListener(
            v -> action.run()
        );

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                0, dp(120), 1
            );
        params.setMargins(
            dp(3), 0, dp(3), 0
        );
        parent.addView(stat, params);
    }


    void summary(String icon,String title,
                 String subtitle,int tint,
                 Runnable action) {
        LinearLayout row=new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackground(shape(0xFFFFFFFF,15));
        row.setElevation(dp(2));
        pad(row,14);

        TextView illustration=text(
            icon,27,GREEN,true
        );
        illustration.setGravity(Gravity.CENTER);
        illustration.setBackground(shape(tint,12));

        row.addView(illustration,
            new LinearLayout.LayoutParams(
                dp(57),dp(57)));

        LinearLayout info=column();
        info.setPadding(dp(14),0,0,0);

        info.addView(text(title,16,NAVY,true));

        TextView detail=text(
            subtitle,13,MUTED,false
        );
        detail.setPadding(0,dp(5),0,0);
        info.addView(detail);

        row.addView(info,
            new LinearLayout.LayoutParams(0,-2,1));

        row.addView(text("›",26,TEAL,true));

        LinearLayout.LayoutParams lp=
            new LinearLayout.LayoutParams(-1,-2);
        lp.bottomMargin=dp(13);

        body.addView(row,lp);
        row.setOnClickListener(v->action.run());
    }

    void quickAction(LinearLayout parent,
                     String icon,String title,
                     Runnable action) {
        LinearLayout c=column();
        c.setGravity(Gravity.CENTER);
        c.setBackground(shape(0xFFFFFFFF,14));
        c.setElevation(dp(2));
        pad(c,16);

        TextView symbol=text(
            icon,27,TEAL,true
        );
        symbol.setGravity(Gravity.CENTER);
        c.addView(symbol);

        TextView label=text(
            title,13,NAVY,true
        );
        label.setGravity(Gravity.CENTER);
        label.setPadding(0,dp(9),0,0);
        c.addView(label);

        LinearLayout.LayoutParams lp=
            new LinearLayout.LayoutParams(0,dp(110),1);
        lp.setMargins(dp(3),dp(3),dp(8),dp(8));

        parent.addView(c,lp);
        c.setOnClickListener(v->action.run());
    }

    void addIngredient() {
        returningFromIngredient = true;
        startActivity(new Intent(
            this,IngredientActivity.class
        ));
    }

    String emoji(String name) {
        String n=name.toLowerCase(Locale.ROOT);
        if(n.contains("tomato"))return "🍅";
        if(n.contains("onion"))return "🧅";
        if(n.contains("garlic"))return "🧄";
        if(n.contains("chicken"))return "🍗";
        if(n.contains("rice"))return "🍚";
        if(n.contains("egg"))return "🥚";
        if(n.contains("potato"))return "🥔";
        if(n.contains("carrot"))return "🥕";
        if(n.contains("bread"))return "🍞";
        if(n.contains("milk"))return "🥛";
        if(n.contains("cheese"))return "🧀";
        if(n.contains("pasta"))return "🍝";
        return "🥬";
    }


    void pantry() {
        ArrayList<PantryItem> items = new ArrayList<>();

        try (Cursor c = db.getPantry()) {
            while (c.moveToNext()) {
                items.add(new PantryItem(
                    c.getLong(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("quantity")),
                    c.getString(c.getColumnIndexOrThrow("unit")),
                    c.getString(c.getColumnIndexOrThrow("expiry"))
                ));
            }
        }

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);

        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(14);
        search.setHint("⌕  Search ingredients...");
        search.setCompoundDrawablePadding(dp(8));
        search.setPadding(dp(16), 0, dp(12), 0);
        search.setBackground(shape(0xFFFFFFFF, 13));

        toolbar.addView(search,
            new LinearLayout.LayoutParams(0, dp(52), 1));

        TextView add = text("+", 29, 0xFFFFFFFF, true);
        add.setGravity(Gravity.CENTER);
        add.setBackground(shape(0xFF23744B, 13));

        LinearLayout.LayoutParams ap =
            new LinearLayout.LayoutParams(dp(52), dp(52));
        ap.leftMargin = dp(10);
        toolbar.addView(add, ap);
        add.setOnClickListener(v -> addIngredient());

        body.addView(toolbar);

        LinearLayout section = new LinearLayout(this);
        section.setGravity(Gravity.CENTER_VERTICAL);
        section.setPadding(0, dp(18), 0, dp(14));

        TextView heading = text(
            "Your ingredients", 20, 0xFF173F35, true);
        section.addView(heading,
            new LinearLayout.LayoutParams(0, -2, 1));

        TextView count = text(
            items.size() + (items.size() == 1 ? " item" : " items"),
            12, 0xFF23744B, true);
        count.setGravity(Gravity.CENTER);
        count.setPadding(dp(12), dp(7), dp(12), dp(7));
        count.setBackground(shape(0xFFE7F1E8, 20));
        section.addView(count);

        body.addView(section);

        if (items.isEmpty()) {
            LinearLayout emptyCard = column();
            emptyCard.setGravity(Gravity.CENTER);
            emptyCard.setPadding(
                dp(20), dp(23), dp(20), dp(23));
            emptyCard.setBackground(shape(0xFFEAF4EC, 20));

            TextView symbol = text(
                "🧺", 40, 0xFF23744B, true);
            symbol.setGravity(Gravity.CENTER);
            symbol.setBackground(shape(0xFFFFFFFF, 20));
            emptyCard.addView(symbol,
                new LinearLayout.LayoutParams(dp(68), dp(68)));

            TextView title = text(
                "Your pantry is empty",
                21, 0xFF173F35, true);
            title.setGravity(Gravity.CENTER);
            title.setPadding(0, dp(14), 0, dp(8));
            emptyCard.addView(title);

            TextView description = text(
                "Add your first ingredient to start " +
                "discovering meals you can make.",
                14, 0xFF52665C, false);
            description.setGravity(Gravity.CENTER);
            description.setPadding(0, 0, 0, dp(18));
            emptyCard.addView(description);

            TextView action = text(
                "+  Add ingredient", 16, 0xFFFFFFFF, true);
            action.setGravity(Gravity.CENTER);
            action.setPadding(
                dp(15), dp(16), dp(15), dp(16));
            action.setBackground(shape(0xFF23744B, 12));
            emptyCard.addView(action,
                new LinearLayout.LayoutParams(-1, -2));
            action.setOnClickListener(v -> addIngredient());

            body.addView(emptyCard);
            return;
        }

        ListView pantryList = new ListView(this);
        pantryList.setDivider(null);
        pantryList.setDividerHeight(0);
        pantryList.setCacheColorHint(0x00000000);
        pantryList.setBackgroundColor(0x00000000);
        pantryList.setVerticalScrollBarEnabled(false);

        PantryAdapter adapter = new PantryAdapter(items);
        pantryList.setAdapter(adapter);

        TextView noResults = text(
            "No matching ingredients found.",
            14, 0xFF52665C, false
        );
        noResults.setGravity(Gravity.CENTER);
        noResults.setPadding(0, dp(35), 0, dp(35));

        body.addView(noResults);
        body.addView(pantryList);

        Runnable updateList = () -> {
            noResults.setVisibility(
                adapter.getCount() == 0 ? View.VISIBLE : View.GONE
            );

            pantryList.setVisibility(
                adapter.getCount() == 0 ? View.GONE : View.VISIBLE
            );

            // The page already scrolls, so the list needs its own height.
            int totalHeight = 0;
            int width = getResources()
                .getDisplayMetrics().widthPixels - dp(40);

            for (int i = 0; i < adapter.getCount(); i++) {
                View row = adapter.getView(i, null, pantryList);

                row.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        width, View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        0, View.MeasureSpec.UNSPECIFIED
                    )
                );

                totalHeight += row.getMeasuredHeight();
            }

            pantryList.setLayoutParams(
                new LinearLayout.LayoutParams(
                    -1, Math.max(dp(1), totalHeight)
                )
            );
        };

        updateList.run();

        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(
                CharSequence s, int start, int count, int after) {}

            public void onTextChanged(
                CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
                updateList.run();
            }

            public void afterTextChanged(Editable e) {}
        });
    }

    static class PantryItem {
        long id;
        String name,unit,expiry;
        double quantity;

        PantryItem(long id,String name,
                   double quantity,String unit,
                   String expiry) {
            this.id=id;
            this.name=name;
            this.quantity=quantity;
            this.unit=unit;
            this.expiry=expiry;
        }
    }

    class PantryAdapter extends BaseAdapter {
        ArrayList<PantryItem> all,visible;

        PantryAdapter(ArrayList<PantryItem> items) {
            all=items;
            visible=new ArrayList<>(items);
        }

        void filter(String query) {
            visible.clear();
            for(PantryItem item:all) {
                if(item.name.toLowerCase(Locale.ROOT)
                    .contains(query.toLowerCase(Locale.ROOT)))
                    visible.add(item);
            }
            notifyDataSetChanged();
        }

        public int getCount(){return visible.size();}
        public Object getItem(int p){return visible.get(p);}
        public long getItemId(int p){return visible.get(p).id;}

        @Override
        public View getView(
            int position, View convertView, ViewGroup parent
        ) {
            PantryItem item = visible.get(position);

            LinearLayout wrapper = column();
            wrapper.setPadding(0, 0, 0, dp(14));

                LinearLayout card = new LinearLayout(MainActivity.this);
                card.setGravity(Gravity.CENTER_VERTICAL);
                card.setPadding(
                    dp(15), dp(17), dp(15), dp(17));
                card.setBackground(shape(0xFFFFFFFF, 19));
                card.setElevation(dp(1));

                int ingredientImage =
                    ingredientPhoto(item.name);

                if (ingredientImage != 0) {
                    ImageView photoView =
                        new ImageView(MainActivity.this);
                    photoView.setImageResource(
                        ingredientImage
                    );
                    photoView.setScaleType(
                        ImageView.ScaleType.CENTER_CROP
                    );
                    photoView.setContentDescription(
                        item.name
                    );
                    photoView.setBackground(
                        shape(0xFFEAF2E8, 12)
                    );
                    photoView.setClipToOutline(true);

                    card.addView(
                        photoView,
                        new LinearLayout.LayoutParams(
                            dp(66), dp(66)
                        )
                    );
                } else {
                    TextView icon = text(
                        emoji(item.name),
                        27, 0xFF173F35, false
                    );
                    icon.setGravity(Gravity.CENTER);
                    icon.setBackground(
                        shape(0xFFEAF2E8, 12)
                    );
                    card.addView(
                        icon,
                        new LinearLayout.LayoutParams(
                            dp(66), dp(66)
                        )
                    );
                }

                LinearLayout info = column();
                info.setPadding(dp(15), 0, 0, 0);

                info.addView(text(
                    item.name, 16, 0xFF173F35, true));

                String quantity = String.format(
                    Locale.US, "%s %s",
                    item.quantity == (long)item.quantity
                        ? String.valueOf((long)item.quantity)
                        : String.valueOf(item.quantity),
                    item.unit == null ? "" : item.unit
                );

                TextView amount = text(
                    quantity, 14, 0xFF52665C, false);
                amount.setPadding(0, dp(4), 0, 0);
                info.addView(amount);

                if (item.expiry != null &&
                    !item.expiry.isEmpty()) {
                    TextView expiry = text(
                        "Expires " + item.expiry,
                        12, 0xFFC16C2B, false);
                    expiry.setPadding(0, dp(4), 0, 0);
                    info.addView(expiry);
                }

                
                String freshness = "No expiry date";
                int badgeBackground = 0xFFF0F4F0;
                int badgeText = 0xFF52665C;

                if (item.expiry != null && !item.expiry.isEmpty()) {
                    try {
                        java.text.SimpleDateFormat fmt =
                            new java.text.SimpleDateFormat(
                                "yyyy-MM-dd", Locale.US);
                        fmt.setLenient(false);
                        java.util.Date expiryDate =
                            fmt.parse(item.expiry);

                        java.util.Calendar today =
                            java.util.Calendar.getInstance();
                        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
                        today.set(java.util.Calendar.MINUTE, 0);
                        today.set(java.util.Calendar.SECOND, 0);
                        today.set(java.util.Calendar.MILLISECOND, 0);

                        java.util.Calendar limit =
                            (java.util.Calendar) today.clone();
                        int warningDays = getSharedPreferences(
                            "smart_pantry", MODE_PRIVATE
                        ).getInt("expiry_days", 3);
                        limit.add(
                            java.util.Calendar.DAY_OF_YEAR,
                            warningDays
                        );

                        if (expiryDate.before(today.getTime())) {
                            freshness = "Expired";
                            badgeBackground = 0xFFFCE9E7;
                            badgeText = 0xFFB44336;
                        } else if (!expiryDate.after(limit.getTime())) {
                            freshness = "Expiring soon";
                            badgeBackground = 0xFFFFF0DF;
                            badgeText = 0xFFB95A12;
                        } else {
                            freshness = "Within date";
                            badgeBackground = 0xFFE7F5E8;
                            badgeText = 0xFF18713D;
                        }
                    } catch (java.text.ParseException ignored) {
                        freshness = "Check expiry date";
                    }
                }

                TextView freshnessBadge =
                    text(freshness, 12, badgeText, true);
                freshnessBadge.setPadding(
                    dp(11), dp(6), dp(11), dp(6));
                freshnessBadge.setBackground(
                    shape(badgeBackground, 18));

                LinearLayout.LayoutParams badgeParams =
                    new LinearLayout.LayoutParams(-2, -2);
                badgeParams.topMargin = dp(9);
                info.addView(freshnessBadge, badgeParams);

                card.addView(info,
                    new LinearLayout.LayoutParams(0, -2, 1));

                TextView menu = text(
                    "⋮", 25, 0xFF82958B, true);
                menu.setGravity(Gravity.CENTER);
                card.addView(menu,
                    new LinearLayout.LayoutParams(
                        dp(44), dp(48)));

                menu.setOnClickListener(v -> {
                    PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                    popup.getMenu().add("Edit");
                    popup.getMenu().add("Delete");

                    popup.setOnMenuItemClickListener(choice -> {
                        if (choice.getTitle().equals("Edit")) {
                            Intent intent = new Intent(
                                MainActivity.this, IngredientActivity.class);
                            intent.putExtra("id", item.id);
                            returningFromIngredient = true;
                            startActivity(intent);
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Delete ingredient?")
                                .setMessage(
                                    "Remove " + item.name +
                                    " from your pantry?")
                                .setNegativeButton(
                                    "Cancel", null)
                                .setPositiveButton(
                                    "Delete", (dialog, which) -> {
                                        db.deleteIngredient(item.id);
                                        show("Pantry");
                                    })
                                .show();
                        }
                        return true;
                    });

                    popup.show();
                });

                card.setOnClickListener(v -> {
                    Intent intent = new Intent(
                        MainActivity.this, IngredientActivity.class);
                    intent.putExtra("id", item.id);
                    returningFromIngredient = true;
                            startActivity(intent);
                });


            wrapper.addView(card);
            return wrapper;
        }
    }

    int photo(String name) {
        return getResources().getIdentifier(
            name,"drawable",getPackageName()
        );
    }


    int ingredientPhoto(String name) {
        String n = name.toLowerCase(Locale.ROOT).trim();

        if (n.contains("tomato")) return R.drawable.ingredient_tomato;
        if (n.contains("onion")) return R.drawable.ingredient_onion;
        if (n.contains("egg")) return R.drawable.ingredient_egg;
        if (n.contains("butter")) return R.drawable.ingredient_butter;
        if (n.contains("bread") || n.contains("toast"))
            return R.drawable.ingredient_bread;
        if (n.contains("garlic")) return R.drawable.ingredient_garlic;
        if (n.contains("pasta")) return R.drawable.ingredient_pasta;
        if (n.contains("chicken")) return R.drawable.ingredient_chicken;
        if (n.contains("rice")) return R.drawable.ingredient_rice;
        if (n.contains("carrot")) return R.drawable.ingredient_carrot;
        if (n.contains("potato")) return R.drawable.ingredient_potato;
        if (n.contains("milk")) return R.drawable.ingredient_milk;
        if (n.contains("oil")) return R.drawable.ingredient_oil;
        if (n.contains("cheese")) return R.drawable.ingredient_cheese;
        if (n.contains("water")) return R.drawable.ingredient_water;

        return 0;
    }

    int recipePhoto(String name) {
        return RecipeImages.get(name);
    }


    void recipes() {
        ArrayList<Long> matches = new ArrayList<>(
            MatchingUtils.suggestedRecipes(db)
        );

        if (matches.isEmpty()) {
            LinearLayout panel = column();
            panel.setGravity(Gravity.CENTER);
            panel.setPadding(
                dp(20), dp(30), dp(20), dp(25)
            );
            panel.setBackground(shape(0xFFEAF4EC, 20));

            TextView illustration = text(
                "♨", 43, 0xFF23744B, true
            );
            illustration.setGravity(Gravity.CENTER);
            illustration.setBackground(
                shape(0xFFFFFFFF, 20)
            );
            panel.addView(
                illustration,
                new LinearLayout.LayoutParams(
                    dp(76), dp(76)
                )
            );

            TextView title = text(
                "No recipes ready yet",
                21, 0xFF173F35, true
            );
            title.setGravity(Gravity.CENTER);
            title.setPadding(
                0, dp(18), 0, dp(10)
            );
            panel.addView(title);

            TextView description = text(
                "Add ingredients to discover meals " +
                "you can make with what you have.",
                14, 0xFF52665C, false
            );
            description.setGravity(Gravity.CENTER);
            description.setPadding(
                0, 0, 0, dp(20)
            );
            panel.addView(description);

            TextView tip = text(
                "Only recipes with all required " +
                "ingredients and sufficient " +
                "quantities appear here.",
                13, 0xFF23744B, false
            );
            tip.setPadding(
                dp(14), dp(14),
                dp(14), dp(14)
            );
            tip.setBackground(
                shape(0xFFFFFFFF, 12)
            );
            panel.addView(
                tip,
                new LinearLayout.LayoutParams(-1, -2)
            );

            TextView action = text(
                "+  Add ingredient",
                16, 0xFFFFFFFF, true
            );
            action.setGravity(Gravity.CENTER);
            action.setPadding(
                dp(15), dp(16),
                dp(15), dp(16)
            );
            action.setBackground(
                shape(0xFF23744B, 12)
            );

            LinearLayout.LayoutParams actionParams =
                new LinearLayout.LayoutParams(-1, -2);
            actionParams.topMargin = dp(20);

            panel.addView(action, actionParams);
            action.setOnClickListener(
                v -> addIngredient()
            );

            body.addView(panel);
            return;
        }

        LinearLayout banner = column();
        banner.setPadding(
            dp(19), dp(19),
            dp(19), dp(19)
        );
        banner.setBackground(
            shape(0xFFE9F6EE, 19)
        );

        banner.addView(text(
            matches.size() + (matches.size() == 1 ? " recipe ready to cook" : " recipes ready to cook"),
            18, 0xFF173F35, true
        ));

        TextView subtitle = text(
            "Every required ingredient is available " +
            "in sufficient quantity.",
            13, 0xFF52665C, false
        );
        subtitle.setPadding(
            0, dp(7), 0, 0
        );
        banner.addView(subtitle);

        LinearLayout.LayoutParams bannerParams =
            new LinearLayout.LayoutParams(-1, -2);
        bannerParams.bottomMargin = dp(20);
        body.addView(banner, bannerParams);

        try (Cursor c = db.getRecipes()) {
            while (c.moveToNext()) {
                long id = c.getLong(
                    c.getColumnIndexOrThrow("id")
                );

                if (!matches.contains(id)) {
                    continue;
                }

                recipeCard(
                    id,
                    c.getString(
                        c.getColumnIndexOrThrow("name")
                    ),
                    c.getInt(
                        c.getColumnIndexOrThrow("minutes")
                    ),
                    c.getInt(
                        c.getColumnIndexOrThrow("servings")
                    )
                );
            }
        }
    }



    void recipeCard(
        long id, String name,
        int minutes, int servings
    ) {
        LinearLayout card = column();
        card.setBackground(
            shape(0xFFFFFFFF, 20)
        );
        card.setClipToOutline(true);
        card.setElevation(dp(2));

        int imageId = recipePhoto(name);

        if (imageId != 0) {
            android.widget.FrameLayout photoArea =
                new android.widget.FrameLayout(this);

            ImageView image = new ImageView(this);
            image.setImageResource(imageId);
            image.setScaleType(
                ImageView.ScaleType.CENTER_CROP
            );
            image.setContentDescription(
                "Photograph representing " + name
            );

            photoArea.addView(
                image,
                new android.widget.FrameLayout.LayoutParams(
                    -1, -1
                )
            );

            TextView badge = text(
                "✓  READY TO COOK",
                11,
                0xFF007B53,
                true
            );
            badge.setPadding(
                dp(12), dp(8), dp(12), dp(8)
            );
            badge.setBackground(
                shape(0xFFE7F5E9, 20)
            );

            android.widget.FrameLayout.LayoutParams
                badgeParams =
                new android.widget.FrameLayout.LayoutParams(
                    -2, -2,
                    Gravity.BOTTOM | Gravity.LEFT
                );

            badgeParams.leftMargin = dp(15);
            badgeParams.bottomMargin = dp(14);

            photoArea.addView(
                badge, badgeParams
            );

            card.addView(
                photoArea,
                new LinearLayout.LayoutParams(
                    -1, dp(200)
                )
            );
        }

        LinearLayout info = column();
        info.setPadding(
            dp(19), dp(18), dp(19), dp(20)
        );

        info.addView(
            text(
                name,
                22,
                0xFF103A35,
                true
            )
        );

        TextView details = text(
            "◷  " + minutes + " min     "
                + "♧  " + servings + " servings",
            13,
            0xFF52665C,
            false
        );
        details.setPadding(
            0, dp(10), 0, dp(14)
        );
        info.addView(details);

        TextView explanation = text(
            "All required ingredients are "
                + "available in your pantry.",
            13,
            0xFF52665C,
            false
        );
        explanation.setPadding(
            0, 0, 0, dp(17)
        );
        info.addView(explanation);

        TextView action = text(
            "View recipe   →",
            14,
            0xFF007B53,
            true
        );
        action.setPadding(
            dp(17), dp(12),
            dp(17), dp(12)
        );
        action.setBackground(
            shape(0xFFE7F5E9, 22)
        );

        LinearLayout actionRow =
            new LinearLayout(this);
        actionRow.addView(action);
        info.addView(actionRow);

        card.addView(info);

        LinearLayout.LayoutParams cardParams =
            new LinearLayout.LayoutParams(
                -1, -2
            );
        cardParams.bottomMargin = dp(19);

        body.addView(
            card, cardParams
        );

        card.setOnClickListener(v -> {
            Intent intent = new Intent(
                this,
                RecipeDetailActivity.class
            );
            intent.putExtra("id", id);
            startActivity(intent);
        });
    }


    void expiry() {
        heading("Expiry reminders");
        if (!expiryRemindersEnabled()) {
            card("Reminders are switched off",
                "Enable expiry reminders in Settings to see alerts.",
                () -> show("Settings"));
            button("Back to home", () -> show("Home"));
            return;
        }

        int found = 0;
        try (Cursor c = db.getPantry()) {
            while (c.moveToNext()) {
                String date = c.getString(
                    c.getColumnIndexOrThrow("expiry"));
                if (!withinReminderWindow(date)) continue;
                found++;
                long id = c.getLong(c.getColumnIndexOrThrow("id"));
                String name = c.getString(
                    c.getColumnIndexOrThrow("name"));
                card(emoji(name) + "  " + name,
                    "Expiry: " + date, () -> {
                        Intent intent = new Intent(
                            this, IngredientActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    });
            }
        }
        if (found == 0) {
            card("No expiry reminders",
                "No ingredients expire within the selected "
                    + reminderDays() + "-day reminder window.", null);
        }
        button("Back to home", () -> show("Home"));
    }

    void settings() {
        android.content.SharedPreferences prefs =
            getSharedPreferences("smart_pantry", MODE_PRIVATE);

        heading("Preferences");

        LinearLayout panel = column();
        panel.setBackground(shape(0xFFFFFFFF, 20));
        panel.setPadding(
            dp(17), dp(19), dp(17), dp(19)
        );

        LinearLayout alertRow = new LinearLayout(this);
        alertRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView bell = text(
            "♧", 24, 0xFF23744B, true
        );
        bell.setGravity(Gravity.CENTER);
        bell.setBackground(shape(0xFFE9F6EE, 13));

        alertRow.addView(
            bell,
            new LinearLayout.LayoutParams(dp(52), dp(52))
        );

        LinearLayout alertInfo = column();
        alertInfo.setPadding(dp(14), 0, dp(7), 0);

        alertInfo.addView(text(
            "Expiry reminders",
            16, 0xFF173F35, true
        ));

        TextView alertDescription = text(
            "Show reminders for ingredients expiring soon",
            12, 0xFF52665C, false
        );
        alertDescription.setPadding(0, dp(5), 0, 0);
        alertInfo.addView(alertDescription);

        alertRow.addView(
            alertInfo,
            new LinearLayout.LayoutParams(0, -2, 1)
        );

        Switch alerts = new Switch(this);
        alerts.setChecked(
            prefs.getBoolean("expiry_alerts", true)
        );

        alerts.setOnCheckedChangeListener(
            (button, checked) -> {
                prefs.edit()
                    .putBoolean("expiry_alerts", checked)
                    .apply();
            }
        );

        alertRow.addView(alerts);
        panel.addView(alertRow);

        View divider = new View(this);
        divider.setBackgroundColor(0xFFE5EBE6);

        LinearLayout.LayoutParams dividerParams =
            new LinearLayout.LayoutParams(-1, dp(1));
        dividerParams.setMargins(
            0, dp(20), 0, dp(20)
        );
        panel.addView(divider, dividerParams);

        LinearLayout reminderRow = new LinearLayout(this);
        reminderRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView calendar = text(
            "◷", 26, 0xFF23744B, true
        );
        calendar.setGravity(Gravity.CENTER);
        calendar.setBackground(shape(0xFFE9F6EE, 13));

        reminderRow.addView(
            calendar,
            new LinearLayout.LayoutParams(dp(52), dp(52))
        );

        LinearLayout reminderInfo = column();
        reminderInfo.setPadding(dp(14), 0, dp(7), 0);

        reminderInfo.addView(text(
            "Reminder window",
            16, 0xFF173F35, true
        ));

        TextView reminderDescription = text(
            "When food is considered expiring soon",
            12, 0xFF52665C, false
        );
        reminderDescription.setPadding(0, dp(5), 0, 0);
        reminderInfo.addView(reminderDescription);

        reminderRow.addView(
            reminderInfo,
            new LinearLayout.LayoutParams(0, -2, 1)
        );

        Spinner window = new Spinner(this);
        window.setBackground(
            shape(0xFFE9F6EE, 12)
        );
        window.setPadding(
            dp(8), dp(5), dp(5), dp(5)
        );

        String[] options = {
            "1 day", "3 days", "7 days"
        };

        android.widget.ArrayAdapter<String> windowAdapter =
            new android.widget.ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
            );

        windowAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );

        window.setAdapter(windowAdapter);

        int savedDays = prefs.getInt("expiry_days", 3);
        window.setSelection(
            savedDays == 1 ? 0 : savedDays == 7 ? 2 : 1
        );

        window.setOnItemSelectedListener(
            new android.widget.AdapterView.OnItemSelectedListener() {
                public void onNothingSelected(
                    android.widget.AdapterView<?> parent
                ) {}

                public void onItemSelected(
                    android.widget.AdapterView<?> parent,
                    View view,
                    int position,
                    long id
                ) {
                    int days =
                        position == 0 ? 1 :
                        position == 2 ? 7 : 3;

                    prefs.edit()
                        .putInt("expiry_days", days)
                        .apply();
                }
            }
        );

        reminderRow.addView(window);
        panel.addView(reminderRow);
        body.addView(panel);

        heading("Your data");

        LinearLayout dataPanel = column();
        dataPanel.setBackground(shape(0xFFFFFFFF, 20));
        dataPanel.setPadding(
            dp(18), dp(18), dp(18), dp(18)
        );

        dataPanel.addView(text(
            "▤   Local storage",
            17, 0xFF173F35, true
        ));

        TextView storage = text(
            "Your ingredients are stored on this device using SQLite.",
            13, 0xFF52665C, false
        );
        storage.setPadding(
            dp(28), dp(7), 0, dp(17)
        );
        dataPanel.addView(storage);

        View dataDivider = new View(this);
        dataDivider.setBackgroundColor(0xFFE5EBE6);
        dataPanel.addView(
            dataDivider,
            new LinearLayout.LayoutParams(-1, dp(1))
        );

        TextView recipesTitle = text(
            "▤   Recipe collection",
            17, 0xFF173F35, true
        );
        recipesTitle.setPadding(0, dp(17), 0, 0);
        dataPanel.addView(recipesTitle);

        TextView recipesDescription = text(
            "16 recipes stored locally",
            13, 0xFF52665C, false
        );
        recipesDescription.setPadding(
            dp(28), dp(7), 0, 0
        );
        dataPanel.addView(recipesDescription);

        body.addView(dataPanel);


        heading("About");

        LinearLayout aboutCard = column();
        aboutCard.setBackground(
            shape(0xFFFFFFFF, 20)
        );
        aboutCard.setPadding(
            dp(19), dp(18),
            dp(19), dp(18)
        );

        aboutCard.addView(
            text(
                "Smart Pantry Manager",
                16,
                0xFF103A35,
                true
            )
        );

        TextView version = text(
            "Version 1.0",
            12,
            0xFF64766F,
            false
        );
        version.setPadding(
            0, dp(7), 0, 0
        );
        aboutCard.addView(version);

        body.addView(aboutCard);
    }

    void empty(String title,String message,
               String action,Runnable callback) {
        LinearLayout panel=column();
        panel.setGravity(Gravity.CENTER);
        panel.setPadding(
            dp(20),dp(65),dp(20),dp(25)
        );

        TextView illustration=text(
            "♨",57,MUTED,false
        );
        illustration.setGravity(Gravity.CENTER);
        panel.addView(illustration);

        TextView heading=text(
            title,18,NAVY,true
        );
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0,dp(18),0,dp(10));
        panel.addView(heading);

        TextView description=text(
            message,14,MUTED,false
        );
        description.setGravity(Gravity.CENTER);
        panel.addView(description);

        body.addView(panel);
        button(action,callback);
    }
}
