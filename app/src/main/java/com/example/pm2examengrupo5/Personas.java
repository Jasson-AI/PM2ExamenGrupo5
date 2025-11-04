package com.example.pm2examengrupo5;

public class Personas {
    private int id;
    private String nombres;
    private String telefono;
    private Double latitud;
    private Double longitud;
    private String firma;

    public Personas() {}

    public Personas(int id, String nombres, String telefono, Double latitud, Double longitud, String firma) {
        this.id = id;
        this.nombres = nombres;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.firma = firma;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public String getFirma() { return firma; }
    public void setFirma(String firma) { this.firma = firma; }
}
