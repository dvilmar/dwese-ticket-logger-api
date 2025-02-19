package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa un ticket con información detallada.
 *
 * Este DTO se utiliza para transferir datos completos de un ticket entre las capas
 * de la aplicación, especialmente para exponerlos a través de la API.
 */
@Data
public class TicketDTO {

    /**
     * Identificador único del ticket.
     * <p>
     * Este campo corresponde al identificador principal (ID) del ticket en la base de datos.
     * </p>
     * Ejemplo: `101`, `202`, `303`.
     */
    private Long id;

    /**
     * Fecha del ticket.
     * <p>
     * Representa el momento en que se generó el ticket.
     * </p>
     * Ejemplo: `2025-01-15T10:30:00`.
     */
    private Date date;

    /**
     * Descuento aplicado al ticket.
     * <p>
     * Indica el porcentaje de descuento aplicado al total del ticket.
     * </p>
     * Ejemplo: `10.00` (10% de descuento).
     */
    private BigDecimal discount;

    /**
     * Total calculado del ticket.
     * <p>
     * Representa la suma de los precios de los productos asociados, menos el descuento.
     * </p>
     * Ejemplo: `45.75`.
     */
    private BigDecimal total;

    /**
     * Información de la ubicación asociada al ticket.
     * <p>
     * Este campo incluye un objeto de tipo {@link LocationDTO} con los datos de la ubicación
     * donde se generó el ticket.
     * </p>
     * Ejemplo:
     * ```json
     * {
     *   "id": 1,
     *   "address": "Calle Gran Vía, 10",
     *   "city": "Madrid",
     *   "supermarket": {
     *     "id": 1,
     *     "name": "Carrefour"
     *   },
     *   "province": {
     *     "id": 1,
     *     "name": "Madrid",
     *     "code": "MD",
     *     "region": {
     *       "id": 1,
     *       "name": "Comunidad de Madrid"
     *     }
     *   }
     * }
     * ```
     */
    private LocationDTO location;

    /**
     * Lista de productos asociados al ticket.
     * <p>
     * Cada producto está representado por un objeto de tipo {@link ProductDTO},
     * que incluye detalles como el nombre y el precio.
     * </p>
     * Ejemplo:
     * ```json
     * [
     *   {
     *     "id": 1,
     *     "name": "Pan",
     *     "price": 1.5
     *   },
     *   {
     *     "id": 2,
     *     "name": "Leche",
     *     "price": 2.0
     *   }
     * ]
     * ```
     */
    private List<ProductDTO> products;
}
