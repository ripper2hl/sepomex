package com.perales.sepomex.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "estado")
public class Estado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    @OneToMany(mappedBy = "estado")
    private List<Ciudad> ciudades;
    
    @OneToMany(mappedBy = "estado")
    private List<Municipio> municipios;
    
    @OneToMany(mappedBy = "estado")
    private List<Colonia> colonias;
    
    @OneToMany(mappedBy = "estado")
    private List<CodigoPostal> codigosPostales;
}
