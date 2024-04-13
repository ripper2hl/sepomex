package com.perales.sepomex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "archivo")
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime fechaCarga;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    // Constructor vacío requerido por JPA
    public Archivo() {
    }

    // Constructor con todos los atributos
    public Archivo(Integer id, LocalDateTime fechaCarga, String contenido) {
        this.id = id;
        this.fechaCarga = fechaCarga;
        this.contenido = contenido;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    // Método equals generado automáticamente por IntelliJ IDEA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Archivo archivo = (Archivo) o;
        return id == archivo.id && Objects.equals(fechaCarga, archivo.fechaCarga) && Objects.equals(contenido, archivo.contenido);
    }

    // Método hashCode generado automáticamente por IntelliJ IDEA
    @Override
    public int hashCode() {
        return Objects.hash(id, fechaCarga, contenido);
    }

    // Método toString generado automáticamente por IntelliJ IDEA
    @Override
    public String toString() {
        return "Archivo{" +
                "id=" + id +
                ", fechaCarga=" + fechaCarga +
                ", contenido='" + contenido + '\'' +
                '}';
    }
}