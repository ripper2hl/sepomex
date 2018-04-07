package com.perales.sepomex.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "inegi_clave_municipio")
public class InegiClaveMunicipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "inegiClaveMunicipio")
    private List<Colonia> colonias;
}
