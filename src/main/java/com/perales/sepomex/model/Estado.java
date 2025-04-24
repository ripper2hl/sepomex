package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode(exclude = {"id", "ciudades", "municipios", "colonias", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = {"ciudades", "municipios", "colonias", "codigosPostales"})
@Entity(name = "estado")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Representa un estado con información básica.")
public class Estado implements Serializable {
    private static final long serialVersionUID = -7541622533488952041L;

    @Id
    @GeneratedValue(
            generator = "sequence_estado",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_estado",
            allocationSize = 10
    )
    @Column(name = "id")
    @Schema(description = "ID único del estado", example = "19")
    private Integer id;

    @Analyzer(definition = "es")
    @Field(store = Store.YES)
    @Field(name = "estadoEs_beginEnd", store = Store.YES, analyzer = @Analyzer(definition = "es_beginEnd"))
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    @Schema(description = "Nombre del estado", example = "Nuevo León")
    private String nombre;

    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "inegi_clave", nullable = false)
    @Schema(description = "Clave INEGI del estado", example = "19")
    private String inegiClave;

    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación de Swagger
    private List<Ciudad> ciudades;

    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación de Swagger
    private List<Municipio> municipios;

    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación de Swagger
    private List<Colonia> colonias;

    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación de Swagger
    private List<CodigoPostal> codigosPostales;
}
