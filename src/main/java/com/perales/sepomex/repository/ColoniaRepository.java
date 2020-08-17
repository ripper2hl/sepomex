package com.perales.sepomex.repository;

import com.perales.sepomex.model.Colonia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColoniaRepository extends JpaRepository<Colonia, Long> {
    
    Page<Colonia> findByMunicipioId(Integer municipioId, Pageable pageable);
    
    @EntityGraph(value = "Colonia.detail", type = EntityGraph.EntityGraphType.LOAD)
    Colonia findOneById(Long id);
}
