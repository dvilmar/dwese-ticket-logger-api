package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.CategoryRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Category;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Controlador que maneja las operaciones CRUD para la entidad `Category`.
 * Utiliza `CategoryDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {


    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);


    // DAO para gestionar las operaciones de las categorías en la base de datos
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Lista todas las categorías y las pasa como atributo al modelo para que sean
     * accesibles en la vista `category.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de categorías.
     */
    @GetMapping
    public String listCategories(Model model) {
        logger.info("Solicitando la lista de todas las categorías...");
        List<Category> listCategories = null;
        listCategories = categoryRepository.findAll();
        logger.info("Se han cargado {} categorías.", listCategories.size());
        model.addAttribute("listCategories", listCategories); // Pasar la lista de categorías al modelo
        return "category"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear una nueva categoría.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva categoría.");
        model.addAttribute("category", new Category()); // Crear un nuevo objeto Category
        model.addAttribute("parents", categoryRepository.findAll()); // Lista de categorías
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar una categoría existente.
     *
     * @param id    ID de la categoría a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la categoría con ID {}", id);
        Optional<Category> category = categoryRepository.findById(id);;
        List<Category> parentCategories = categoryRepository.findAll();
        model.addAttribute("category", category.get());
        model.addAttribute("parents", parentCategories); // Lista de categorías
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva categoría en la base de datos.
     *
     * @param category              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de categorías.
     */
    @PostMapping("/insert")
    public String insertCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        logger.info("Insertando nueva categoría");
        // Verificar si hay errores de validación
        if (result.hasErrors()) {
            model.addAttribute("parents", categoryRepository.findAll()); // Lista de categorías
            return "category-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        // Verificar si ya existe una categoría con el mismo nombre
        if (categoryRepository.existsByName(category.getName())) {
            logger.warn("El nombre de la categoría {} ya existe.", category.getName());
            String errorMessage = messageSource.getMessage("msg.category-controller.insert.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/add"; // Redirige al formulario de inserción con el error
        }
        // Si el campo 'parent' es null o no válido, asignar null a 'parent'
        if (category.getParent() != null && category.getParent().getId() == null) {
            category.setParent(null); // Aseguramos que 'parent' es null si no se seleccionó categoría
        }
        // Verificar si se ha subido una imagen
        if (imageFile != null && !imageFile.isEmpty()) {
            // Guardar la imagen en el sistema de archivos
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName); // Guardar el nombre del archivo en la entidad
            }
        }
        categoryRepository.save(category);
        logger.info("Categoría con nombre '{}' insertada con éxito.", category.getName());
        return "redirect:/categories";
    }


    /**
     * Actualiza una categoría existente en la base de datos.
     *
     * @param category              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de categorías.
     */
    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale,  @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        logger.info("Actualizando categoría con ID {}", category.getId());
        if (result.hasErrors()) {
            model.addAttribute("parents", categoryRepository.findAll()); // Lista de categorías
            return "category-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (categoryRepository.existsCategoryByNameAndNotId(category.getName(), category.getId())) {
            logger.warn("El nombre de la categoría {} ya existe para otra categoría.", category.getName());
            String errorMessage = messageSource.getMessage("msg.category-controller.update.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/edit?id=" + category.getId();
        }
        // Si el campo 'parent' es null o una categoría no válida, asignar null a 'parent'
        if (category.getParent() != null && category.getParent().getId() == null) {
            category.setParent(null); // Aseguramos que 'parent' es null si no se seleccionó categoría
        }
        // Guardar la imagen subida
        if (imageFile != null && !imageFile.isEmpty()) {
            // Eliminar la imagen anterior si existe
            if (category.getImage() != null && !category.getImage().isEmpty()) {
                fileStorageService.deleteFile(category.getImage()); // Eliminar la imagen anterior
            }
            // Guardar la nueva imagen
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName); // Guardar el nombre del archivo en la entidad
            }
        }
        categoryRepository.save(category);
        logger.info("Categoría con ID {} actualizada con éxito.", category.getId());
        return "redirect:/categories"; // Redirigir a la lista de categorías
    }


    /**
     * Elimina una categoría de la base de datos.
     *
     * @param id                 ID de la categoría a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de categorías.
     */
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando categoría con ID {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        categoryRepository.deleteById(id);
        // Eliminar la imagen asociada, si existe
        if (category.get().getImage() != null && !category.get().getImage().isEmpty()) {
            fileStorageService.deleteFile(category.get().getImage());
        }
        logger.info("Categoría con ID {} eliminada con éxito.", id);
        return "redirect:/categories"; // Redirigir a la lista de categorías
    }
}