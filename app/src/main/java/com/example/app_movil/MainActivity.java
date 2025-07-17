package com.example.app_movil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_movil.ui.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_movil.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       String idUsuario = getIntent().getStringExtra("id_usuario");
       String nombreUsuario = getIntent().getStringExtra("nombre_completo");

        //Guardar en SharedPreferences

        SharedPreferences prefs = getSharedPreferences("TUS_PREFS", MODE_PRIVATE );
        prefs.edit().putString("id_usuario", idUsuario )
                .putString("nombre_completo", nombreUsuario )
                .apply();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


       // NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        TextView textViewName  = headerView.findViewById(R.id.textViewName);

        //String nombreUsuario = getIntent().getStringExtra("nombre_completo");
        Toast.makeText(this, "Usuario recibido: " + nombreUsuario, Toast.LENGTH_LONG).show();
        if (nombreUsuario != null)
        {
           textViewName.setText(nombreUsuario);
        }

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show();
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

       // NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "Cerrando sesión..." + item.getItemId(), Toast.LENGTH_SHORT).show();
                // Margen de tiempo de 1 segundo (1000 ms)
                new android.os.Handler().postDelayed(() -> {
                    cerrarSesion();
                }, 1000);
                binding.drawerLayout.closeDrawers();
                return true;
            }

            return NavigationUI.onNavDestinationSelected(item, navController);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void cerrarSesion() {
        Toast.makeText(this, "Cerrando sesión......", Toast.LENGTH_SHORT).show();

         SharedPreferences prefs = getSharedPreferences("TU_PREFS", MODE_PRIVATE);
         prefs.edit().clear().apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }




}

