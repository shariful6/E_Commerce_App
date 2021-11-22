package com.shariful.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.navigation);

        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        bottomNavigation.setSelectedItemId(R.id.home_id);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        loadFragment(homeFragment);
                        return true;
                    case R.id.message_id:
                        MessageFragment messageFragment = new MessageFragment();
                        loadFragment(messageFragment);
                        return true;
                    case R.id.cart_id:
                        CartFragment cartFragment = new CartFragment();
                        loadFragment(cartFragment);
                        return true;
                    case R.id.noti_id:
                        // NotificationFragment notificationFragment = new NotificationFragment();
                        // loadFragment(notificationFragment);
                        startActivity(new Intent(getApplicationContext(),AddProductActivity.class));
                        return true;
                    case R.id.profile_id:
                        AccountFragment accountFragment = new AccountFragment();
                        loadFragment(accountFragment);
                        return true;

                }
                return false;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        //replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }
}