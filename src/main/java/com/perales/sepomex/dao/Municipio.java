package com.perales.sepomex.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "municipio")
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "identificador_municipal")
    private String identificadorMunicipal;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @OneToMany(mappedBy = "municipio")
    private List<Colonia> colonias;

    public Municipio() {
        super();
    }
}