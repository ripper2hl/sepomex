package com.perales.sepomex.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Data
@EqualsAndHashCode(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@ToString(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@Entity(name = "colonia")
public class Colonia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "inegi_clave_ciudad_id")
    private InegiClaveCiudad inegiClaveCiudad;

    @ManyToOne
    @JoinColumn(name = "inegi_clave_municipio_id")
    private InegiClaveMunicipio inegiClaveMunicipio;

    @ManyToOne
    @JoinColumn(name = "codigo_postal_id")
    private CodigoPostal codigoPostal;

    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_id")
    private CodigoPostal codigoPostalAdministracionAsentamiento;

    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_oficina_id")
    private CodigoPostal codigoPostalAdministracionAsentamientoOficina;

    @ManyToOne
    @JoinColumn(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;

    @ManyToOne
    @JoinColumn(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;

    public Colonia() {
        super();
    }
}