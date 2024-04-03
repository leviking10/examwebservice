package com.groupeisi.examwebservice.service.dto;

import com.groupeisi.examwebservice.domain.DateInfo;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.groupeisi.examwebservice.domain.DateInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DateInfoDTO implements Serializable {

    private Long id;

    private String date;

    private String dayOfWeek;

    private SearchHistoryDTO searchHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public DateInfoDTO() {
    }

    public DateInfoDTO(DateInfo dateInfo) {
        this.date = dateInfo.getDate();
        this.dayOfWeek = dateInfo.getDayOfWeek();
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public SearchHistoryDTO getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(SearchHistoryDTO searchHistory) {
        this.searchHistory = searchHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateInfoDTO)) {
            return false;
        }

        DateInfoDTO dateInfoDTO = (DateInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dateInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DateInfoDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", searchHistory=" + getSearchHistory() +
            "}";
    }
}
