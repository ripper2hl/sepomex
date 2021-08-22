package com.perales.sepomex.functions;

import com.perales.sepomex.model.Municipio;
import com.perales.sepomex.service.MunicipioService;
import com.perales.sepomex.util.FunctionMunicipiosPorEstadoPojo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Log4j2
public class ObtenerMunicipiosPorEstado implements Function<FunctionMunicipiosPorEstadoPojo, Page<Municipio>> {
    
    
    private MunicipioService municipioService;
    
    @Autowired
    public ObtenerMunicipiosPorEstado(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }
    
    @Override
    public Page<Municipio> apply(FunctionMunicipiosPorEstadoPojo municipioPagePojo) {
        return municipioService.findByEstadoId( municipioPagePojo.getEstadoId(), municipioPagePojo.getPage(), municipioPagePojo.getSize() );
    }
}
