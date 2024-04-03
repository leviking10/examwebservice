package com.groupeisi.examwebservice.domain;

import static com.groupeisi.examwebservice.domain.DateInfoTestSamples.*;
import static com.groupeisi.examwebservice.domain.SearchHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.groupeisi.examwebservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SearchHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchHistory.class);
        SearchHistory searchHistory1 = getSearchHistorySample1();
        SearchHistory searchHistory2 = new SearchHistory();
        assertThat(searchHistory1).isNotEqualTo(searchHistory2);

        searchHistory2.setId(searchHistory1.getId());
        assertThat(searchHistory1).isEqualTo(searchHistory2);

        searchHistory2 = getSearchHistorySample2();
        assertThat(searchHistory1).isNotEqualTo(searchHistory2);
    }

    @Test
    void searchItemsTest() throws Exception {
        SearchHistory searchHistory = getSearchHistoryRandomSampleGenerator();
        DateInfo dateInfoBack = getDateInfoRandomSampleGenerator();

        searchHistory.addSearchItems(dateInfoBack);
        assertThat(searchHistory.getSearchItems()).containsOnly(dateInfoBack);
        assertThat(dateInfoBack.getSearchHistory()).isEqualTo(searchHistory);

        searchHistory.removeSearchItems(dateInfoBack);
        assertThat(searchHistory.getSearchItems()).doesNotContain(dateInfoBack);
        assertThat(dateInfoBack.getSearchHistory()).isNull();

        searchHistory.searchItems(new HashSet<>(Set.of(dateInfoBack)));
        assertThat(searchHistory.getSearchItems()).containsOnly(dateInfoBack);
        assertThat(dateInfoBack.getSearchHistory()).isEqualTo(searchHistory);

        searchHistory.setSearchItems(new HashSet<>());
        assertThat(searchHistory.getSearchItems()).doesNotContain(dateInfoBack);
        assertThat(dateInfoBack.getSearchHistory()).isNull();
    }
}
