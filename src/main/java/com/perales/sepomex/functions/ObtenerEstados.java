package com.perales.sepomex.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.service.EstadoService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Log4j2
public class ObtenerEstados implements Function<Void, String> {
    
    @Autowired
    private EstadoService estadoService;
    
    @SneakyThrows
    @Override
    public String apply(Void unused) {
        Page<Estado> page = estadoService.buscarTodos(0, 33);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        String objectAsString = objectMapper.writeValueAsString(page);
        return objectAsString;
    }
}
