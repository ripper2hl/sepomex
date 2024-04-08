package com.perales.sepomex.util;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.es.SpanishLightStemFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class MyLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer( "spanish" ).custom()
                .tokenizer( StandardTokenizerFactory.class )
                .tokenFilter( SpanishLightStemFilterFactory.class )
                .tokenFilter( LowerCaseFilterFactory.class )
                .tokenFilter( SnowballPorterFilterFactory.class )
                .param( "language", "Spanish" )
                .tokenFilter( LowerCaseFilterFactory.class );
    }
}
