package com.custom.app.model;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by olga on 26.09.15.
 * This is a base class for all entities. It provides an equals and hashcode that will always work correctly in all
 * circumstances. This avoids frequent errors related to the implementation of those same methods.
 */
@MappedSuperclass
public class AbstractEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Long version;

    @Transient
    private UUID uuid;

    @Column(name = "UUID")
    private String uuidStr;

    @PrePersist
    protected void prePersist() {
        syncUuidString();
    }

    protected void syncUuidString() {
        if (uuidStr == null) {
            // initial method call fills the uuid
            getUuid();
        }
    }

    public UUID getUuid() {
        if (uuidStr == null) {
            if (uuid == null) {
                uuid = UUID.randomUUID();
            }
            uuidStr = uuid.toString();
        }
        if (uuid == null && uuidStr != null) {
            uuid = UUID.fromString(uuidStr);
        }
        return uuid;
    }

    public Integer getId() {
        return id;
    }

    /*
    *
    *   This method is mean for testing purposes only (create mock data), as we should not set Ids manually,
    *   Hibernate will do it automatically
    *
    **/
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getUuid() != null ? getUuid().hashCode() : 0;
    }

    public Long getVersion() {
        return version;
    }

    public String getUuidStr() {
        return uuidStr;
    }
}
