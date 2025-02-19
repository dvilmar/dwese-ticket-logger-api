package org.iesalixar.daw2.dvm.dwese_ticket_logger_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DweseTicketLoggerApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DweseTicketLoggerApiApplication.class);
    }

        public static void main(String[] args) {
                SpringApplication.run(DweseTicketLoggerApiApplication.class, args);
        }

}

