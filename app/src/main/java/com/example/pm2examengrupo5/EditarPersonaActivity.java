package com.example.pm2examengrupo5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;

public class EditarPersonaActivity extends AppCompatActivity {

    TextView tvId;
    EditText etNombres, etTelefono, etLatitud, etLongitud;
    Button btnGuardar, btnCancelar;

    int id;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_persona);

        tvId = findViewById(R.id.tvId);
        etNombres = findViewById(R.id.etNombres);
        etTelefono = findViewById(R.id.etTelefono);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnGuardar = findViewById(R.id.btnGuardarCambios);
        btnCancelar = findViewById(R.id.btnCancelar);

        api = RetrofitClient.getClient().create(ApiService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            String nombres = extras.getString("nombres");
            String telefono = extras.getString("telefono");
            double latitud = extras.getDouble("latitud");
            double longitud = extras.getDouble("longitud");

            tvId.setText("ID: " + id);
            etNombres.setText(nombres);
            etTelefono.setText(telefono != null ? telefono : "");
            etLatitud.setText(String.valueOf(latitud));
            etLongitud.setText(String.valueOf(longitud));
        }

        btnCancelar.setOnClickListener(v -> finish());

        btnGuardar.setOnClickListener(v -> {
            String nuevosNombres = etNombres.getText().toString().trim();
            String nuevoTelefono = etTelefono.getText().toString().trim();
            String nuevaLatitud = etLatitud.getText().toString().trim();
            String nuevaLongitud = etLongitud.getText().toString().trim();

            if (nuevosNombres.isEmpty() || nuevoTelefono.isEmpty()
                    || nuevaLatitud.isEmpty() || nuevaLongitud.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Personas persona = new Personas();
            persona.setId(id);
            persona.setNombres(nuevosNombres);
            persona.setTelefono(nuevoTelefono);
            persona.setLatitud(Double.parseDouble(nuevaLatitud));
            persona.setLongitud(Double.parseDouble(nuevaLongitud));

            Call<Void> call = api.updatePerson(persona);
            call.enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(EditarPersonaActivity.this, "Contacto actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditarPersonaActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EditarPersonaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
