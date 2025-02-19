package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO (Data Transfer Object) utilizado para crear o actualizar un ticket.
 *
 * Este DTO encapsula los datos necesarios para registrar o modificar un ticket en la base de datos,
 * asegurando la validación de los campos requeridos.
 */
@Data
public class TicketCreateDTO {

    /**
     * Fecha del ticket.
     * <p>
     * Representa el momento en el que se genera el ticket. Este campo es obligatorio.
     * </p>
     * Ejemplo: `2025-01-15T10:30:00`
     */
    @NotNull(message = "La fecha no puede estar vacía.")
    private Date date;

    /**
     * Descuento aplicado al ticket.
     * <p>
     * Representa el porcentaje de descuento aplicado al total del ticket. Este campo es obligatorio.
     * </p>
     * Ejemplo: `10.00` (10% de descuento)
     */
    @NotNull(message = "El descuento no puede estar vacío.")
    private BigDecimal discount;

    /**
     * Identificador único de la ubicación asociada al ticket.
     * <p>
     * Este campo indica en qué ubicación se ha generado el ticket y es obligatorio.
     * </p>
     * Ejemplo: `5` (referencia a un ID de la tabla de ubicaciones)
     */
    @NotNull(message = "El ID de la ubicación no puede estar vacío.")
    private Long locationId;

    /**
     * Lista de identificadores de los productos asociados al ticket.
     * <p>
     * Cada identificador corresponde a un producto que forma parte del ticket. Este campo es opcional.
     * </p>
     * Ejemplo: `[1, 2, 3]` (referencias a IDs de la tabla de productos)
     */
    private List<Long> productIds;
}
