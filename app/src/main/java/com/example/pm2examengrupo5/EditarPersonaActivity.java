package com.example.pm2examengrupo5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class EditarPersonaActivity extends AppCompatActivity {

    TextView tvId;
    EditText etNombres, etLatitud, etLongitud;
    Button btnGuardar, btnCancelar;

    int id;
    String urlActualizar = "http://192.168.1.34/Examen2P-php/UpdatePerson.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_persona);

        tvId = findViewById(R.id.tvId);
        etNombres = findViewById(R.id.etNombres);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnGuardar = findViewById(R.id.btnGuardarCambios);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Recuperar datos enviados desde ListarContactosActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            String nombres = extras.getString("nombres");
            double latitud = extras.getDouble("latitud");
            double longitud = extras.getDouble("longitud");

            tvId.setText("ID: " + id);
            etNombres.setText(nombres);
            etLatitud.setText(String.valueOf(latitud));
            etLongitud.setText(String.valueOf(longitud));
        }

        btnCancelar.setOnClickListener(v -> finish());

        btnGuardar.setOnClickListener(v -> {
            String nuevosNombres = etNombres.getText().toString().trim();
            String nuevaLatitud = etLatitud.getText().toString().trim();
            String nuevaLongitud = etLongitud.getText().toString().trim();

            if (nuevosNombres.isEmpty() || nuevaLatitud.isEmpty() || nuevaLongitud.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            actualizarPersona(id, nuevosNombres, nuevaLatitud, nuevaLongitud);
        });
    }

    private void actualizarPersona(int id, String nombres, String latitud, String longitud) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.34/Examen2P-php/UpdatePerson.php";

        String jsonBody = "{"
                + "\"id\":" + id + ","
                + "\"nombres\":\"" + nombres + "\","
                + "\"latitud\":\"" + latitud + "\","
                + "\"longitud\":\"" + longitud + "\""
                + "}";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show();
                    // cerrar actividad o refrescar pantalla
                },
                error -> {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                return jsonBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        queue.add(request);
    }

}
