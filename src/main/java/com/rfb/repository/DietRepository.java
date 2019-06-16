package com.rfb.repository;

import com.rfb.domain.Diet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Diet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietRepository extends JpaRepository<Diet, Long>, JpaSpecificationExecutor<Diet> {

}
