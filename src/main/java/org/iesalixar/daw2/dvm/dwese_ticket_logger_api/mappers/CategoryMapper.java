package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.CategoryCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.CategoryDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ParentCategoryDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    /**
     * Convierte una entidad Category en un CategoryDTO.
     *
     * @param category La entidad Category a convertir.
     * @return Un CategoryDTO con los datos de la entidad.
     */
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setImage(category.getImage());

        // Si la categoría tiene un padre, convertir a ParentCategoryDTO
        if (category.getParentCategory() != null) {
            categoryDTO.setParentCategory(toParentDTO(category.getParentCategory()));
        }

        return categoryDTO;
    }

    /**
     * Convierte una entidad Category en un ParentCategoryDTO.
     *
     * @param parentCategory La entidad Category que representa la categoría padre.
     * @return Un ParentCategoryDTO con los datos básicos de la categoría padre.
     */
    public ParentCategoryDTO toParentDTO(Category parentCategory) {
        if (parentCategory == null) {
            return null;
        }

        ParentCategoryDTO parentCategoryDTO = new ParentCategoryDTO();
        parentCategoryDTO.setId(parentCategory.getId());
        parentCategoryDTO.setName(parentCategory.getName());
        parentCategoryDTO.setImage(parentCategory.getImage());

        return parentCategoryDTO;
    }

    /**
     * Convierte un CategoryCreateDTO en una entidad Category.
     *
     * @param createDTO El DTO con los datos para crear la categoría.
     * @param parentCategory La categoría padre asociada, si existe.
     * @return Una entidad Category con los datos del DTO.
     */
    public Category toEntity(CategoryCreateDTO createDTO, Category parentCategory) {
        if (createDTO == null) {
            return null;
        }

        Category category = new Category();
        category.setName(createDTO.getName());
        category.setParentCategory(parentCategory);

        return category;
    }
}

