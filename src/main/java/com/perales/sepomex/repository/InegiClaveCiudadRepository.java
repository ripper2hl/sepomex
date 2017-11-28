package com.perales.sepomex.repository;

import com.perales.sepomex.model.InegiClaveCiudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InegiClaveCiudadRepository extends JpaRepository<InegiClaveCiudad,Integer>{
    InegiClaveCiudad findFirstByNombre(String nombre);
}