package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Indexed
@NormalizerDef(name = "lowercase",
        filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
        }
)
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
            @NamedAttributeNode("municipio"),
            @NamedAttributeNode("ciudad"),
            @NamedAttributeNode("estado"),
            @NamedAttributeNode("codigoPostal"),
            @NamedAttributeNode("codigoPostalAdministracionAsentamiento"),
            @NamedAttributeNode("codigoPostalAdministracionAsentamientoOficina"),
            @NamedAttributeNode("inegiClaveCiudad"),
            @NamedAttributeNode("inegiClaveMunicipio"),
            @NamedAttributeNode("asentamientoTipo"),
            @NamedAttributeNode("zonaTipo")
    })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Colonia implements Serializable {
    
    private static final long serialVersionUID = 7622719610604643048L;
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
    
    @Field(termVector = TermVector.YES ,
            index = Index.YES,
            normalizer = @Normalizer(definition = "lowercase"))
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    @ApiModelProperty(notes = "Nombre")
    private String nombre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
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
    @JoinColumn(name = "inegi_clave_ciudad_id")
    private InegiClaveCiudad inegiClaveCiudad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inegi_clave_municipio_id")
    private InegiClaveMunicipio inegiClaveMunicipio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;
    
    @NotNull
    @NotBlank
    @Column(name = "identificador_municipal", nullable = false)
    private String identificadorMunicipal;
}