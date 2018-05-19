package com.perales.sepomex.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "inegi_clave_ciudad_id")
    private InegiClaveCiudad inegiClaveCiudad;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "inegi_clave_municipio_id")
    private InegiClaveMunicipio inegiClaveMunicipio;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_postal_id")
    private CodigoPostal codigoPostal;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_id")
    private CodigoPostal codigoPostalAdministracionAsentamiento;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_oficina_id")
    private CodigoPostal codigoPostalAdministracionAsentamientoOficina;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;

    public Colonia() {
        super();
    }
}