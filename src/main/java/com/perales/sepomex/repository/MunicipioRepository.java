package com.perales.sepomex.repository;

import com.perales.sepomex.model.Estado;
import com.perales.sepomex.model.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    
    Municipio findFirstByNombreAndEstadoId(String nombre, Integer estadoId);
    
    Page<Municipio> findByEstadoIdOrderByNombre(Integer estadoId, Pageable pageable);

    @Query("SELECT m FROM municipio m JOIN FETCH m.estado WHERE m.nombre = :nombre")
    List<Municipio> findFirstByNombre(String nombre);

    @Query("SELECT m FROM municipio m JOIN FETCH m.estado e WHERE m.nombre = :nombreMunicipio AND e.nombre = :nombreEstado")
    Municipio findFirstByNombreAndNombreEstado(String nombreMunicipio, String nombreEstado);

    @Query("SELECT m FROM municipio m JOIN FETCH m.estado e WHERE m.nombre = :nombreMunicipio AND e.id = :idEstado")
    Municipio findFirstByNombreAndIdEstado(String nombreMunicipio, Integer idEstado);

    @Query("SELECT m FROM municipio m JOIN FETCH m.estado")
    List<Municipio> findAllMunicipiosWithEstado();
}
