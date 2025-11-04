package com.example.pm2examengrupo5;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class ListarContactosActivity extends AppCompatActivity {
    ListView listaContactos;
    SearchView searchView;
    Button btnAtras, btnEliminar, btnActualizar;
    ArrayList<String> listaInformacion;
    ArrayList<Personas> listaPersonas;
    ArrayAdapter<String> adapter;
    int posicionSeleccionada = -1;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        listaContactos = findViewById(R.id.listaContactos);
        searchView = findViewById(R.id.searchView);
        btnAtras = findViewById(R.id.btnatras2);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);

        api = RetrofitClient.getClient().create(ApiService.class);

        btnAtras.setOnClickListener(v -> finish());

        obtenerPersonasDeAPI();

        listaContactos.setOnItemClickListener((parent, view, position, id) -> {
            Personas personaSeleccionada = listaPersonas.get(position);
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Ir a ubicación")
                    .setMessage("Deseas abrir la ubicación de " + personaSeleccionada.getNombres() + " en el mapa?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        String uri = "geo:" + personaSeleccionada.getLatitud() + "," + personaSeleccionada.getLongitud() + "?q=" + personaSeleccionada.getLatitud() + "," + personaSeleccionada.getLongitud();
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No tienes una app de mapas instalada", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        listaContactos.setOnItemLongClickListener((parent, view, position, id) -> {
            posicionSeleccionada = position;
            Personas personaSeleccionada = listaPersonas.get(position);
            Toast.makeText(this, "Seleccionado para operaciones: " + personaSeleccionada.getNombres(), Toast.LENGTH_SHORT).show();
            return true;
        });

        btnEliminar.setOnClickListener(v -> {
            if (posicionSeleccionada != -1) {
                Personas persona = listaPersonas.get(posicionSeleccionada);
                Map<String, Integer> idMap = new HashMap<>();
                idMap.put("id", persona.getId());

                Call<Void> call = api.deletePerson(idMap);
                call.enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ListarContactosActivity.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                            obtenerPersonasDeAPI();
                        } else {
                            Toast.makeText(ListarContactosActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ListarContactosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Seleccione un contacto para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        btnActualizar.setOnClickListener(v -> {
            if (posicionSeleccionada != -1) {
                Personas persona = listaPersonas.get(posicionSeleccionada);
                Intent intent = new Intent(this, EditarPersonaActivity.class);
                intent.putExtra("id", persona.getId());
                intent.putExtra("nombres", persona.getNombres());
                intent.putExtra("latitud", persona.getLatitud());
                intent.putExtra("longitud", persona.getLongitud());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Seleccione un contacto para actualizar", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarDatos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarDatos(newText);
                return false;
            }
        });
    }

    private void obtenerPersonasDeAPI() {
        Call<List<Personas>> call = api.getPersons();
        call.enqueue(new retrofit2.Callback<List<Personas>>() {
            @Override
            public void onResponse(Call<List<Personas>> call, retrofit2.Response<List<Personas>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listaPersonas = new ArrayList<>(response.body());
                    llenarLista();
                } else {
                    Toast.makeText(ListarContactosActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Personas>> call, Throwable t) {
                Toast.makeText(ListarContactosActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarLista() {
        listaInformacion = new ArrayList<>();
        for (Personas p : listaPersonas) {
            listaInformacion.add(p.getId() + " | " + p.getNombres()
                    + "\nLatitud: " + p.getLatitud() + " | Longitud: " + p.getLongitud());
        }
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaInformacion);
        listaContactos.setAdapter(adapter);

        posicionSeleccionada = -1;
    }

    private void filtrarDatos(String texto) {
        if (texto == null || texto.isEmpty()) {
            llenarLista();
            return;
        }
        ArrayList<String> listaFiltrada = new ArrayList<>();
        for (Personas p : listaPersonas) {
            if (p.getNombres().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(p.getId() + " | " + p.getNombres()
                        + "\nLatitud: " + p.getLatitud() + " | Longitud: " + p.getLongitud());
            }
        }
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listaFiltrada);
        listaContactos.setAdapter(adapter);
    }
}
