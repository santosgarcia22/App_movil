package com.example.app_movil.ui.gallery;

//para la camara
import android.content.pm.PackageManager;

import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.util.Base64;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_movil.R;
import com.example.app_movil.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap currentBitmap;
    private ImageView imageView;
    private Button btnScan, btnGuardar;
    private EditText etNombre;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    String fechaActual = sdf.format(new Date());

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
               // new ViewModelProvider(this).get(GalleryViewModel.class);

        //Pide el permiso si no esta concedido
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 101);
        }

        //Enlace a las vistas del layout
        imageView = view.findViewById(R.id.imageView);
        btnScan = view.findViewById(R.id.btnScan);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        etNombre = view.findViewById(R.id.etNombre);

        //Evento para capturar la foto

        btnScan.setOnClickListener(v -> {
            try {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(getContext(), "No hay app de cámara", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener el nombre de tu TextView o EditText (Segun el layout)
                String nombre = etNombre.getText().toString();

                //llama el metodo para enviar a la api

                if (nombre.isEmpty()){
                    etNombre.setError("Ingrese el nombre");
                }
                //    if (currentBitmap == null) {
//                    Toast.makeText(getContext(), "Capture una foto del carnet", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                enviarNombre(nombre);
            }
        });

       // binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return view;
//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
    }


    private void enviarNombre(String nombre){
        String url = "https://9f10-201-150-85-19.ngrok-free.app/api/accesos";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();
        try {
            postData.put("nombre", nombre);
            postData.put("tipo", 2);
            postData.put("posicion", "cabina App");
            postData.put("ingreso", fechaActual);
            postData.put("salida", "2025-06-12 12:00:00");
            postData.put("Sicronizacion", fechaActual);
            postData.put("id", "test123");
            postData.put("vuelo", 1);

            // Aquí agregamos la imagen solo si hay foto tomada
            if (currentBitmap != null) {
                String imagenBase64 = bitmapToBase64(currentBitmap);
                postData.put("imagen_base64", imagenBase64);
            }
            // No envíes el campo 'objetos', Laravel lo gestiona
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                response -> Toast.makeText(getContext(), "¡Registro enviado!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(getContext(), "Error al enviar: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        queue.add(jsonObjectRequest);
    }

    //Utilidad para convertir Bitmap a Base64

    private String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP); // NO_WRAP para evitar saltos de línea
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            currentBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(currentBitmap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}