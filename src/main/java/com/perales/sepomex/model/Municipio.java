package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode( exclude = { "id", "colonias", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@ToString(exclude = {"estado","ciudad", "colonias", "codigosPostales"})
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


    @FullTextField(analyzer = "es_beginEnd")
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
