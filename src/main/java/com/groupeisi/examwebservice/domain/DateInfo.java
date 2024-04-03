package com.groupeisi.examwebservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A DateInfo.
 */
@Entity
@Table(name = "date_info")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "day_of_week")
    private String dayOfWeek;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "searchItems" }, allowSetters = true)
    private SearchHistory searchHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DateInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public DateInfo date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    public DateInfo dayOfWeek(String dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
        return this;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public SearchHistory getSearchHistory() {
        return this.searchHistory;
    }

    public void setSearchHistory(SearchHistory searchHistory) {
        this.searchHistory = searchHistory;
    }

    public DateInfo searchHistory(SearchHistory searchHistory) {
        this.setSearchHistory(searchHistory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((DateInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DateInfo{" +
            ", date='" + getDate() + "'" +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            "}";
    }
}
