package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;


import java.util.List;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositorio para la entidad Ticket que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad Ticket.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {




    /**
     * Obtiene todos los tickets que tienen un descuento mayor que un valor específico.
     *
     * @param discount el valor mínimo del descuento.
     * @return una lista de tickets con un descuento mayor que el valor especificado.
     */
    List<Ticket> findByDiscountGreaterThan(Float discount);
}
