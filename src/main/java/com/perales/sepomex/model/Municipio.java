package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "municipio")
public class Municipio implements Serializable {
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