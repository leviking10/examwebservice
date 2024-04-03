package com.groupeisi.examwebservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.groupeisi.examwebservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DateInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DateInfoDTO.class);
        DateInfoDTO dateInfoDTO1 = new DateInfoDTO();
        dateInfoDTO1.setId(1L);
        DateInfoDTO dateInfoDTO2 = new DateInfoDTO();
        assertThat(dateInfoDTO1).isNotEqualTo(dateInfoDTO2);
        dateInfoDTO2.setId(dateInfoDTO1.getId());
        assertThat(dateInfoDTO1).isEqualTo(dateInfoDTO2);
        dateInfoDTO2.setId(2L);
        assertThat(dateInfoDTO1).isNotEqualTo(dateInfoDTO2);
        dateInfoDTO1.setId(null);
        assertThat(dateInfoDTO1).isNotEqualTo(dateInfoDTO2);
    }
}
