package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

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
@Entity(name = "inegi_clave_ciudad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Representa una clave INEGI asociada a una ciudad.")
public class InegiClaveCiudad implements Serializable {

    private static final long serialVersionUID = 3360169705836153435L;

    @Id
    @GeneratedValue(
            generator = "sequence_inegi_clave_ciudad",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_inegi_clave_ciudad",
            allocationSize = 10
    )
    @Column(name = "id")
    @Schema(description = "ID único de la clave INEGI de la ciudad", example = "1")
    private Integer id;

    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    @Schema(description = "Nombre de la clave INEGI de la ciudad", example = "01")
    private String nombre;

    @OneToMany(mappedBy = "inegiClaveCiudad", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta esta relación en la documentación de Swagger
    private List<Colonia> colonias;
}