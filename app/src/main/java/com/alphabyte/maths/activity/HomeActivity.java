package com.alphabyte.maths.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alphabyte.maths.R;
import com.alphabyte.maths.fragment.FavouriteFragment;
import com.alphabyte.maths.fragment.HomeFragment;
import com.alphabyte.maths.fragment.SettingsFragment;
import com.alphabyte.maths.helper.PreferenceHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    private Menu menu;
    int filter = 1;
    boolean darkTheme;
    HomeFragment homeFragment;
    SettingsFragment settingsFragment;
    FavouriteFragment favouriteFragment;

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
