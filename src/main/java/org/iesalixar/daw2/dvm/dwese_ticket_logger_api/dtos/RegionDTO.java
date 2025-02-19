package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO (Data Transfer Object) que representa una región.
 *
 * Esta clase se utiliza para transferir datos de una región
 * entre las capas de la aplicación, especialmente para exponerlos
 * a través de la API sin incluir información innecesaria o sensible.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {

    /**
     * Identificador único de la región.
     * Es el mismo ID que se encuentra en la entidad `Region` de la base de datos.
     */
    private Long id;

    /**
     * Código de la región.
     * Normalmente es una cadena corta (máximo 2 caracteres) que identifica la región.
     * Ejemplo: "01" para Andalucía.
     */
    private String code;

    /**
     * Nombre completo de la región.
     * Ejemplo: "Andalucía", "Cataluña".
     */
    private String name;
}
