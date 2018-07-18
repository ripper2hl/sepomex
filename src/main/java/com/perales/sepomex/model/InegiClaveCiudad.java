package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "inegi_clave_ciudad")
public class InegiClaveCiudad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @JsonBackReference(value = "colonias")
    @OneToMany(mappedBy = "inegiClaveCiudad")
    private List<Colonia> colonias;
}