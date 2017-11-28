package com.perales.sepomex.repository;

import com.perales.sepomex.model.InegiClaveMunicipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InegiClaveMunicipioRepository extends JpaRepository<InegiClaveMunicipio,Integer>{
    InegiClaveMunicipio findFirstByNombre(String nombre);
}