package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    boolean existsByName(String address);
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND c.id != :id")
    boolean existsCategoryByNameAndNotId(@Param("name") String name, @Param("id") Long id);

}
