package com.perales.sepomex.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "codigo_postal")
public class CodigoPostal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @NotBlank
    @Column(name = "nombre")
    private String nombre;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @NotNull
    @OneToMany(mappedBy = "codigoPostal")
    private List<Colonia> colonias;
    
    @NotNull
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamiento")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamiento;
    
    @NotNull
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamientoOficina")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamientoOficina;

    public CodigoPostal() {
        super();
    }

}