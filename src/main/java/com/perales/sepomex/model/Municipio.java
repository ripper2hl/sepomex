package com.perales.sepomex.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "municipio")
public class Municipio implements Serializable {
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
    @Column(name = "identificador_municipal", nullable = false)
    private String identificadorMunicipal;
    
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @OneToMany(mappedBy = "municipio")
    private List<CodigoPostal> codigosPostales;
    
    @OneToMany(mappedBy = "municipio")
    private List<Colonia> colonias;

    public Municipio() {
        super();
    }
}