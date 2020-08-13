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
@EqualsAndHashCode( exclude = { "id", "colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "zona_tipo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ZonaTipo implements Serializable {
    
    private static final long serialVersionUID = -7321890937862530088L;
    @Id
    @GeneratedValue(
            generator = "sequence_zona_tipo",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_zona_tipo",
            allocationSize = 10
    )
    @Column(name = "id")
    private Integer id;
    
    @Analyzer(definition = "es")
    @Field(store = Store.YES)
    @Field(name = "zonaTipoEs_beginEnd", store = Store.YES, analyzer = @Analyzer(definition = "es_beginEnd"))
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @OneToMany(mappedBy = "zonaTipo", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
}