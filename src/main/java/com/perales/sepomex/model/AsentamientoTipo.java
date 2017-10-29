package com.perales.sepomex.model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class AsentamientoTipo {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String sepomexClave;

    @OneToMany
    private List<Colonia> colonias;

    public AsentamientoTipo() {
        super();
    }
}
