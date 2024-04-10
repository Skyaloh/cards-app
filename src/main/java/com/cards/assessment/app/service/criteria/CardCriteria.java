package com.cards.assessment.app.service.criteria;

import com.cards.assessment.app.domain.enumeration.Status;
import com.cards.assessment.app.helper.filter.Filter;
import com.cards.assessment.app.helper.filter.InstantFilter;
import com.cards.assessment.app.helper.filter.LongFilter;
import com.cards.assessment.app.helper.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class CardCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class CustomerStatusFilter extends Filter<Status> {
    }

    private LongFilter id;
    private LongFilter userId;

    private StringFilter name;
    private StringFilter color;
    private InstantFilter createdDate;

    private CustomerStatusFilter status;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public CustomerStatusFilter getStatus() {
        return status;
    }

    public void setStatus(CustomerStatusFilter status) {
        this.status = status;
    }


    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getColor() {
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCriteria that = (CardCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "CardCriteria{" +
                "id=" + id +
                "userId=" + userId +
                ", status=" + status +
                ", name=" + name +
                ", color=" + color +
                ", createdDate=" + createdDate +
                '}';
    }
}
