package com.perales.sepomex.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "codigo_postal")
public class CodigoPostal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "codigoPostal")
    private List<Colonia> colonias;

    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamiento")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamiento;

    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamientoOficina")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamientoOficina;

    public CodigoPostal() {
        super();
    }

}