package com.example.huffman;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_image)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    public void navigate(int id, Bundle bundle){
        //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(id,bundle);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_navigation_home_to_navigation_image,bundle);
    }

    public void navigateUp(){
        Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    @Override
    public void onBackPressed() {
        //Navigation.findNavController(this,R.id.nav_host_fragment).
        //navigateUp();
        super.onBackPressed();
    }
}