package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ZonaTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    @OneToMany
    private List<Colonia> colonias;
    public ZonaTipo() {
        super();
    }
}
