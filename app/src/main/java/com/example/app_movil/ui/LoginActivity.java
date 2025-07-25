package com.example.app_movil.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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

    // Declarar las variables
    EditText etUsername, etPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Y aquí se enlazan
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUser());
    }
    private void loginUser() {
        String url = "http://192.168.21.90:8080/api/login-app";
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

                            // si da error al pasar el dato
                            JSONObject user = response.getJSONObject("user");
                            String nombreUsuario = user.getString("nombre_completo"); // o "usuario"

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                          //  intent.putExtra("id_usuario", idUsuario )
                            intent.putExtra("nombre_completo", nombreUsuario); // PASAS EL NOMBRE!


                            // Al recibir la respuesta
                          //  JSONObject user = response.getJSONObject("user");
                            String idUsuario = user.getString("nombre_completo");
                            SharedPreferences prefs = getSharedPreferences("TUS_PREFS", MODE_PRIVATE);
                            prefs.edit().putString("nombre_completo", idUsuario).apply();

                            startActivity(intent);
                            finish();
                        } else {

                            String msg = response.has("message") ? response.getString("message") : "Credenciales incorrectas";
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error ->  {

                    String errorMsg = "Error de conexión";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        // Intenta leer el mensaje devuelto por el backend
                        try {
                            String body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject obj = new JSONObject(body);
                            if (obj.has("message")) {
                                errorMsg = obj.getString("message");
                            }
                        } catch (Exception e) {
                            // Si no es JSON, ignora
                        }
                    }
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                }
        );
        queue.add(request);
    }
}
