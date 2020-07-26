package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode( exclude = { "id", "estado", "ciudades" , "municipios", "colonias", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "ciudad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ciudad implements Serializable {
    
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
    private Integer id;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    
    @OneToMany(mappedBy = "ciudad", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
    
    
    @OneToMany(mappedBy = "ciudad", fetch = FetchType.LAZY)
    private List<Municipio> municipios;
    
    
    @OneToMany(mappedBy = "ciudad", fetch = FetchType.LAZY)
    private List<CodigoPostal> codigosPostales;
}