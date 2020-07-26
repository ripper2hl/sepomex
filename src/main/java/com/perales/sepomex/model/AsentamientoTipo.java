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
@EqualsAndHashCode( exclude = { "id","colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "asentamiento_tipo")
public class AsentamientoTipo implements Serializable {
    
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
    private Integer id;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "sepomex_clave", nullable = false)
    private String sepomexClave;
    
    
    @OneToMany(mappedBy = "asentamientoTipo")
    @JsonManagedReference
    private List<Colonia> colonias;
}
