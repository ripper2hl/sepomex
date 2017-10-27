package com.perales.model;

import java.util.Objects;

public class ZonaTipo {

    private Integer id;
    private String nombre;

    public ZonaTipo() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZonaTipo zonaTipo = (ZonaTipo) o;
        return Objects.equals(id, zonaTipo.id) &&
                Objects.equals(nombre, zonaTipo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @Override
    public String toString() {
        return "ZonaTipo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
