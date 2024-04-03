package com.groupeisi.examwebservice.service.mapper;

import com.groupeisi.examwebservice.domain.SearchHistory;
import com.groupeisi.examwebservice.service.dto.SearchHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchHistory} and its DTO {@link SearchHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchHistoryMapper extends EntityMapper<SearchHistoryDTO, SearchHistory> {}
