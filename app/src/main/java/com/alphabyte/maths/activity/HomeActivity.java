package com.alphabyte.maths.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alphabyte.maths.R;
import com.alphabyte.maths.fragment.FavouriteFragment;
import com.alphabyte.maths.fragment.HomeFragment;
import com.alphabyte.maths.fragment.RequestFragment;
import com.alphabyte.maths.helper.PreferenceHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    private Menu menu;
    int filter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        if(preferenceHelper.isDarkTheme()){
            setTheme(R.style.AppTheme_DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        toolbar.setTitle("Topics");
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.navigation_home);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            filter = 1;
                            getSupportActionBar().setTitle("Topics");
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_favourite:
                            filter = 0;
                            getSupportActionBar().setTitle("Favourites");
                            selectedFragment = new FavouriteFragment();                            break;
                        case R.id.navigation_request:
                            filter = 0;
                            getSupportActionBar().setTitle("Request For New Topics");
                            selectedFragment = new RequestFragment();
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
