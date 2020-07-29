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
@EqualsAndHashCode( exclude = { "id", "colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "inegi_clave_ciudad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private Integer id;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @OneToMany(mappedBy = "inegiClaveCiudad", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
}