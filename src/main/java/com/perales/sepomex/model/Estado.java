package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "estado")
public class Estado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @NotBlank
    @Column(name = "nombre")
    private String nombre;
    
    @NotNull
    @NotBlank
    @Column(name = "inegi_clave")
    private String inegiClave;
    
    @NotNull
    @OneToMany(mappedBy = "estado")
    private List<Ciudad> ciudades;
    
    @NotNull
    @OneToMany(mappedBy = "estado")
    private List<Municipio> municipios;
    
    @NotNull
    @OneToMany(mappedBy = "estado")
    private List<Colonia> colonias;
    
    @NotNull
    @OneToMany(mappedBy = "estado")
    private List<CodigoPostal> codigosPostales;

    public Estado() {
        super();
    }
}
