package com.bangkit.tagme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.bangkit.tagme.activity.LoginActivity;
import com.bangkit.tagme.activity.QRscannerActivity;
import com.bangkit.tagme.fragment.AboutUsFragment;
import com.bangkit.tagme.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private long backPressedTime;
    private Toast backToast;

    public static ArrayList<Fav> courseModalArrayList;
    static SharedPreferences sharedPreferences;
    public static NotesAdapter adapter ;


    // Bottom Navigation
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = menuItem -> {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                return true;
            case R.id.about_us:
                fragment = new AboutUsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                return true;

        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Untuk Toolbar menu diatas
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.naView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationbar);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }


        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("courses", null);

        Type type = new TypeToken<ArrayList<Fav>>() {}.getType();


        courseModalArrayList = gson.fromJson(json, type);


        if (courseModalArrayList == null) {
            courseModalArrayList = new ArrayList<>();
        }

        adapter = new NotesAdapter(courseModalArrayList, MainActivity.this);

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    // onBackPressed for exit
    @Override
    public void onBackPressed() {
        if (backPressedTime + 1000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.qrScan) {
            startActivity(new Intent(getApplicationContext(), QRscannerActivity.class));
        }
        if (item.getItemId() == R.id.addQRGenerator) {
            startActivity(new Intent(getApplicationContext(), QRGeneratorActivity.class));
        }
        if (item.getItemId() == R.id.qrHistory) {
            startActivity(new Intent(getApplicationContext(), ResultShow.class));
        }  //ini class history dari scan QR

        return super.onOptionsItemSelected(item);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment()).commit();
                break;

            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START );
        return true;
    }

}