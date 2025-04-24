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
@EqualsAndHashCode(
        exclude = { "id", "ciudad" , "municipio", "estado",
                "colonias", "coloniasCodigoPostalAdministracionAsentamiento",
                "coloniasCodigoPostalAdministracionAsentamientoOficina"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "codigo_postal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Representa un código postal con información básica.")
public class CodigoPostal implements Serializable {
    
    private static final long serialVersionUID = -3109072997206117833L;

    @Id
    @GeneratedValue(
            generator = "sequence_codigo_postal",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_codigo_postal",
            allocationSize = 10
    )
    @Column(name = "id")
    @Schema(description = "ID único del código postal", example = "1")
    private Long id;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    @Schema(description = "Nombre del código postal", example = "01000")
    private String nombre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    @Schema(hidden = true) // Oculta este campo en la documentación
    private Municipio municipio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    @Schema(hidden = true) // Oculta este campo en la documentación
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    @Schema(hidden = true) // Oculta este campo en la documentación
    private Ciudad ciudad;
    
    @OneToMany(mappedBy = "codigoPostal", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación
    private List<Colonia> colonias;
    
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamiento", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamiento;
    
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamientoOficina", fetch = FetchType.LAZY)
    @Schema(hidden = true) // Oculta este campo en la documentación
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamientoOficina;
}