package com.perales.sepomex.repository;

import com.perales.sepomex.model.Estado;
import com.perales.sepomex.model.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    
    Municipio findFirstByNombreAndEstadoId(String nombre, Integer estadoId);
    
    Page<Municipio> findByEstadoIdOrderByNombre(Integer estadoId, Pageable pageable);

    @Query("SELECT m FROM municipio m JOIN FETCH m.estado WHERE m.nombre = :nombre")
    Municipio findFirstByNombre(String nombre);
}
