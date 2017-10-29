package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String codigoPostal;

    @ManyToOne
    private AsentamientoTipo asentamientoTipo;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private Estado estado;

    @ManyToOne
    private Ciudad ciudad;
    private String codigoPostalAdministracionAsentamiento;
    private String codigoPostalAdministracionAsentamientoOficina;

    @ManyToOne
    private ZonaTipo zonaTipo;

    public Colonia() {
        super();
    }

}
