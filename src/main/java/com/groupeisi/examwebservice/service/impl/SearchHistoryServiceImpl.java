package com.groupeisi.examwebservice.service.impl;

import com.groupeisi.examwebservice.domain.SearchHistory;
import com.groupeisi.examwebservice.repository.SearchHistoryRepository;
import com.groupeisi.examwebservice.service.SearchHistoryService;
import com.groupeisi.examwebservice.service.dto.SearchHistoryDTO;
import com.groupeisi.examwebservice.service.mapper.SearchHistoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.groupeisi.examwebservice.domain.SearchHistory}.
 */
@Service
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final Logger log = LoggerFactory.getLogger(SearchHistoryServiceImpl.class);

    private final SearchHistoryRepository searchHistoryRepository;

    private final SearchHistoryMapper searchHistoryMapper;

    public SearchHistoryServiceImpl(SearchHistoryRepository searchHistoryRepository, SearchHistoryMapper searchHistoryMapper) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.searchHistoryMapper = searchHistoryMapper;
    }

    @Override
    public SearchHistoryDTO save(SearchHistoryDTO searchHistoryDTO) {
        log.debug("Request to save SearchHistory : {}", searchHistoryDTO);
        SearchHistory searchHistory = searchHistoryMapper.toEntity(searchHistoryDTO);
        searchHistory = searchHistoryRepository.save(searchHistory);
        return searchHistoryMapper.toDto(searchHistory);
    }

    @Override
    public SearchHistoryDTO update(SearchHistoryDTO searchHistoryDTO) {
        log.debug("Request to update SearchHistory : {}", searchHistoryDTO);
        SearchHistory searchHistory = searchHistoryMapper.toEntity(searchHistoryDTO);
        searchHistory = searchHistoryRepository.save(searchHistory);
        return searchHistoryMapper.toDto(searchHistory);
    }

    @Override
    public Optional<SearchHistoryDTO> partialUpdate(SearchHistoryDTO searchHistoryDTO) {
        log.debug("Request to partially update SearchHistory : {}", searchHistoryDTO);

        return searchHistoryRepository
            .findById(searchHistoryDTO.getId())
            .map(existingSearchHistory -> {
                searchHistoryMapper.partialUpdate(existingSearchHistory, searchHistoryDTO);

                return existingSearchHistory;
            })
            .map(searchHistoryRepository::save)
            .map(searchHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchHistoryDTO> findAll() {
        log.debug("Request to get all SearchHistories");
        return searchHistoryRepository.findAll().stream().map(searchHistoryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchHistoryDTO> findOne(Long id) {
        log.debug("Request to get SearchHistory : {}", id);
        return searchHistoryRepository.findById(id).map(searchHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SearchHistory : {}", id);
        searchHistoryRepository.deleteById(id);
    }
}
