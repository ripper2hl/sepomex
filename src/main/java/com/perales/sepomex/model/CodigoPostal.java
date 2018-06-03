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
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @OneToMany(mappedBy = "codigoPostal")
    private List<Colonia> colonias;
    
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamiento")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamiento;
    
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamientoOficina")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamientoOficina;

    public CodigoPostal() {
        super();
    }

}