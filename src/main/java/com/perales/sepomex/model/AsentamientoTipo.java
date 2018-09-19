package com.perales.sepomex.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode( exclude = { "id","colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotNull
    @NotBlank
    @Column(name = "sepomex_clave", nullable = false)
    private String sepomexClave;
    
    @JsonBackReference(value = "colonias")
    @OneToMany(mappedBy = "asentamientoTipo")
    private List<Colonia> colonias;
}
