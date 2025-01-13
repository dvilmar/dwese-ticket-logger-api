package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase DTO (Data Transfer Object) que representa una categoría.
 *
 * Esta clase se utiliza para transferir datos de una categoría
 * entre las capas de la aplicación, especialmente para exponerlos
 * a través de la API sin incluir información innecesaria o sensible.
 */
@Getter
@Setter
public class CategoryDTO {

    /**
     * Identificador único de la categoría.
     * Es el mismo ID que se encuentra en la entidad `Category` de la base de datos.
     */
    private Long id;

    /**
     * Nombre de la categoría.
     * Ejemplo: "Electrónica", "Ropa".
     */
    private String name;

    /**
     * Enlace a la imagen asociada con la categoría.
     * Es una URL o ruta a la imagen.
     */
    private String image;

    /**
     * Categoría padre de la categoría actual.
     * Contiene información resumida de la categoría padre.
     */
    private ParentCategoryDTO parentCategory;
}

