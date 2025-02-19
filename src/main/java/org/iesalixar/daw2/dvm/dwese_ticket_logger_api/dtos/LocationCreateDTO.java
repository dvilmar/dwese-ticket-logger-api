package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para la creación de una nueva ubicación.
 *
 * Este DTO se utiliza para transferir los datos necesarios para crear una ubicación
 * en la base de datos a través de la API. Incluye validaciones para garantizar que
 * los campos obligatorios estén presentes.
 */
@Data
public class LocationCreateDTO {

    /**
     * Dirección de la ubicación.
     * <p>
     * Representa la dirección completa donde se encuentra la nueva ubicación.
     * Este campo es obligatorio y no puede estar vacío.
     * </p>
     * Ejemplo: "Calle Mayor, 10", "Avenida de la Constitución, 15".
     */
    @NotEmpty(message = "La dirección no puede estar vacía.")
    private String address;

    /**
     * Ciudad de la ubicación.
     * <p>
     * Indica la ciudad donde estará situada la nueva ubicación.
     * Este campo es obligatorio y no puede estar vacío.
     * </p>
     * Ejemplo: "Valencia", "Barcelona", "Sevilla".
     */
    @NotEmpty(message = "La ciudad no puede estar vacía.")
    private String city;

    /**
     * Identificador del supermercado asociado.
     * <p>
     * Este campo es obligatorio y debe contener el ID de un supermercado existente
     * al que pertenecerá la nueva ubicación.
     * </p>
     * Ejemplo: 1, 2, 3.
     */
    @NotNull(message = "El ID del supermercado no puede ser nulo.")
    private Long supermarketId;

    /**
     * Identificador de la provincia asociada.
     * <p>
     * Este campo es obligatorio y debe contener el ID de una provincia existente
     * a la que pertenecerá la nueva ubicación.
     * </p>
     * Ejemplo: 101, 202, 303.
     */
    @NotNull(message = "El ID de la provincia no puede ser nulo.")
    private Long provinceId;
}
