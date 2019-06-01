package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode( exclude = { "id", "colonias"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "inegi_clave_ciudad")
public class InegiClaveCiudad implements Serializable {
    
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
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @OneToMany(mappedBy = "inegiClaveCiudad")
    private List<Colonia> colonias;
}