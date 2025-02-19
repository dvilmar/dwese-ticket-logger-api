package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.SupermarketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * Controlador REST para gestionar operaciones CRUD relacionadas con la entidad `Supermarket`.
 */
@RestController
@RequestMapping("/api/supermarkets")
public class SupermarketController {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);

    @Autowired
    private SupermarketService supermarketService;

    /**
     * Lista todos los supermercados almacenados en la base de datos.
     *
     * @return ResponseEntity con la lista de supermercados o un error en caso de fallo.
     */
    @GetMapping
    public ResponseEntity<List<SupermarketDTO>> getAllSupermarkets() {
        logger.info("Solicitando la lista de todos los supermercados...");
        try {
            List<SupermarketDTO> supermarkets = supermarketService.getAllSupermarkets();
            logger.info("Se han encontrado {} supermercados.", supermarkets.size());
            return ResponseEntity.ok(supermarkets);
        } catch (Exception e) {
            logger.error("Error al listar los supermercados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene un supermercado específico por su ID.
     *
     * @param id ID del supermercado solicitado.
     * @return ResponseEntity con el supermercado encontrado o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupermarketById(@PathVariable Long id) {
        logger.info("Buscando supermercado con ID {}", id);
        try {
            SupermarketDTO supermarketDTO = supermarketService.getSupermarketById(id);
            logger.info("Supermercado con ID {} encontrado.", id);
            return ResponseEntity.ok(supermarketDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("No se encontró ningún supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al buscar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el supermercado.");
        }
    }

    /**
     * Crea un nuevo supermercado.
     *
     * @param createDTO DTO con los datos para crear el supermercado.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con el supermercado creado o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createSupermarket(
            @Valid @RequestBody SupermarketCreateDTO createDTO,
            Locale locale) {
        logger.info("Creando un nuevo supermercado con nombre {}", createDTO.getName());
        try {
            SupermarketDTO createdSupermarket = supermarketService.createSupermarket(createDTO);
            logger.info("Supermercado creado exitosamente con ID {}", createdSupermarket.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSupermarket);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el supermercado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear el supermercado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el supermercado.");
        }
    }

    /**
     * Actualiza un supermercado existente.
     *
     * @param id        ID del supermercado a actualizar.
     * @param createDTO DTO con los datos para actualizar el supermercado.
     * @param locale    Idioma para los mensajes de error.
     * @return ResponseEntity con el supermercado actualizado o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupermarket(
            @PathVariable Long id,
            @Valid @RequestBody SupermarketCreateDTO createDTO,
            Locale locale) {
        logger.info("Actualizando supermercado con ID {}", id);
        try {
            SupermarketDTO updatedSupermarket = supermarketService.updateSupermarket(id, createDTO);
            logger.info("Supermercado con ID {} actualizado exitosamente.", id);
            return ResponseEntity.ok(updatedSupermarket);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el supermercado.");
        }
    }

    /**
     * Elimina un supermercado por su ID.
     *
     * @param id ID del supermercado a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupermarket(@PathVariable Long id) {
        logger.info("Eliminando supermercado con ID {}", id);
        try {
            supermarketService.deleteSupermarket(id);
            logger.info("Supermercado con ID {} eliminado exitosamente.", id);
            return ResponseEntity.ok("Supermercado eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el supermercado.");
        }
    }
}
