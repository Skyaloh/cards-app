package com.cards.assessment.app.dto;

import org.springframework.data.annotation.ReadOnlyProperty;

import java.io.Serializable;
import java.time.Instant;

public abstract class AbstractAuditingDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ReadOnlyProperty
    private Instant createdDate = Instant.now();

    @ReadOnlyProperty
    private Instant modifiedDate = Instant.now();

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


}
