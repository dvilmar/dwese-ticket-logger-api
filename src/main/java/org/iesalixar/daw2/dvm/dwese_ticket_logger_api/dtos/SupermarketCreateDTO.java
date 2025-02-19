package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * DTO utilizado para crear o actualizar un supermercado.
 */
@Data
public class SupermarketCreateDTO {
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    private String name; // Nombre del supermercado.
}
