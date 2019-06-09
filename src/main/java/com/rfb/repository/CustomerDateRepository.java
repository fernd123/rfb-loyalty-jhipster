package com.rfb.repository;

import com.rfb.domain.CustomerDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CustomerDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerDateRepository extends JpaRepository<CustomerDate, Long> {

}
