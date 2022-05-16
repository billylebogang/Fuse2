package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);

        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);

        container.addView(view);

        super.setContentView(drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.menu_drawer_close, R.string.menu_drawer_open);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.nav_chats:
                startActivity(new Intent(DrawerBase.this, Chats.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_matches:
                startActivity(new Intent(DrawerBase.this, Matches.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_reserve:
                startActivity(new Intent(DrawerBase.this, Reserves.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_notification:
                startActivity(new Intent(DrawerBase.this, Notifications.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_home:
                startActivity(new Intent(DrawerBase.this, Dashboard.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_setting:
                startActivity(new Intent(DrawerBase.this, Settings.class));
                overridePendingTransition(0,0);
                break;
        }

        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}