package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@ToString(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties(ignoreUnknown = true)
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
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "inegi_clave_ciudad_id")
    private InegiClaveCiudad inegiClaveCiudad;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "inegi_clave_municipio_id")
    private InegiClaveMunicipio inegiClaveMunicipio;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "codigo_postal_id")
    private CodigoPostal codigoPostal;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_id")
    private CodigoPostal codigoPostalAdministracionAsentamiento;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_oficina_id")
    private CodigoPostal codigoPostalAdministracionAsentamientoOficina;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;
}