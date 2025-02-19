package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) que representa un producto con información básica.
 *
 * Este DTO se utiliza para transferir los datos de un producto entre las capas de la aplicación,
 * especialmente en las respuestas de la API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    /**
     * Identificador único del producto.
     * <p>
     * Este campo corresponde al identificador principal (ID) de la entidad `Product` en la base de datos.
     * </p>
     * Ejemplo: 1, 2, 3.
     */
    private Long id;

    /**
     * Nombre del producto.
     * <p>
     * Representa el nombre del producto tal como aparece en el sistema.
     * </p>
     * Ejemplo: "Pan", "Leche", "Arroz".
     */
    private String name;

    /**
     * Precio del producto.
     * <p>
     * Representa el precio actual del producto.
     * </p>
     * Ejemplo: 1.50, 2.75, 3.99.
     */
    private BigDecimal price;
}
