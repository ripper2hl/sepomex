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
@Entity(name = "estado")
public class Estado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotNull
    @NotBlank
    @Column(name = "inegi_clave", nullable = false)
    private String inegiClave;
    
    @JsonBackReference(value = "ciudades")
    @OneToMany(mappedBy = "estado")
    private List<Ciudad> ciudades;
    
    @JsonBackReference(value = "municipios")
    @OneToMany(mappedBy = "estado")
    private List<Municipio> municipios;
    
    @JsonBackReference(value = "colonias")
    @OneToMany(mappedBy = "estado")
    private List<Colonia> colonias;
    
    @JsonBackReference(value = "codigosPostales")
    @OneToMany(mappedBy = "estado")
    private List<CodigoPostal> codigosPostales;
}
