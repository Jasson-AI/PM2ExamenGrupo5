package com.example.pm2examengrupo5;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarContactosActivity extends AppCompatActivity {
    ListView listaContactos;
    ArrayList<String> listaInformacion;
    ArrayList<Personas> listaPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        listaContactos = findViewById(R.id.listaContactos);
        Button btnatras2 = findViewById(R.id.btnatras2);
        btnatras2.setOnClickListener(view -> finish());

        obtenerPersonasDeAPI();
    }

    private void obtenerPersonasDeAPI() {
        listaPersonas = new ArrayList<>();
        listaInformacion = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.34/Examen2P-php/GetPersons.php"; // Cambia URL según sea necesario

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

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1, listaInformacion);
                        listaContactos.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error de parseo JSON", Toast.LENGTH_SHORT).show();
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
    }
}