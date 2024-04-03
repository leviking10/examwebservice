package com.groupeisi.examwebservice.repository;

import com.groupeisi.examwebservice.domain.SearchHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {}
