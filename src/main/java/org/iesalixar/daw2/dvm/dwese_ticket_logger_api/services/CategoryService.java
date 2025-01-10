package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

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

}
