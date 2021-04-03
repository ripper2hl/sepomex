package com.perales.sepomex.functions;

import com.perales.sepomex.model.Estado;
import com.perales.sepomex.service.EstadoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Log4j2
public class ObtenerEstados implements Function<Void, Page<Estado>> {
    
    @Autowired
    private EstadoService estadoService;
    
    @Override
    public Page<Estado> apply(Void unused) {
        return estadoService.buscarTodos(0,33);
    }
}
