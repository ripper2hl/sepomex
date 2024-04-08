package com.perales.sepomex.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.engine.backend.types.TermVector;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode(exclude = {"id", "colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity(name = "asentamiento_tipo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AsentamientoTipo implements Serializable {

    private static final long serialVersionUID = 3547469072116532512L;

    @Id
    @GeneratedValue(generator = "sequence_asentamiento_tipo", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence_asentamiento_tipo", allocationSize = 10)
    @Column(name = "id")
    private Integer id;

    @GenericField
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @FullTextField(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "sepomex_clave", nullable = false)
    private String sepomexClave;

    @OneToMany(mappedBy = "asentamientoTipo", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
}
