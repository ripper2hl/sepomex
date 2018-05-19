package com.perales.sepomex.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "asentamiento_tipo")
public class AsentamientoTipo implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
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
    
    @NotNull
    @OneToMany(mappedBy = "asentamientoTipo")
    private List<Colonia> colonias;

    public AsentamientoTipo() {
        super();
    }
}
