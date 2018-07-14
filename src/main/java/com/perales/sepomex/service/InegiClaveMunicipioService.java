package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.InegiClaveMunicipio;
import com.perales.sepomex.repository.InegiClaveMunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InegiClaveMunicipioService implements ServiceGeneric<InegiClaveMunicipio, Integer> {

    @Autowired
    private InegiClaveMunicipioRepository inegiClaveMunicipioRepository;
    
    @Transactional(readOnly = true)
    public InegiClaveMunicipio buscarPorId(Integer id) {
        return inegiClaveMunicipioRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<InegiClaveMunicipio> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return inegiClaveMunicipioRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public InegiClaveMunicipio guardar(InegiClaveMunicipio entity) {
        return inegiClaveMunicipioRepository.save(entity);
    }
    
    @Transactional
    public InegiClaveMunicipio actualizar(InegiClaveMunicipio entity) {
        return inegiClaveMunicipioRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public InegiClaveMunicipio borrar(Integer id) {
        InegiClaveMunicipio inegiClaveMunicipio = inegiClaveMunicipioRepository.findById(id).get();
        inegiClaveMunicipio.getColonias().forEach(colonia -> colonia.setInegiClaveMunicipio(null));
        inegiClaveMunicipioRepository.delete(inegiClaveMunicipio);
        return inegiClaveMunicipio;
    }
    
    @Transactional(readOnly = true)
    public InegiClaveMunicipio findFirstByNombre(String nombre) {
        return inegiClaveMunicipioRepository.findFirstByNombre(nombre);
    }
}
