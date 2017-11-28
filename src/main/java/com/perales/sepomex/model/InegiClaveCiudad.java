package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "inegi_clave_ciudad")
public class InegiClaveCiudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "inegiClaveCiudad")
    private List<Colonia> colonias;
}
