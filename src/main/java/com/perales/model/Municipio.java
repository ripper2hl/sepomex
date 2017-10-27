package com.perales.model;

import java.util.Objects;

public class Municipio {
    private Integer id;
    private String nombre;
    private String inegiClave;
    private String identificadorMunicipal;

    public Municipio() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInegiClave() {
        return inegiClave;
    }

    public void setInegiClave(String inegiClave) {
        this.inegiClave = inegiClave;
    }

    public String getIdentificadorMunicipal() {
        return identificadorMunicipal;
    }

    public void setIdentificadorMunicipal(String identificadorMunicipal) {
        this.identificadorMunicipal = identificadorMunicipal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Municipio municipio = (Municipio) o;
        return Objects.equals(id, municipio.id) &&
                Objects.equals(nombre, municipio.nombre) &&
                Objects.equals(inegiClave, municipio.inegiClave) &&
                Objects.equals(identificadorMunicipal, municipio.identificadorMunicipal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, inegiClave, identificadorMunicipal);
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", inegiClave='" + inegiClave + '\'' +
                ", identificadorMunicipal='" + identificadorMunicipal + '\'' +
                '}';
    }
}