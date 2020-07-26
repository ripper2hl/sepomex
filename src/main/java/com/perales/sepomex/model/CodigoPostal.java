package com.perales.sepomex.model;

import com.fasterxml.jackson.annotation.*;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;
    
    @OneToMany(mappedBy = "codigoPostal", fetch = FetchType.LAZY)
    private List<Colonia> colonias;
    
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamiento", fetch = FetchType.LAZY)
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamiento;
    
    @OneToMany(mappedBy = "codigoPostalAdministracionAsentamientoOficina", fetch = FetchType.LAZY)
    private List<Colonia> coloniasCodigoPostalAdministracionAsentamientoOficina;
}