package com.example.pm2examengrupo5;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListarContactosActivity extends AppCompatActivity {
    ListView listaContactos;
    SearchView searchView;
    Button btnAtras, btnEliminar, btnActualizar;
    ArrayList<String> listaInformacion;
    ArrayList<Personas> listaPersonas;
    ArrayAdapter<String> adapter;

    // Para recordar el contacto seleccionado
    int posicionSeleccionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        listaContactos = findViewById(R.id.listaContactos);
        searchView = findViewById(R.id.searchView);
        btnAtras = findViewById(R.id.btnatras2);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);

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
            return true; // importante, consume el evento para no lanzar onItemClick simultaneamente
        });

        btnEliminar.setOnClickListener(v -> {
            if (posicionSeleccionada != -1) {
                Personas persona = listaPersonas.get(posicionSeleccionada);
                eliminarContacto(persona.getId());
            } else {
                Toast.makeText(this, "Seleccione un contacto para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        btnActualizar.setOnClickListener(v -> {
            if (posicionSeleccionada != -1) {
                Personas persona = listaPersonas.get(posicionSeleccionada);
                // Abre la actividad para editar, pasando datos por extras
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
        listaPersonas = new ArrayList<>();
        listaInformacion = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.34/Examen2P-php/GetPersons.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Personas persona = new Personas();
                            persona.setId(obj.getInt("id"));
                            persona.setNombres(obj.getString("nombres"));
                            persona.setLatitud(obj.getDouble("latitud"));
                            persona.setLongitud(obj.getDouble("longitud"));
                            listaPersonas.add(persona);
                        }
                        llenarLista();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al parsear JSON", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
        });

        queue.add(request);
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

        posicionSeleccionada = -1;  // Reiniciamos selección
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

    private void eliminarContacto(int id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.34/Examen2P-php/DeletePerson.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                    obtenerPersonasDeAPI();
                },
                error -> {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        queue.add(request);
    }

}
