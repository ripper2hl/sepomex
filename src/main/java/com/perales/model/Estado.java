package com.perales.model;

public class Estado {
    private Integer id;
    private String nombre;
    private String inegiClave;

    public Estado() {
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
}
