package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase DTO (Data Transfer Object) que representa una categoría padre de forma resumida.
 *
 * Esta clase se utiliza para transferir datos de una categoría padre
 * entre las capas de la aplicación, especialmente para exponer información básica
 * sin incluir detalles innecesarios o generar ciclos.
 */
@Getter
@Setter
public class ParentCategoryDTO {

    /**
     * Identificador único de la categoría padre.
     * Es el mismo ID que se encuentra en la entidad `Category` de la base de datos.
     */
    private Long id;

    /**
     * Nombre de la categoría padre.
     * Ejemplo: "Electrónica", "Ropa".
     */
    private String name;

    /**
     * Enlace a la imagen asociada con la categoría padre.
     * Es una URL o ruta a la imagen.
     */
    private String image;
}
