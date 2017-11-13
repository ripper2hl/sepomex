package com.perales.sepomex.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


@Data
@EqualsAndHashCode(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@ToString(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@Entity
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String codigoPostal;

    @ManyToOne(cascade=CascadeType.ALL)
    private AsentamientoTipo asentamientoTipo;

    @ManyToOne(cascade=CascadeType.ALL)
    private Municipio municipio;

    @ManyToOne(cascade=CascadeType.ALL)
    private Estado estado;

    @ManyToOne(cascade=CascadeType.ALL)
    private Ciudad ciudad;

    private String codigoPostalAdministracionAsentamiento;
    private String codigoPostalAdministracionAsentamientoOficina;

    @ManyToOne(cascade=CascadeType.ALL)
    private ZonaTipo zonaTipo;

    public Colonia() {
        super();
    }

}
