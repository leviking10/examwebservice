package com.groupeisi.examwebservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.groupeisi.examwebservice.IntegrationTest;
import com.groupeisi.examwebservice.domain.DateInfo;
import com.groupeisi.examwebservice.repository.DateInfoRepository;
import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
import com.groupeisi.examwebservice.service.mapper.DateInfoMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DateInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DateInfoResourceIT {

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_DAY_OF_WEEK = "AAAAAAAAAA";
    private static final String UPDATED_DAY_OF_WEEK = "BBBBBBBBBB";

    private static final String DEFAULT_D = "AAAAAAAAAA";
    private static final String UPDATED_D = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/date-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DateInfoRepository dateInfoRepository;

    @Autowired
    private DateInfoMapper dateInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDateInfoMockMvc;

    private DateInfo dateInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DateInfo createEntity(EntityManager em) {
        DateInfo dateInfo = new DateInfo().date(DEFAULT_DATE).dayOfWeek(DEFAULT_DAY_OF_WEEK);
        return dateInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DateInfo createUpdatedEntity(EntityManager em) {
        DateInfo dateInfo = new DateInfo().date(UPDATED_DATE).dayOfWeek(UPDATED_DAY_OF_WEEK);
        return dateInfo;
    }

    @BeforeEach
    public void initTest() {
        dateInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createDateInfo() throws Exception {
        int databaseSizeBeforeCreate = dateInfoRepository.findAll().size();
        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);
        restDateInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dateInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeCreate + 1);
        DateInfo testDateInfo = dateInfoList.get(dateInfoList.size() - 1);
        assertThat(testDateInfo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDateInfo.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void createDateInfoWithExistingId() throws Exception {
        // Create the DateInfo with an existing ID
        dateInfo.setId(1L);
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        int databaseSizeBeforeCreate = dateInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDateInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dateInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDateInfos() throws Exception {
        // Initialize the database
        dateInfoRepository.saveAndFlush(dateInfo);

        // Get all the dateInfoList
        restDateInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dateInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].d").value(hasItem(DEFAULT_D)));
    }

    @Test
    @Transactional
    void getDateInfo() throws Exception {
        // Initialize the database
        dateInfoRepository.saveAndFlush(dateInfo);

        // Get the dateInfo
        restDateInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, dateInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dateInfo.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK))
            .andExpect(jsonPath("$.d").value(DEFAULT_D));
    }

    @Test
    @Transactional
    void getNonExistingDateInfo() throws Exception {
        // Get the dateInfo
        restDateInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDateInfo() throws Exception {
        // Initialize the database
        dateInfoRepository.saveAndFlush(dateInfo);

        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();

        // Update the dateInfo
        DateInfo updatedDateInfo = dateInfoRepository.findById(dateInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDateInfo are not directly saved in db
        em.detach(updatedDateInfo);
        updatedDateInfo.date(UPDATED_DATE).dayOfWeek(UPDATED_DAY_OF_WEEK);
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(updatedDateInfo);

        restDateInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dateInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dateInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
        DateInfo testDateInfo = dateInfoList.get(dateInfoList.size() - 1);
        assertThat(testDateInfo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDateInfo.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void putNonExistingDateInfo() throws Exception {
        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();
        dateInfo.setId(longCount.incrementAndGet());

        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDateInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dateInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dateInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDateInfo() throws Exception {
        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();
        dateInfo.setId(longCount.incrementAndGet());

        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dateInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDateInfo() throws Exception {
        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();
        dateInfo.setId(longCount.incrementAndGet());

        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dateInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDateInfoWithPatch() throws Exception {
        // Initialize the database
        dateInfoRepository.saveAndFlush(dateInfo);

        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();

        // Update the dateInfo using partial update
        DateInfo partialUpdatedDateInfo = new DateInfo();
        partialUpdatedDateInfo.setId(dateInfo.getId());


        restDateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDateInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDateInfo))
            )
            .andExpect(status().isOk());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
        DateInfo testDateInfo = dateInfoList.get(dateInfoList.size() - 1);
        assertThat(testDateInfo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDateInfo.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);

    }

    @Test
    @Transactional
    void fullUpdateDateInfoWithPatch() throws Exception {
        // Initialize the database
        dateInfoRepository.saveAndFlush(dateInfo);

        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();

        // Update the dateInfo using partial update
        DateInfo partialUpdatedDateInfo = new DateInfo();
        partialUpdatedDateInfo.setId(dateInfo.getId());

        partialUpdatedDateInfo.date(UPDATED_DATE).dayOfWeek(UPDATED_DAY_OF_WEEK);

        restDateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDateInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDateInfo))
            )
            .andExpect(status().isOk());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
        DateInfo testDateInfo = dateInfoList.get(dateInfoList.size() - 1);
        assertThat(testDateInfo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDateInfo.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);

    }

    @Test
    @Transactional
    void patchNonExistingDateInfo() throws Exception {
        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();
        dateInfo.setId(longCount.incrementAndGet());

        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dateInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dateInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDateInfo() throws Exception {
        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();
        dateInfo.setId(longCount.incrementAndGet());

        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dateInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDateInfo() throws Exception {
        int databaseSizeBeforeUpdate = dateInfoRepository.findAll().size();
        dateInfo.setId(longCount.incrementAndGet());

        // Create the DateInfo
        DateInfoDTO dateInfoDTO = dateInfoMapper.toDto(dateInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dateInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DateInfo in the database
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDateInfo() throws Exception {
        // Initialize the database
        dateInfoRepository.saveAndFlush(dateInfo);

        int databaseSizeBeforeDelete = dateInfoRepository.findAll().size();

        // Delete the dateInfo
        restDateInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dateInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DateInfo> dateInfoList = dateInfoRepository.findAll();
        assertThat(dateInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
