package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode(exclude = {"id", "estado", "municipio", "colonias", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity(name = "ciudad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Representa una ciudad con información básica.")
public class Ciudad implements Serializable {

    private static final long serialVersionUID = 2570095220028518900L;

    @Id
    @GeneratedValue(
            generator = "sequence_ciudad",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_ciudad",
            allocationSize = 10
    )
    @Column(name = "id")
    @Schema(description = "ID único de la ciudad", example = "1")
    private Integer id;

    @Analyzer(definition = "es")
    @Field(store = Store.YES)
    @Field(name = "ciudadEs_beginEnd", store = Store.YES, analyzer = @Analyzer(definition = "es_beginEnd"))
    @NotNull
    @Column(name = "nombre", nullable = false)
    @Schema(description = "Nombre de la ciudad", example = "Monterrey")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    @Schema(hidden = true) // Oculta este campo en la documentación
    private Estado estado;

    @OneToMany(mappedBy = "ciudad", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación
    private List<Colonia> colonias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    @Schema(hidden = true) // Oculta este campo en la documentación
    private Municipio municipio;

    @OneToMany(mappedBy = "ciudad", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación
    private List<CodigoPostal> codigosPostales;
}