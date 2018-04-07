package com.perales.sepomex.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "inegi_clave")
    private String inegiClave;

    @OneToMany(mappedBy = "estado")
    private List<Ciudad> ciudades;

    @OneToMany(mappedBy = "estado")
    private List<Municipio> municipios;

    @OneToMany(mappedBy = "estado")
    private List<Colonia> colonias;

    public Estado() {
        super();
    }
}
