package com.perales.sepomex.repository;

import com.perales.sepomex.model.AsentamientoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsentamientoTipoRepository extends JpaRepository<AsentamientoTipo, Integer> {
}
