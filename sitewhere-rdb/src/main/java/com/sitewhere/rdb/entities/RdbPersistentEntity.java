/**
 * Copyright © 2014-2021 The SiteWhere Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sitewhere.rdb.entities;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.sitewhere.spi.common.IPersistentEntity;

/**
 * Base class for common persistent entity information.
 */
@MappedSuperclass
public abstract class RdbPersistentEntity implements IPersistentEntity {

    /** Serial version UID */
    private static final long serialVersionUID = 7969849077443762835L;

    /** Unique token */
    @Column(name = "token", unique = true)
    private String token;

    /** Date entity was created */
    @Column(name = "created_date")
    private Date createdDate;

    /** Username for creator */
    @Column(name = "created_by")
    private String createdBy;

    /** Date entity was last updated */
    @Column(name = "updated_date")
    private Date updatedDate;

    /** Username that updated entity */
    @Column(name = "updated_by")
    private String updatedBy;

    /*
     * @see com.sitewhere.spi.common.IPersistentEntity#getToken()
     */
    @Override
    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    /*
     * @see com.sitewhere.spi.common.IPersistentEntity#getCreatedDate()
     */
    @Override
    public Date getCreatedDate() {
	return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
    }

    /*
     * @see com.sitewhere.spi.common.IPersistentEntity#getCreatedBy()
     */
    @Override
    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    /*
     * @see com.sitewhere.spi.common.IPersistentEntity#getUpdatedDate()
     */
    @Override
    public Date getUpdatedDate() {
	return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
    }

    /*
     * @see com.sitewhere.spi.common.IPersistentEntity#getUpdatedBy()
     */
    @Override
    public String getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
	this.updatedBy = updatedBy;
    }

    /**
     * Implemented in subclasses to set metadata.
     * 
     * @param metadata
     */
    public abstract void setMetadata(Map<String, String> metadata);

    public static void copy(IPersistentEntity source, RdbPersistentEntity target) {
	if (source.getToken() != null) {
	    target.setToken(source.getToken());
	}
	if (source.getCreatedDate() != null) {
	    target.setCreatedDate(source.getCreatedDate());
	}
	if (source.getCreatedBy() != null) {
	    target.setCreatedBy(source.getCreatedBy());
	}
	if (source.getUpdatedDate() != null) {
	    target.setUpdatedDate(source.getUpdatedDate());
	}
	if (source.getUpdatedBy() != null) {
	    target.setUpdatedBy(source.getUpdatedBy());
	}
	if (source.getMetadata() != null) {
	    target.setMetadata(source.getMetadata());
	}
    }
}
