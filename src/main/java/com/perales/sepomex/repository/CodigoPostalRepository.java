package com.perales.sepomex.repository;

import com.perales.sepomex.model.CodigoPostal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoPostalRepository extends JpaRepository<CodigoPostal, Integer> {
    CodigoPostal findFirstByNombre(String nombre);
}
