package com.alphabyte.mathsformulabook.activity;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alphabyte.mathsformulabook.R;
import com.alphabyte.mathsformulabook.fragment.FavouriteFragment;
import com.alphabyte.mathsformulabook.fragment.HomeFragment;
import com.alphabyte.mathsformulabook.fragment.SettingsFragment;
import com.alphabyte.mathsformulabook.helper.PreferenceHelper;
import com.alphabyte.mathsformulabook.models.Favourite;
import com.alphabyte.mathsformulabook.models.TopicList;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    private Menu menu;
    int filter = 1;
    boolean darkTheme;
    HomeFragment homeFragment;
    SettingsFragment settingsFragment;
    FavouriteFragment favouriteFragment;

    //TODO : FIX SHORTCUT FUNCTION


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        darkTheme = preferenceHelper.isDarkTheme();
        if(darkTheme){
            setTheme(R.style.AppTheme_DarkTheme);
        }


        favouriteFragment = new FavouriteFragment();
        settingsFragment = new SettingsFragment();
        homeFragment = new HomeFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        toolbar.setTitle("Topics");
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.navigation_home);


        ColorStateList colorStateList = ContextCompat.getColorStateList(this,R.color.nav_item_foreground_dark);

        if(darkTheme){
            bottomNav.setItemTextColor(colorStateList);
            bottomNav.setItemIconTintList(colorStateList);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homeFragment).commit();
        }

        //updateShortcut();


    }

    @Override
    protected void onPause() {
        //updateShortcut();
        super.onPause();
    }

    void updateShortcut() {
        PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        String favourites = preferenceHelper.getFavouriteList();
        Favourite fav = gson.fromJson(favourites, Favourite.class);
        List<TopicList.TopicDetails> topicList;
        if (fav != null) {
            topicList = fav.getTopicList();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                if (shortcutManager.getDynamicShortcuts().size() != 0) {
                    shortcutManager.removeAllDynamicShortcuts();
                }
                List<ShortcutInfo> shortcutInfoList = new ArrayList<>();
                int count = 0;
                for (int i = topicList.size(); count < 3; i--) {
                    TopicList.TopicDetails t = topicList.get(i);
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra("topic_file_name", t.getTopic_file_name());
                    intent.putExtra("topic_selected", t.getTopic_name());
                    intent.setAction(Intent.ACTION_VIEW);
                    ShortcutInfo shortcut = new ShortcutInfo.Builder(this, t.getTopic_name())
                            .setShortLabel(t.getTopic_name())
                            .setIntent(intent)
                            .build();
                    shortcutInfoList.add(shortcut);
                    count++;
                }
                if (shortcutInfoList.size() != 0) {
                    shortcutManager.setDynamicShortcuts(shortcutInfoList);

                }
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    selectedFragment = homeFragment;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            filter = 1;
                            getSupportActionBar().setTitle("Topics");
                            selectedFragment = homeFragment;
                            break;
                        case R.id.navigation_favourite:
                            filter = 0;
                            getSupportActionBar().setTitle("Favourites");
                            selectedFragment = favouriteFragment;                           break;
                        case R.id.navigation_settings:
                            filter = 0;
                            getSupportActionBar().setTitle("Settings");
                            selectedFragment = settingsFragment;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    /*
                    if (menu != null) {
                        updateMenu();
                    }*/

                    return true;
                }
            };
}
