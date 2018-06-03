package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
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
    
    @OneToMany(mappedBy = "inegiClaveCiudad")
    private List<Colonia> colonias;
}
