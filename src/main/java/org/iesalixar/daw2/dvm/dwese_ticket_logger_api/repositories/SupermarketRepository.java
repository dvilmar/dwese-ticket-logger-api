package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;


import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {


    boolean existsByName(String code);
    @Query("SELECT COUNT(s) > 0 FROM Supermarket s WHERE s.name = :name AND s.id != :id")
    boolean existsSupermarketByNameAndNotId(@Param("name") String name, @Param("id") Long id);

}
