package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) que representa un supermercado con información básica.
 *
 * Este DTO se utiliza para transferir datos de un supermercado entre las capas de la
 * aplicación, especialmente para exponerlos a través de la API sin incluir información
 * innecesaria o sensible.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupermarketDTO {

    /**
     * Identificador único del supermercado.
     * <p>
     * Este campo corresponde al identificador principal (ID) de la entidad
     * `Supermarket` en la base de datos.
     * </p>
     * Ejemplo: 1, 2, 3.
     */
    private Long id;

    /**
     * Nombre del supermercado.
     * <p>
     * Representa el nombre del supermercado tal como aparece en el sistema.
     * </p>
     * Ejemplo: "Mercadona", "Carrefour", "Lidl".
     */
    private String name;
}
