package com.groupeisi.examwebservice.web.rest;

import com.groupeisi.examwebservice.repository.SearchHistoryRepository;
import com.groupeisi.examwebservice.service.SearchHistoryService;
import com.groupeisi.examwebservice.service.dto.SearchHistoryDTO;
import com.groupeisi.examwebservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.groupeisi.examwebservice.domain.SearchHistory}.
 */
@RestController
@RequestMapping("/api/search-histories")
public class SearchHistoryResource {

    private final Logger log = LoggerFactory.getLogger(SearchHistoryResource.class);

    private static final String ENTITY_NAME = "examWebServiceSearchHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchHistoryService searchHistoryService;

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryResource(SearchHistoryService searchHistoryService, SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryService = searchHistoryService;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    /**
     * {@code POST  /search-histories} : Create a new searchHistory.
     *
     * @param searchHistoryDTO the searchHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchHistoryDTO, or with status {@code 400 (Bad Request)} if the searchHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchHistoryDTO> createSearchHistory(@RequestBody SearchHistoryDTO searchHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save SearchHistory : {}", searchHistoryDTO);
        if (searchHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SearchHistoryDTO result = searchHistoryService.save(searchHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/search-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /search-histories/:id} : Updates an existing searchHistory.
     *
     * @param id the id of the searchHistoryDTO to save.
     * @param searchHistoryDTO the searchHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the searchHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchHistoryDTO> updateSearchHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SearchHistoryDTO searchHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SearchHistory : {}, {}", id, searchHistoryDTO);
        if (searchHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SearchHistoryDTO result = searchHistoryService.update(searchHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /search-histories/:id} : Partial updates given fields of an existing searchHistory, field will ignore if it is null
     *
     * @param id the id of the searchHistoryDTO to save.
     * @param searchHistoryDTO the searchHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the searchHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchHistoryDTO> partialUpdateSearchHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SearchHistoryDTO searchHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SearchHistory partially : {}, {}", id, searchHistoryDTO);
        if (searchHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchHistoryDTO> result = searchHistoryService.partialUpdate(searchHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-histories} : get all the searchHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchHistories in body.
     */
    @GetMapping("")
    public List<SearchHistoryDTO> getAllSearchHistories() {
        log.debug("REST request to get all SearchHistories");
        return searchHistoryService.findAll();
    }

    /**
     * {@code GET  /search-histories/:id} : get the "id" searchHistory.
     *
     * @param id the id of the searchHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchHistoryDTO> getSearchHistory(@PathVariable("id") Long id) {
        log.debug("REST request to get SearchHistory : {}", id);
        Optional<SearchHistoryDTO> searchHistoryDTO = searchHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchHistoryDTO);
    }

    /**
     * {@code DELETE  /search-histories/:id} : delete the "id" searchHistory.
     *
     * @param id the id of the searchHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchHistory(@PathVariable("id") Long id) {
        log.debug("REST request to delete SearchHistory : {}", id);
        searchHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
