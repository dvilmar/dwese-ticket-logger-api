package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class FileStorageController {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageController.class);

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    /**
     * Devuelve un archivo de imagen dado su nombre.
     *
     * Este método maneja la solicitud para obtener un archivo almacenado en el servidor.
     * La ruta del archivo se construye utilizando la variable de entorno definida
     * para el directorio de almacenamiento (`UPLOAD_PATH`).
     *
     * Valida que el archivo exista y sea legible antes de enviarlo como respuesta.
     * También detecta el tipo MIME para asegurarse de que el cliente pueda procesar correctamente el archivo.
     *
     * @param fileName El nombre del archivo a descargar, recibido como parte de la URL.
     * @return ResponseEntity con el archivo solicitado o un código de error si ocurre algún problema.
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            // Construir la ruta completa del archivo a partir del directorio de almacenamiento y el nombre del archivo.
            Path filePath = Paths.get(uploadPath).resolve(fileName).normalize();
            // Crear un recurso a partir de la URI del archivo.
            Resource resource = new UrlResource(filePath.toUri());
            // Verificar si el archivo existe y es accesible.
            if (resource.exists() && resource.isReadable()) {
                logger.info("Sirviendo archivo: {}", fileName);
                // Intentar detectar el tipo MIME del archivo.
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    // Si no se puede determinar el tipo MIME, se asigna un tipo genérico.
                    contentType = "application/octet-stream";
                    logger.warn("No se pudo detectar el tipo MIME del archivo {}. Se usará el tipo genérico.", fileName);
                }
                // Devolver el archivo con el tipo MIME y configuración adecuada para mostrarlo en línea.
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Si el archivo no existe o no es accesible, devolver un error 404 (NOT FOUND).
                logger.error("El archivo {} no existe o no se puede leer.", fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (IOException e) {
            logger.error("Error al servir el archivo {}: {}", fileName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}

