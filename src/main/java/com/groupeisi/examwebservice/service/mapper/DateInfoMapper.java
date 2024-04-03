package com.groupeisi.examwebservice.service.mapper;

import com.groupeisi.examwebservice.domain.DateInfo;
import com.groupeisi.examwebservice.domain.SearchHistory;
import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
import com.groupeisi.examwebservice.service.dto.SearchHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DateInfo} and its DTO {@link DateInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DateInfoMapper extends EntityMapper<DateInfoDTO, DateInfo> {
    @Mapping(target = "searchHistory", source = "searchHistory", qualifiedByName = "searchHistoryId")
    DateInfoDTO toDto(DateInfo s);

    @Named("searchHistoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SearchHistoryDTO toDtoSearchHistoryId(SearchHistory searchHistory);
}
