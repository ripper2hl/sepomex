package com.perales.sepomex.functions;

import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.service.ColoniaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Log4j2
@Component
public class ObtenerColonia implements Function<Colonia, List<Colonia>> {
    
    
    private ColoniaService coloniaService;
    
    @Autowired
    public ObtenerColonia(ColoniaService coloniaService) {
        this.coloniaService = coloniaService;
    }
    
    @Override
    public List<Colonia> apply(Colonia colonia) {
        return coloniaService.search(colonia);
    }
}
