package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode(exclude = {"id", "colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity(name = "asentamiento_tipo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Representa un tipo de asentamiento con información básica.")
public class AsentamientoTipo implements Serializable {

    private static final long serialVersionUID = 3547469072116532512L;

    @Id
    @GeneratedValue(
            generator = "sequence_asentamiento_tipo",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_asentamiento_tipo",
            allocationSize = 10
    )
    @Column(name = "id")
    @Schema(description = "ID único del tipo de asentamiento", example = "1")
    private Integer id;

    @Analyzer(definition = "es")
    @Field(store = Store.YES)
    @Field(name = "asentamientoTipoEs_beginEnd", store = Store.YES, analyzer = @Analyzer(definition = "es_beginEnd"))
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    @Schema(description = "Nombre del tipo de asentamiento", example = "Colonia")
    private String nombre;

    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "sepomex_clave", nullable = false)
    @Schema(description = "Clave SEPOMEX del tipo de asentamiento", example = "09")
    private String sepomexClave;

    @OneToMany(mappedBy = "asentamientoTipo", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta esta relación en la documentación de Swagger
    private List<Colonia> colonias;
}