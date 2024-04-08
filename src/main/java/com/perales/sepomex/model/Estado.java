package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.annotations.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode( exclude = { "id", "ciudades" , "municipios", "colonias", "codigosPostales"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@ToString(exclude = {"ciudades","municipios", "colonias", "codigosPostales"})
@Entity(name = "estado")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Estado implements Serializable {
    private static final long serialVersionUID = -7541622533488952041L;
    @Id
    @GeneratedValue(
            generator = "sequence_estado",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_estado",
            allocationSize = 10
    )
    @Column(name = "id")
    private Integer id;
    

    @Field(store = Store.YES)
    @Field(name = "estadoEs_beginEnd", store = Store.YES, analyzer = @Analyzer(definition = "es_beginEnd"))
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "inegi_clave", nullable = false)
    private String inegiClave;
    
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    private List<Ciudad> ciudades;
    
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    private List<Municipio> municipios;
    
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
    
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    private List<CodigoPostal> codigosPostales;
}
