package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Indexed
@Data
@EqualsAndHashCode(
        exclude = { "id", "ciudad" , "municipio", "estado",
                "colonias", "coloniasCodigoPostalAdministracionAsentamiento," +
                "coloniasCodigoPostalAdministracionAsentamientoOficina"})
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Entity(name = "codigo_postal")
public class CodigoPostal implements Serializable {
    
    @Id
    @GeneratedValue(
            generator = "sequence_codigo_postal",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "sequence_codigo_postal",
            allocationSize = 10
    )
    @Column(name = "id")
    private Integer id;
    
    @Field(termVector = TermVector.YES)
    @NotNull
    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "codigoPostal")
    private List<Colonia> colonias;
    
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamiento")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamiento;
    
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamientoOficina")
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamientoOficina;
}