package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.RegionRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Controlador que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/provinces")
public class ProvinceController {


    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);


    // DAO para gestionar las operaciones de las provincias en la base de datos
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RegionRepository regionRepository;


    /**
     * Lista todas las provincias y las pasa como atributo al modelo para que sean
     * accesibles en la vista `province.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de provincias.
     */
    @GetMapping
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = null;
        listProvinces = provinceRepository.findAll();
        logger.info("Se han cargado {} provincias.", listProvinces.size());
        model.addAttribute("listProvinces", listProvinces); // Pasar la lista de provincias al modelo
        return "province"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear una nueva provincia.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");
        model.addAttribute("province", new Province()); // Crear un nuevo objeto Province
        model.addAttribute("regions", regionRepository.findAll()); // Lista de regiones
        return "province-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar una provincia existente.
     *
     * @param id    ID de la provincia a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Optional<Province> province = provinceRepository.findById(id);
        List<Region> regions = regionRepository.findAll();
        model.addAttribute("province", province.get());
        model.addAttribute("regions", regions); // Lista de regiones
        return "province-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva provincia en la base de datos.
     *
     * @param province              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/insert")
    public String insertProvince(@Valid @ModelAttribute("province") Province province, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        if (result.hasErrors()) {
            model.addAttribute("regions", regionRepository.findAll()); // Lista de regiones
            return "province-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (provinceRepository.existsByCode(province.getCode())) {
            logger.warn("El código de la provincia {} ya existe.", province.getCode());
            String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/provinces/new";
        }
        provinceRepository.save(province);
        logger.info("Provincia {} insertada con éxito.", province.getCode());
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }


    /**
     * Actualiza una provincia existente en la base de datos.
     *
     * @param province              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/update")
    public String updateProvince(@Valid @ModelAttribute("province") Province province, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {
        logger.info("Actualizando provincia con ID {}", province.getId());
        if (result.hasErrors()) {
            model.addAttribute("regions", regionRepository.findAll()); // Lista de regiones
            return "province-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (provinceRepository.existsProvinceByCodeAndNotId(province.getCode(), province.getId())) {
            logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
            String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/provinces/edit?id=" + province.getId();
        }
        provinceRepository.save(province);
        logger.info("Provincia con ID {} actualizada con éxito.", province.getId());
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }


    /**
     * Elimina una provincia de la base de datos.
     *
     * @param id                 ID de la provincia a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/delete")
    public String deleteProvince(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando provincia con ID {}", id);
      try {
          provinceRepository.deleteById(id);
      } catch (RuntimeException e) {
          redirectAttributes.addFlashAttribute("errorMessage", "msg.province-controller.delete.error");
          return "redirect:/provinces";
      }
        logger.info("Provincia con ID {} eliminada con éxito.", id);
        return "redirect:/provinces"; // Redirigir a la lista de provincias
    }
}
