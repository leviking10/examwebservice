package com.groupeisi.examwebservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A SearchHistory.
 */
@Entity
@Table(name = "search_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "search_date")
    private ZonedDateTime searchDate;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchHistory")
    @JsonIgnoreProperties(value = { "searchHistory" }, allowSetters = true)
    private Set<DateInfo> searchItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSearchDate() {
        return this.searchDate;
    }

    public SearchHistory searchDate(ZonedDateTime searchDate) {
        this.setSearchDate(searchDate);
        return this;
    }

    public void setSearchDate(ZonedDateTime searchDate) {
        this.searchDate = searchDate;
    }






    public Set<DateInfo> getSearchItems() {
        return this.searchItems;
    }

    public void setSearchItems(Set<DateInfo> dateInfos) {
        if (this.searchItems != null) {
            this.searchItems.forEach(i -> i.setSearchHistory(null));
        }
        if (dateInfos != null) {
            dateInfos.forEach(i -> i.setSearchHistory(this));
        }
        this.searchItems = dateInfos;
    }

    public SearchHistory searchItems(Set<DateInfo> dateInfos) {
        this.setSearchItems(dateInfos);
        return this;
    }

    public SearchHistory addSearchItems(DateInfo dateInfo) {
        this.searchItems.add(dateInfo);
        dateInfo.setSearchHistory(this);
        return this;
    }

    public SearchHistory removeSearchItems(DateInfo dateInfo) {
        this.searchItems.remove(dateInfo);
        dateInfo.setSearchHistory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchHistory{" +
            "id=" + getId() +
            ", searchDate='" + getSearchDate() + "'" +
            ", searchItems=" + searchItems.stream().map(Object::toString).collect(Collectors.joining(", ")) +
            "}";
    }
}
