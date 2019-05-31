package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode( exclude = { "id", "colonias", "ciudad", "estado", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "municipio")
public class Municipio implements Serializable {

    @Id
    @GeneratedValue(
            generator = "sequence_municipio",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_municipio",
            allocationSize = 10
    )
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @JsonBackReference(value = "codigosPostales")
    @OneToMany(mappedBy = "municipio")
    private List<CodigoPostal> codigosPostales;
    
    @JsonBackReference(value = "colonias")
    @OneToMany(mappedBy = "municipio")
    private List<Colonia> colonias;
}