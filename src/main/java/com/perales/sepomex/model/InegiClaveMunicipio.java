package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "inegi_clave_municipio")
public class InegiClaveMunicipio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "inegiClaveMunicipio")
    private List<Colonia> colonias;
}
