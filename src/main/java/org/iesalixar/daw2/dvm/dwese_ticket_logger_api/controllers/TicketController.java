package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.TicketCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.TicketDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;

    /**
     * Lista todos los tickets almacenados en la base de datos.
     *
     * @return ResponseEntity con la lista de tickets o un mensaje de error.
     */
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        logger.info("Solicitando la lista de todos los tickets...");
        try {
            List<TicketDTO> tickets = ticketService.getAllTickets();
            logger.info("Se han encontrado {} tickets.", tickets.size());
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            logger.error("Error al listar los tickets: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene un ticket específico por su ID.
     *
     * @param id ID del ticket solicitado.
     * @return ResponseEntity con el ticket encontrado o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        logger.info("Buscando ticket con ID {}", id);
        try {
            TicketDTO ticketDTO = ticketService.getTicketById(id);
            logger.info("Ticket con ID {} encontrado.", id);
            return ResponseEntity.ok(ticketDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("No se encontró ningún ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al buscar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el ticket.");
        }
    }

    /**
     * Crea un nuevo ticket.
     *
     * @param ticketCreateDTO DTO con los datos para crear el ticket.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con el ticket creado o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createTicket(
            @Valid @RequestBody TicketCreateDTO ticketCreateDTO,
            Locale locale) {
        logger.info("Creando un nuevo ticket...");
        try {
            TicketDTO createdTicket = ticketService.createTicket(ticketCreateDTO, locale);
            logger.info("Ticket creado exitosamente con ID {}", createdTicket.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear el ticket: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear el ticket: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el ticket.");
        }
    }

    /**
     * Actualiza un ticket existente.
     *
     * @param id ID del ticket a actualizar.
     * @param ticketCreateDTO DTO con los datos para actualizar el ticket.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con el ticket actualizado o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketCreateDTO ticketCreateDTO,
            Locale locale) {
        logger.info("Actualizando ticket con ID {}", id);
        try {
            TicketDTO updatedTicket = ticketService.updateTicket(id, ticketCreateDTO, locale);
            logger.info("Ticket con ID {} actualizado exitosamente.", id);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el ticket.");
        }
    }

    /**
     * Elimina un ticket por su ID.
     *
     * @param id ID del ticket a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        logger.info("Eliminando ticket con ID {}", id);
        try {
            ticketService.deleteTicket(id);
            logger.info("Ticket con ID {} eliminado exitosamente.", id);
            return ResponseEntity.ok("Ticket eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el ticket.");
        }
    }

    /**
     * Endpoint para añadir un producto a un ticket.
     *
     * @param ticketId ID del ticket.
     * @param productId ID del producto.
     * @return ResponseEntity con el TicketDTO actualizado.
     */
    @PostMapping("/{ticketId}/products/{productId}")
    public ResponseEntity<?> addProductToTicket(@PathVariable Long ticketId, @PathVariable Long productId) {
        logger.info("Añadiendo producto con ID {} al ticket con ID {}", productId, ticketId);
        try {
            TicketDTO updatedTicket = ticketService.addProductToTicket(ticketId, productId);
            logger.info("Producto con ID {} añadido exitosamente al ticket con ID {}", productId, ticketId);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al añadir producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al añadir producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al añadir producto al ticket.");
        }
    }

    /**
     * Endpoint para eliminar un producto de un ticket.
     *
     * @param ticketId ID del ticket.
     * @param productId ID del producto.
     * @return ResponseEntity con el TicketDTO actualizado.
     */
    @DeleteMapping("/{ticketId}/products/{productId}")
    public ResponseEntity<?> removeProductFromTicket(@PathVariable Long ticketId, @PathVariable Long productId) {
        logger.info("Eliminando producto con ID {} del ticket con ID {}", productId, ticketId);
        try {
            TicketDTO updatedTicket = ticketService.removeProductFromTicket(ticketId, productId);
            logger.info("Producto con ID {} eliminado exitosamente del ticket con ID {}", productId, ticketId);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar producto del ticket.");
        }
    }
}
