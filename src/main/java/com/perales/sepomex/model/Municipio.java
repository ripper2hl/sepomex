package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.*;
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
@EqualsAndHashCode( exclude = { "id", "colonias", "ciudad", "estado", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "municipio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    @JsonBackReference
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    @JsonBackReference
    private Ciudad ciudad;
    
    @OneToMany(mappedBy = "municipio", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CodigoPostal> codigosPostales;
    
    @OneToMany(mappedBy = "municipio", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Colonia> colonias;
}