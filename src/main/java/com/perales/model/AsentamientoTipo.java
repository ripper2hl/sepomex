package com.perales.model;

import java.util.Objects;

public class AsentamientoTipo {

    private Integer id;
    private String nombre;
    private String sepomexClave;

    public AsentamientoTipo() {
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

    public String getSepomexClave() {
        return sepomexClave;
    }

    public void setSepomexClave(String sepomexClave) {
        this.sepomexClave = sepomexClave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsentamientoTipo that = (AsentamientoTipo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @Override
    public String toString() {
        return "AsentamientoTipo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
