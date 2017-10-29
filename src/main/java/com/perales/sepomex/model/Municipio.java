package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String inegiClave;
    private String identificadorMunicipal;
    @OneToMany
    private List<Colonia> colonias;

    public Municipio() {
        super();
    }
}