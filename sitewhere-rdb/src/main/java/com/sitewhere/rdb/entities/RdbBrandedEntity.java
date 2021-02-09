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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.sitewhere.spi.common.IBrandedEntity;

/**
 * Base class for common branded entity information.
 */
@MappedSuperclass
public abstract class RdbBrandedEntity extends RdbPersistentEntity implements IBrandedEntity {

    /** Serial version UID */
    private static final long serialVersionUID = 1316198983434049296L;

    /** Date entity was last updated */
    @Column(name = "background_color")
    private String backgroundColor;

    /** Foreground color */
    @Column(name = "foreground_color")
    private String foregroundColor;

    /** Border color */
    @Column(name = "border_color")
    private String borderColor;

    /** Image URL */
    @Column(name = "image_url")
    private String imageUrl;

    /** Icon */
    @Column(name = "icon")
    private String icon;

    public String getBackgroundColor() {
	return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
	this.backgroundColor = backgroundColor;
    }

    public String getForegroundColor() {
	return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
	this.foregroundColor = foregroundColor;
    }

    public String getBorderColor() {
	return borderColor;
    }

    public void setBorderColor(String borderColor) {
	this.borderColor = borderColor;
    }

    public String getImageUrl() {
	return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public static void copy(IBrandedEntity source, RdbBrandedEntity target) {
	if (source.getBackgroundColor() != null) {
	    target.setBackgroundColor(source.getBackgroundColor());
	}
	if (source.getForegroundColor() != null) {
	    target.setForegroundColor(source.getForegroundColor());
	}
	if (source.getBorderColor() != null) {
	    target.setBorderColor(source.getBorderColor());
	}
	if (source.getImageUrl() != null) {
	    target.setImageUrl(source.getImageUrl());
	}
	if (source.getIcon() != null) {
	    target.setIcon(source.getIcon());
	}
	RdbPersistentEntity.copy(source, target);
    }
}
