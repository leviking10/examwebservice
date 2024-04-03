package com.groupeisi.examwebservice.repository;

import com.groupeisi.examwebservice.domain.DateInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DateInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DateInfoRepository extends JpaRepository<DateInfo, Long> {}
