package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode( exclude = { "id", "colonias", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "municipio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Municipio implements Serializable {
    
    private static final long serialVersionUID = 390174828643611176L;
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
    

    @Field(store = Store.YES)
    @Field(name = "municipioEs_beginEnd", store = Store.YES, analyzer = @Analyzer(definition = "es_beginEnd"))
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @OneToMany(mappedBy = "municipio", fetch = FetchType.LAZY)
    private List<CodigoPostal> codigosPostales;
    
    @OneToMany(mappedBy = "municipio", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
}
