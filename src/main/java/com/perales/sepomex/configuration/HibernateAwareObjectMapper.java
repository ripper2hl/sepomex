package com.perales.sepomex.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

public class HibernateAwareObjectMapper extends ObjectMapper {
    
    public HibernateAwareObjectMapper() {
        Hibernate5Module h5module = new Hibernate5Module();
        h5module.disable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        h5module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        registerModule( h5module );
    }
    
}
