package com.example.app_movil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.example.app_movil.MainActivity;
import com.example.app_movil.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    // Declara aquí las variables
    EditText etUsername, etPassword;
    Button btnLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Y aquí las enlazas
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String url = "https://9f10-201-150-85-19.ngrok-free.app/api/login-app";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("usuario", etUsername.getText().toString().trim());
            postData.put("password", etPassword.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    Log.d("LOGIN_RESPONSE", response.toString()); // <-- AGREGA ESTO
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONObject user = response.getJSONObject("user");
                            String nombreUsuario = user.getString("nombre_completo"); // usa el campo correcto

                            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("nombre_usuario", nombreUsuario);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMsg = "";
                    if (error.networkResponse != null) {
                        errorMsg = "Status code: " + error.networkResponse.statusCode;
                        if (error.networkResponse.data != null) {
                            errorMsg += ", body: " + new String(error.networkResponse.data);
                        }
                    } else {
                        errorMsg = error.toString();
                    }
                    Log.e("LOGIN_ERROR", errorMsg); // <-- AGREGA ESTO
                    Toast.makeText(this, "Error en la conexión: " + errorMsg, Toast.LENGTH_LONG).show();
                }
        );
        queue.add(request);
    }




}
