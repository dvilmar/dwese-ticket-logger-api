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
public class ProductDTO {
    private Long id;
    private String name;
    private String image;
    private ParentCategoryDTO parentCategory;
}

