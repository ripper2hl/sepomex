package com.perales.sepomex.model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "asentamiento_tipo")
public class AsentamientoTipo {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "sepomex_clave")
    private String sepomexClave;

    @OneToMany(mappedBy = "asentamientoTipo")
    private List<Colonia> colonias;

    public AsentamientoTipo() {
        super();
    }
}
