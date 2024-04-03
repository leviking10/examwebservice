package com.groupeisi.examwebservice.web.rest;

import static com.groupeisi.examwebservice.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.groupeisi.examwebservice.IntegrationTest;
import com.groupeisi.examwebservice.domain.SearchHistory;
import com.groupeisi.examwebservice.repository.SearchHistoryRepository;
import com.groupeisi.examwebservice.service.dto.SearchHistoryDTO;
import com.groupeisi.examwebservice.service.mapper.SearchHistoryMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SearchHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchHistoryResourceIT {

    private static final ZonedDateTime DEFAULT_SEARCH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEARCH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DATE_INFO = "AAAAAAAAAA";
    private static final String UPDATED_DATE_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/search-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchHistoryMockMvc;

    private SearchHistory searchHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchHistory createEntity(EntityManager em) {
        SearchHistory searchHistory = new SearchHistory().searchDate(DEFAULT_SEARCH_DATE).dateInfo(DEFAULT_DATE_INFO);
        return searchHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchHistory createUpdatedEntity(EntityManager em) {
        SearchHistory searchHistory = new SearchHistory().searchDate(UPDATED_SEARCH_DATE).dateInfo(UPDATED_DATE_INFO);
        return searchHistory;
    }

    @BeforeEach
    public void initTest() {
        searchHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createSearchHistory() throws Exception {
        int databaseSizeBeforeCreate = searchHistoryRepository.findAll().size();
        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);
        restSearchHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        SearchHistory testSearchHistory = searchHistoryList.get(searchHistoryList.size() - 1);
        assertThat(testSearchHistory.getSearchDate()).isEqualTo(DEFAULT_SEARCH_DATE);
        assertThat(testSearchHistory.getDateInfo()).isEqualTo(DEFAULT_DATE_INFO);
    }

    @Test
    @Transactional
    void createSearchHistoryWithExistingId() throws Exception {
        // Create the SearchHistory with an existing ID
        searchHistory.setId(1L);
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        int databaseSizeBeforeCreate = searchHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSearchHistories() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        // Get all the searchHistoryList
        restSearchHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(sameInstant(DEFAULT_SEARCH_DATE))))
            .andExpect(jsonPath("$.[*].dateInfo").value(hasItem(DEFAULT_DATE_INFO)));
    }

    @Test
    @Transactional
    void getSearchHistory() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        // Get the searchHistory
        restSearchHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, searchHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchHistory.getId().intValue()))
            .andExpect(jsonPath("$.searchDate").value(sameInstant(DEFAULT_SEARCH_DATE)))
            .andExpect(jsonPath("$.dateInfo").value(DEFAULT_DATE_INFO));
    }

    @Test
    @Transactional
    void getNonExistingSearchHistory() throws Exception {
        // Get the searchHistory
        restSearchHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchHistory() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();

        // Update the searchHistory
        SearchHistory updatedSearchHistory = searchHistoryRepository.findById(searchHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchHistory are not directly saved in db
        em.detach(updatedSearchHistory);
        updatedSearchHistory.searchDate(UPDATED_SEARCH_DATE).dateInfo(UPDATED_DATE_INFO);
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(updatedSearchHistory);

        restSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
        SearchHistory testSearchHistory = searchHistoryList.get(searchHistoryList.size() - 1);
        assertThat(testSearchHistory.getSearchDate()).isEqualTo(UPDATED_SEARCH_DATE);
        assertThat(testSearchHistory.getDateInfo()).isEqualTo(UPDATED_DATE_INFO);
    }

    @Test
    @Transactional
    void putNonExistingSearchHistory() throws Exception {
        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();
        searchHistory.setId(longCount.incrementAndGet());

        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchHistory() throws Exception {
        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();
        searchHistory.setId(longCount.incrementAndGet());

        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchHistory() throws Exception {
        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();
        searchHistory.setId(longCount.incrementAndGet());

        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchHistoryWithPatch() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();

        // Update the searchHistory using partial update
        SearchHistory partialUpdatedSearchHistory = new SearchHistory();
        partialUpdatedSearchHistory.setId(searchHistory.getId());

        partialUpdatedSearchHistory.searchDate(UPDATED_SEARCH_DATE).dateInfo(UPDATED_DATE_INFO);

        restSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearchHistory))
            )
            .andExpect(status().isOk());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
        SearchHistory testSearchHistory = searchHistoryList.get(searchHistoryList.size() - 1);
        assertThat(testSearchHistory.getSearchDate()).isEqualTo(UPDATED_SEARCH_DATE);
        assertThat(testSearchHistory.getDateInfo()).isEqualTo(UPDATED_DATE_INFO);
    }

    @Test
    @Transactional
    void fullUpdateSearchHistoryWithPatch() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();

        // Update the searchHistory using partial update
        SearchHistory partialUpdatedSearchHistory = new SearchHistory();
        partialUpdatedSearchHistory.setId(searchHistory.getId());

        partialUpdatedSearchHistory.searchDate(UPDATED_SEARCH_DATE).dateInfo(UPDATED_DATE_INFO);

        restSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearchHistory))
            )
            .andExpect(status().isOk());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
        SearchHistory testSearchHistory = searchHistoryList.get(searchHistoryList.size() - 1);
        assertThat(testSearchHistory.getSearchDate()).isEqualTo(UPDATED_SEARCH_DATE);
        assertThat(testSearchHistory.getDateInfo()).isEqualTo(UPDATED_DATE_INFO);
    }

    @Test
    @Transactional
    void patchNonExistingSearchHistory() throws Exception {
        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();
        searchHistory.setId(longCount.incrementAndGet());

        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchHistory() throws Exception {
        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();
        searchHistory.setId(longCount.incrementAndGet());

        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchHistory() throws Exception {
        int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();
        searchHistory.setId(longCount.incrementAndGet());

        // Create the SearchHistory
        SearchHistoryDTO searchHistoryDTO = searchHistoryMapper.toDto(searchHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchHistory() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        int databaseSizeBeforeDelete = searchHistoryRepository.findAll().size();

        // Delete the searchHistory
        restSearchHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
        assertThat(searchHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
