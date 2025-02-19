package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.CategoryCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.CategoryDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * Lista todas las categorías.
     *
     * @return ResponseEntity con la lista de categorías o un error en caso de fallo.
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("Solicitando la lista de todas las categorías...");
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            logger.info("Se han encontrado {} categorías.", categories.size());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error al listar las categorías: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene una categoría específica por su ID.
     *
     * @param id ID de la categoría solicitada.
     * @return ResponseEntity con la categoría encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        logger.info("Buscando categoría con ID {}", id);
        try {
            Optional<CategoryDTO> categoryDTO = categoryService.getCategoryById(id);

            if (categoryDTO.isPresent()) {
                logger.info("Categoría con ID {} encontrada.", id);
                return ResponseEntity.ok(categoryDTO.get());
            } else {
                logger.warn("No se encontró ninguna categoría con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al buscar la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la categoría.");
        }
    }

    /**
     * Crea una nueva categoría.
     *
     * @param createDTO DTO con los datos para crear la categoría.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la categoría creada o un mensaje de error.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createCategory(
            @Valid @ModelAttribute CategoryCreateDTO createDTO,
            Locale locale) {
        try {
            CategoryDTO categoryDTO = categoryService.createCategory(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear la categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error al guardar la imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen.");
        } catch (Exception e) {
            logger.error("Error inesperado al crear la categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la categoría.");
        }
    }



    /**
     * Actualiza una categoría existente.
     *
     * @param id ID de la categoría a actualizar.
     * @param updateDTO DTO con los datos para actualizar la categoría.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la categoría actualizada o un mensaje de error.
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute CategoryCreateDTO updateDTO,
            Locale locale) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, updateDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error al guardar la imagen para la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen.");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la categoría.");
        }
    }


    /**
     * Elimina una categoría por su ID.
     *
     * @param id ID de la categoría a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        logger.info("Eliminando categoría con ID {}", id);
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Categoría eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoría.");
        }
    }
}
