package com.perales.sepomex.functions;

import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.model.Municipio;
import com.perales.sepomex.service.ColoniaService;
import com.perales.sepomex.util.FunctionColoniaPojo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Log4j2
@Component
public class ObtenerColonia implements Function<FunctionColoniaPojo, List<Colonia>> {
    
    private ColoniaService coloniaService;
    
    @Autowired
    public ObtenerColonia(ColoniaService coloniaService) {
        this.coloniaService = coloniaService;
    }
    
    @Override
    public List<Colonia> apply(FunctionColoniaPojo functionColoniaPojo) {
        Colonia colonia = new Colonia();
        if(functionColoniaPojo.getNombre() != null){
            colonia.setNombre(functionColoniaPojo.getNombre());
        }
        if(functionColoniaPojo.getEstadoId() != null){
            Estado estado = new Estado();
            estado.setId(functionColoniaPojo.getEstadoId());
            colonia.setEstado( estado );
        }
        if(functionColoniaPojo.getMunicipioId() != null){
            Municipio municipio = new Municipio();
            municipio.setId(functionColoniaPojo.getMunicipioId());
            colonia.setMunicipio( municipio );
        }
        return coloniaService.search(colonia);
    }
}
