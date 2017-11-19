package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String inegiClave;

    @OneToMany
    private List<Ciudad> ciudades;

    @OneToMany
    private List<Municipio> municipios;

    @OneToMany
    private List<Colonia> colonias;

    public Estado() {
        super();
    }
}
