package com.perales.sepomex.repository;

import com.perales.sepomex.model.ZonaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaTipoRepository extends JpaRepository<ZonaTipo, Integer> {
}
