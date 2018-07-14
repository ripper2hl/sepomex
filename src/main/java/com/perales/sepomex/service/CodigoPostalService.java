package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.CodigoPostal;
import com.perales.sepomex.repository.CodigoPostalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CodigoPostalService implements ServiceGeneric<CodigoPostal, Integer> {

    @Autowired
    private CodigoPostalRepository codigoPostalRepository;
    
    @Transactional(readOnly = true)
    public CodigoPostal buscarPorId(Integer id) {
        return codigoPostalRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<CodigoPostal> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return codigoPostalRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public CodigoPostal guardar(CodigoPostal entity) {
        return codigoPostalRepository.save(entity);
    }
    
    @Transactional
    public CodigoPostal actualizar(CodigoPostal entity) {
        return codigoPostalRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public CodigoPostal borrar(Integer id) {
        CodigoPostal codigoPostal = codigoPostalRepository.findById(id).get();
        codigoPostal.getColonias().forEach( colonia -> colonia.setCodigoPostal(null) );
        
        codigoPostal.getColoniasCodigoPostalAdministracionAsentamiento()
                .forEach(colonia -> colonia.setCodigoPostalAdministracionAsentamiento(null));
        
        codigoPostal.getColoniasCodigoPostalAdministracionAsentamientoOficina()
                .forEach(colonia -> colonia.setCodigoPostalAdministracionAsentamientoOficina(null));
        codigoPostalRepository.delete(codigoPostal);
        return codigoPostal;
    }
    
    @Transactional(readOnly = true)
    public CodigoPostal findByNombre(String nombre) {
        return codigoPostalRepository.findFirstByNombre(nombre);
    }
}