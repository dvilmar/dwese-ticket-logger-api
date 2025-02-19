package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) utilizado para la creación de nuevas regiones.
 *
 * Este DTO asegura que los datos necesarios para crear una región
 * sean válidos y cumplan con las restricciones definidas, como no permitir valores nulos
 * o limitar el tamaño de los campos.
 *
 * Específicamente diseñado para recibir datos de entrada en peticiones HTTP POST
 * en la API.
 */
@Getter
@Setter
public class RegionCreateDTO {

    /**
     * Código único de la región.
     *
     * - No puede estar vacío (`@NotEmpty`).
     * - Longitud máxima de 2 caracteres (`@Size(max = 2)`).
     *
     * Ejemplo: "01" para Andalucía, "13" para Madrid.
     */
    @NotEmpty(message = "{msg.region.code.notEmpty}")
    @Size(max = 2, message = "{msg.region.code.size}")
    private String code;

    /**
     * Nombre completo de la región.
     *
     * - No puede estar vacío (`@NotEmpty`).
     * - Longitud máxima de 100 caracteres (`@Size(max = 100)`).
     *
     * Ejemplo: "Andalucía", "Cataluña", "Galicia".
     */
    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.size}")
    private String name;
}
