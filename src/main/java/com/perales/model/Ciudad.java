package com.perales.model;

import java.util.Objects;

public class Ciudad {
    private Integer id;
    private String nombre;
    private String clave;

    public Ciudad() {
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciudad ciudad = (Ciudad) o;
        return Objects.equals(id, ciudad.id) &&
                Objects.equals(nombre, ciudad.nombre) &&
                Objects.equals(clave, ciudad.clave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, clave);
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", clave='" + clave + '\'' +
                '}';
    }
}