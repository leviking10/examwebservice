package com.groupeisi.examwebservice.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.groupeisi.examwebservice.domain.SearchHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchHistoryDTO implements Serializable {

    private Long id;

    private ZonedDateTime searchDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(ZonedDateTime searchDate) {
        this.searchDate = searchDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchHistoryDTO)) {
            return false;
        }

        SearchHistoryDTO searchHistoryDTO = (SearchHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, searchHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchHistoryDTO{" +
            "id=" + getId() +
            ", searchDate='" + getSearchDate() + "'" +
            "}";
    }
}
