package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.CategoryCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.CategoryDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Category;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.CategoryMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.CategoryRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FileStorageService fileStorageService;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDTO);
    }

    public CategoryDTO createCategory(CategoryCreateDTO createDTO) {
        logger.info("Creando una nueva categoría con nombre {}", createDTO.getName());
        // Verificar si ya existe una nueva categoría con el mismo nombre
        if (categoryRepository.existsByName(createDTO.getName())) {
            throw new IllegalArgumentException("El nombre de la categoría ya existe.");
        }
        // Obtener la categoría padre si está presente
        Category parentCategory = null;
        if (createDTO.getParentCategoryId() != null) {
            parentCategory =  categoryRepository.findById(createDTO.getParentCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría padre no existe."));
        }
        // Procesar la imagen si se proporciona
        String filename = null;
        if (createDTO.getImageFile() != null) {
            parentCategory = categoryRepository.findById(createDTO.getParentCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría padre no existe."));
        }
        // Crear la entidad Category
        Category category = categoryMapper.toEntity(createDTO, parentCategory);
        category.setImage(filename);
        // Guardar la nueva categoría
        Category savedCategory = categoryRepository.save(category);
        logger.info("Categoría creada exitosamente con ID {}", savedCategory.getId());
        // Convertir la entidad guardada a DTO y devolverla
        return categoryMapper.toDTO(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, CategoryCreateDTO updateDTO) {
        logger.info("Actualizando categoría con ID {}", id);
        // Buscar la categoría existente
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));
        // Verificar si el nombre ya está en uso por otra categoría
        if (categoryRepository.existsCategoryByNameAndNotId(updateDTO.getName(), id)) {
            throw new IllegalArgumentException("El nombre de la categoría ya está en uso.");
        }
        // Obtener la categoría padre si está presente
        Category parentCategory = null;
        if (updateDTO.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(updateDTO.getParentCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría padre no existe."));
        }
        // Procesar la imagen si se proporciona
        String fileName = existingCategory.getImage();
        if (updateDTO.getImageFile() != null && !updateDTO.getImageFile().isEmpty()) {
            fileName = fileStorageService.saveFile(updateDTO.getImageFile());
            if (fileName == null) {
                throw new RuntimeException("Error al guardar la nueva imagen.");
            }
        }
        // Actualizar los datos de la categoría
        existingCategory.setName(updateDTO.getName());
        existingCategory.setParentCategory(parentCategory);
        existingCategory.setImage(fileName);
        // Guardar los cambios
        Category updatedCategory = categoryRepository.save(existingCategory);
        logger.info("Categoría con ID {} actualizada exitosamente.", updatedCategory.getId());
        // Convertir la entidad actualizada a DTO y devolverla
        return categoryMapper.toDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        logger.info("Buscando categoría con ID {}", id);

        // Buscando la categoría
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));

        // Eliminar la imagen asociada si existe
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            fileStorageService.deleteFile(category.getImage());
            logger.info("Imagen asociada a la categoría con ID {} eliminada.", id);
        }

        // Eliminar la categoria
        categoryRepository.deleteById(id);
        logger.info("Categoría con ID {} eliminada exitosamente.", id);
    }
}
