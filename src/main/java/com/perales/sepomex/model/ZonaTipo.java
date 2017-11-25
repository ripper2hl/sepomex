package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "zona_tipo")
public class ZonaTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "zonaTipo")
    private List<Colonia> colonias;
    public ZonaTipo() {
        super();
    }
}
