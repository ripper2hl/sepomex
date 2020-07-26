package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Indexed
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {
        "id","inegiClaveCiudad","asentamientoTipo",
        "inegiClaveMunicipio","codigoPostal",
        "codigoPostalAdministracionAsentamiento",
        "codigoPostalAdministracionAsentamientoOficina",
        "municipio", "estado", "ciudad", "zonaTipo"})
@ToString(exclude = {"asentamientoTipo","municipio", "estado", "ciudad", "zonaTipo"})
@Entity(name = "colonia")
@NamedEntityGraph(name = "Colonia.detail",
    attributeNodes = {
        @NamedAttributeNode("inegiClaveCiudad"),
            @NamedAttributeNode("inegiClaveMunicipio"),
            @NamedAttributeNode("codigoPostal"),
            @NamedAttributeNode("codigoPostalAdministracionAsentamiento"),
            @NamedAttributeNode("codigoPostalAdministracionAsentamientoOficina"),
            @NamedAttributeNode("asentamientoTipo"),
            @NamedAttributeNode("municipio"),
            @NamedAttributeNode("ciudad"),
            @NamedAttributeNode("estado"),
            @NamedAttributeNode("zonaTipo")
    })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Colonia implements Serializable {
    
    @Id
    @GeneratedValue(
            generator = "sequence_colonia",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_colonia",
            allocationSize = 10
    )
    @Column(name = "id")
    @ApiModelProperty(notes = "ID")
    private Integer id;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    @ApiModelProperty(notes = "Nombre")
    private String nombre;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inegi_clave_ciudad_id")
    private InegiClaveCiudad inegiClaveCiudad;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inegi_clave_municipio_id")
    private InegiClaveMunicipio inegiClaveMunicipio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_postal_id")
    private CodigoPostal codigoPostal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_id")
    private CodigoPostal codigoPostalAdministracionAsentamiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_oficina_id")
    private CodigoPostal codigoPostalAdministracionAsentamientoOficina;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;
    
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;
    
    @NotNull
    @NotBlank
    @Column(name = "identificador_municipal", nullable = false)
    private String identificadorMunicipal;
}