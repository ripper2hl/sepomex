package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.InegiClaveMunicipio;
import com.perales.sepomex.repository.InegiClaveMunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class InegiClaveMunicipioService implements ServiceGeneric<InegiClaveMunicipio, Integer> {

    @Autowired
    private InegiClaveMunicipioRepository inegiClaveMunicipioRepository;

    public InegiClaveMunicipio buscarPorId(Integer id) {
        return null;
    }

    public Page<InegiClaveMunicipio> buscarTodos(int page, int size) {
        return null;
    }

    public InegiClaveMunicipio guardar(InegiClaveMunicipio entity) {
        return inegiClaveMunicipioRepository.save(entity);
    }

    public InegiClaveMunicipio actualizar(InegiClaveMunicipio entity) {
        return null;
    }

    public InegiClaveMunicipio borrar(Integer id) {
        return null;
    }

    public InegiClaveMunicipio findFirstByNombre(String nombre) {
        return inegiClaveMunicipioRepository.findFirstByNombre(nombre);
    }
}
