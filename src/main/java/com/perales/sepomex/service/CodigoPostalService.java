package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.CodigoPostal;
import com.perales.sepomex.repository.CodigoPostalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CodigoPostalService implements ServiceGeneric<CodigoPostal, Integer> {

    @Autowired
    private CodigoPostalRepository codigoPostalRepository;

    public CodigoPostal buscarPorId(Integer id) {
        return codigoPostalRepository.getOne(id);
    }

    public List<CodigoPostal> buscarTodos(int page, int size) {
        return null;
    }

    @Transactional
    public CodigoPostal guardar(CodigoPostal entity) {
        return codigoPostalRepository.save(entity);
    }

    public CodigoPostal actualizar(CodigoPostal entity) {
        return null;
    }

    public CodigoPostal borrar(Integer id) {
        return null;
    }

    @Transactional
    public CodigoPostal findByNombre(String nombre) {
        return codigoPostalRepository.findFirstByNombre(nombre);
    }
}