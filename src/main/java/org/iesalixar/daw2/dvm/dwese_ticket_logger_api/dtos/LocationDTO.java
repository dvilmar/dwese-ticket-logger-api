package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) que representa una ubicación con información completa.
 *
 * Este DTO se utiliza para transferir datos de una ubicación entre las capas de la
 * aplicación, especialmente para exponer información relevante de una ubicación a
 * través de la API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    /**
     * Identificador único de la ubicación.
     * <p>
     * Este campo corresponde al identificador principal (ID) de la entidad
     * `Location` en la base de datos.
     * </p>
     * Ejemplo: 101, 202, 303.
     */
    private Long id;

    /**
     * Dirección de la ubicación.
     * <p>
     * Representa la dirección completa donde se encuentra la ubicación.
     * </p>
     * Ejemplo: "Calle Gran Vía, 21", "Avenida de Andalucía, 45".
     */
    private String address;

    /**
     * Ciudad de la ubicación.
     * <p>
     * Indica la ciudad donde está situada la ubicación.
     * </p>
     * Ejemplo: "Madrid", "Sevilla", "Granada".
     */
    private String city;

    /**
     * Información básica del supermercado asociado.
     * <p>
     * Representa el supermercado relacionado con la ubicación. Este campo
     * incluye un objeto de tipo {@link SupermarketDTO}, que contiene datos
     * como el nombre del supermercado.
     * </p>
     */
    private SupermarketDTO supermarket;

    /**
     * Información básica de la provincia asociada.
     * <p>
     * Representa la provincia a la que pertenece la ubicación. Este campo
     * incluye un objeto de tipo {@link ProvinceDTO}, que contiene detalles
     * como el nombre y el código de la provincia.
     * </p>
     */
    private ProvinceDTO province;
}
