package com.groupeisi.examwebservice.domain;

import static com.groupeisi.examwebservice.domain.DateInfoTestSamples.*;
import static com.groupeisi.examwebservice.domain.SearchHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.groupeisi.examwebservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DateInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DateInfo.class);
        DateInfo dateInfo1 = getDateInfoSample1();
        DateInfo dateInfo2 = new DateInfo();
        assertThat(dateInfo1).isNotEqualTo(dateInfo2);

        dateInfo2.setId(dateInfo1.getId());
        assertThat(dateInfo1).isEqualTo(dateInfo2);

        dateInfo2 = getDateInfoSample2();
        assertThat(dateInfo1).isNotEqualTo(dateInfo2);
    }


}
