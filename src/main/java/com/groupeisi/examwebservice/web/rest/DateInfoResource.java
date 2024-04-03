package com.groupeisi.examwebservice.web.rest;

import com.groupeisi.examwebservice.repository.DateInfoRepository;
import com.groupeisi.examwebservice.service.DateInfoService;
import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
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
 * REST controller for managing {@link com.groupeisi.examwebservice.domain.DateInfo}.
 */
@RestController
@RequestMapping("/api/date-infos")
public class DateInfoResource {

    private final Logger log = LoggerFactory.getLogger(DateInfoResource.class);

    private static final String ENTITY_NAME = "examWebServiceDateInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DateInfoService dateInfoService;

    private final DateInfoRepository dateInfoRepository;

    public DateInfoResource(DateInfoService dateInfoService, DateInfoRepository dateInfoRepository) {
        this.dateInfoService = dateInfoService;
        this.dateInfoRepository = dateInfoRepository;
    }

    /**
     * {@code POST  /date-infos} : Create a new dateInfo.
     *
     * @param dateInfoDTO the dateInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dateInfoDTO, or with status {@code 400 (Bad Request)} if the dateInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DateInfoDTO> createDateInfo(@RequestBody DateInfoDTO dateInfoDTO) throws URISyntaxException {
        log.debug("REST request to save DateInfo : {}", dateInfoDTO);
        if (dateInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new dateInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DateInfoDTO result = dateInfoService.save(dateInfoDTO);
        return ResponseEntity
            .created(new URI("/api/date-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /date-infos/:id} : Updates an existing dateInfo.
     *
     * @param id the id of the dateInfoDTO to save.
     * @param dateInfoDTO the dateInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dateInfoDTO,
     * or with status {@code 400 (Bad Request)} if the dateInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dateInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DateInfoDTO> updateDateInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DateInfoDTO dateInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DateInfo : {}, {}", id, dateInfoDTO);
        if (dateInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dateInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dateInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DateInfoDTO result = dateInfoService.update(dateInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dateInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /date-infos/:id} : Partial updates given fields of an existing dateInfo, field will ignore if it is null
     *
     * @param id the id of the dateInfoDTO to save.
     * @param dateInfoDTO the dateInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dateInfoDTO,
     * or with status {@code 400 (Bad Request)} if the dateInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dateInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dateInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DateInfoDTO> partialUpdateDateInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DateInfoDTO dateInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DateInfo partially : {}, {}", id, dateInfoDTO);
        if (dateInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dateInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dateInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DateInfoDTO> result = dateInfoService.partialUpdate(dateInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dateInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /date-infos} : get all the dateInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dateInfos in body.
     */
    @GetMapping("")
    public List<DateInfoDTO> getAllDateInfos() {
        log.debug("REST request to get all DateInfos");
        return dateInfoService.findAll();
    }

    /**
     * {@code GET  /date-infos/:id} : get the "id" dateInfo.
     *
     * @param id the id of the dateInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dateInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DateInfoDTO> getDateInfo(@PathVariable("id") Long id) {
        log.debug("REST request to get DateInfo : {}", id);
        Optional<DateInfoDTO> dateInfoDTO = dateInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dateInfoDTO);
    }

    /**
     * {@code DELETE  /date-infos/:id} : delete the "id" dateInfo.
     *
     * @param id the id of the dateInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDateInfo(@PathVariable("id") Long id) {
        log.debug("REST request to delete DateInfo : {}", id);
        dateInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
