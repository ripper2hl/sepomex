package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String clave;

    @ManyToOne
    private Estado estado;

    @OneToMany
    private List<Colonia> colonias;

    public Ciudad() {
        super();
    }
}