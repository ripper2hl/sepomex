package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.InegiClaveCiudad;
import com.perales.sepomex.repository.InegiClaveCiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InegiClaveCiudadService implements ServiceGeneric<InegiClaveCiudad, Integer> {

    @Autowired
    private InegiClaveCiudadRepository inegiClaveCiudadRepository;

    public InegiClaveCiudad buscarPorId(Integer id) {
        return null;
    }

    public List<InegiClaveCiudad> buscarTodos(int page, int size) {
        return null;
    }

    @Transactional
    public InegiClaveCiudad guardar(InegiClaveCiudad entity) {
        return inegiClaveCiudadRepository.save(entity);
    }

    public InegiClaveCiudad actualizar(InegiClaveCiudad entity) {
        return null;
    }

    public InegiClaveCiudad borrar(Integer id) {
        return null;
    }

    @Transactional
    public InegiClaveCiudad findFirstByNombre(String nombre) {
        return inegiClaveCiudadRepository.findFirstByNombre(nombre);
    }
}
