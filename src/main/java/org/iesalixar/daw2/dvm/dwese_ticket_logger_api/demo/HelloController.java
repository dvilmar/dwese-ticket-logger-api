package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {


    // Creación del logger para esta clase, nos permitirá registrar mensajes en diferentes niveles (INFO, DEBUG, ERROR, etc.)
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);


    /**
     * Método que responde a una petición GET en la ruta "/hello".
     * Este método recibe un parámetro opcional "name" y devuelve un saludo personalizado.
     *
     * @param name Parámetro opcional que representa el nombre a saludar. Si no se pasa, por defecto es "World".
     * @return Una cadena de texto con el saludo personalizado.
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        // Loguea cuando el método hello es llamado, mostrando el nombre recibido
        logger.info("Request received to /hello endpoint with parameter name: {}", name);


        // Formatea y devuelve el mensaje de saludo
        String greeting = String.format("Hello %s!", name);


        // Loguea el mensaje de saludo que se va a devolver
        logger.debug("Greeting message to be returned: {}", greeting);


        return greeting;
    }
}

