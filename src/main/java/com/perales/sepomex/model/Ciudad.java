package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "ciudad")
public class Ciudad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @NotNull
    @OneToMany(mappedBy = "ciudad")
    private List<Colonia> colonias;
    
    @NotNull
    @OneToMany(mappedBy = "ciudad")
    private List<Municipio> municipios;
    
    @NotNull
    @OneToMany(mappedBy = "ciudad")
    private List<CodigoPostal> codigosPostales;

    public Ciudad() {
        super();
    }
}