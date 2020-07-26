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
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "colonia")
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
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "inegi_clave_ciudad_id")
    private InegiClaveCiudad inegiClaveCiudad;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "inegi_clave_municipio_id")
    private InegiClaveMunicipio inegiClaveMunicipio;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "codigo_postal_id")
    private CodigoPostal codigoPostal;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_id")
    private CodigoPostal codigoPostalAdministracionAsentamiento;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "codigo_postal_administracion_asentamiento_oficina_id")
    private CodigoPostal codigoPostalAdministracionAsentamientoOficina;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "asentamiento_tipo_id")
    private AsentamientoTipo asentamientoTipo;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "zona_tipo_id")
    private ZonaTipo zonaTipo;
    
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull
    @NotBlank
    @Column(name = "identificador_municipal", nullable = false)
    private String identificadorMunicipal;
}