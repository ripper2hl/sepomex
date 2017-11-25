package com.perales.sepomex.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


@Data
@EqualsAndHashCode(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@ToString(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@Entity(name = "colonia")
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "clave_ciudad")
    private String claveCiudad;

    @Column(name = "clave_municipio")
    private String claveMunicipio;

    @Column(name = "codigo_postal_administracion_asentamiento")
    private String codigoPostalAdministracionAsentamiento;

    @Column(name = "codigo_postal_administracion_asentamiento_oficina")
    private String codigoPostalAdministracionAsentamientoOficina;

    @ManyToOne
    @Column(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;

    @ManyToOne
    @Column(name = "municipio_id")
    private Municipio municipio;

    @ManyToOne
    @Column(name = "estado_id")
    private Estado estado;

    @ManyToOne
    @Column(name = "ciudad_id")
    private Ciudad ciudad;

    @ManyToOne
    @Column(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;

    public Colonia() {
        super();
    }
}