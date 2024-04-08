package com.perales.sepomex.repository;

import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.model.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColoniaRepository extends JpaRepository<Colonia, Long> {
    
    Page<Colonia> findByMunicipioId(Integer municipioId, Pageable pageable);
    
    @EntityGraph(value = "Colonia.detail", type = EntityGraph.EntityGraphType.LOAD)
    Colonia findOneById(Long id);

    @Query("SELECT c FROM colonia  c JOIN FETCH c.estado JOIN FETCH c.municipio WHERE c.codigoPostal.nombre = :codigoPostal")
    List<Colonia> findByCodigoPostal_Nombre(@Param("codigoPostal") String codigoPostal);

    List<Colonia> findByNombreAndMunicipioIdAndEstadoId(String nombreColonia,
                                                               Integer municipioId,
                                                               Integer estadoId);


    @Query("SELECT c FROM colonia c JOIN FETCH c.municipio m JOIN FETCH m.estado")
    List<Colonia> findAllColoniasWithEstadoAndMunicipio();

}
