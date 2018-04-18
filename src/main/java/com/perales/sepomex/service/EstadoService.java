package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class EstadoService implements ServiceGeneric<Estado, Integer> {

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado buscarPorId(Integer id) {
        return null;
    }

    public Page<Estado> buscarTodos(int page, int size) {
        return null;
    }

    public Estado guardar(Estado entity) {
        return estadoRepository.save(entity);
    }

    public Estado actualizar(Estado entity) {
        return null;
    }

    public Estado borrar(Integer id) {
        return null;
    }

    public Estado findByInegiClave(String inegiClave) {
        return estadoRepository.findFirstByInegiClave(inegiClave);
    }
}
