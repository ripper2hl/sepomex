package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "ciudad")
public class Ciudad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    private Estado estado;

    @OneToMany(mappedBy = "ciudad")
    private List<Colonia> colonias;
    
    @OneToMany(mappedBy = "ciudad")
    private List<Municipio> municipios;

    public Ciudad() {
        super();
    }
}