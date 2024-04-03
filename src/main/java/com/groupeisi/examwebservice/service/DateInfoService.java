package com.groupeisi.examwebservice.service;

import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.groupeisi.examwebservice.domain.DateInfo}.
 */
public interface DateInfoService {
    /**
     * Save a dateInfo.
     *
     * @param dateInfoDTO the entity to save.
     * @return the persisted entity.
     */
    DateInfoDTO save(DateInfoDTO dateInfoDTO);

    /**
     * Updates a dateInfo.
     *
     * @param dateInfoDTO the entity to update.
     * @return the persisted entity.
     */
    DateInfoDTO update(DateInfoDTO dateInfoDTO);

    /**
     * Partially updates a dateInfo.
     *
     * @param dateInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DateInfoDTO> partialUpdate(DateInfoDTO dateInfoDTO);

    /**
     * Get all the dateInfos.
     *
     * @return the list of entities.
     */
    List<DateInfoDTO> findAll();

    /**
     * Get the "id" dateInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DateInfoDTO> findOne(Long id);

    /**
     * Delete the "id" dateInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    /**
     * Find the day of the week for a given date string.
     *
     * @param dateString The date string in the format "dd-MM-yyyy".
     * @return The DTO with the date and the corresponding day of the week.
     */
    DateInfoDTO findDayOfWeek(String dateString);

}
