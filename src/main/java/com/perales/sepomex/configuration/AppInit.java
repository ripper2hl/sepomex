package com.perales.sepomex.configuration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInit implements WebApplicationInitializer {
    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        // Create ApplicationContext
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(AppConfig.class);
        applicationContext.setServletContext(servletContext);
        applicationContext.setDisplayName("sepomex");
        // Add the servlet mapping manually and make it initialize automatically
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("mvc-dispatcher", dispatcherServlet);
        servlet.addMapping("/api/*");
        servlet.setAsyncSupported(true);
        servlet.setLoadOnStartup(1);
    }
}
