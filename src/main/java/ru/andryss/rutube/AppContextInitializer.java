package ru.andryss.rutube;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.io.IOException;
import java.nio.file.Files;


public class AppContextInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {AppConfiguration.class};
    }

    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        try {
            String tmp = Files.createTempDirectory("multipart").toFile().getAbsolutePath();
            registration.setMultipartConfig(new MultipartConfigElement(tmp, 5 * 1024 * 1024, 7 * 1024 * 1024, 5 * 1024 * 1024));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
