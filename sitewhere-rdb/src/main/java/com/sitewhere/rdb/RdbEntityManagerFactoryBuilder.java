/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rdb;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

/**
 * Supports building an {@link EntityManagerFactory} which manages SiteWhere RDB
 * entities.
 */
public class RdbEntityManagerFactoryBuilder {

    public static EntityManagerFactory buildFrom(RdbProviderInformation<?> provider, List<Class<?>> entityClasses,
	    DataSource dataSource, String schema) {
	PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(provider, entityClasses, dataSource, schema);
	return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), null)
		.build();
    }

    protected static RdbPersistenceUnitInfo getPersistenceUnitInfo(RdbProviderInformation<?> provider,
	    List<Class<?>> entityClasses, DataSource dataSource, String schema) {
	List<String> classNames = entityClasses.stream().map(Class::getName).collect(Collectors.toList());
	return new RdbPersistenceUnitInfo(provider, classNames, dataSource, schema);
    }
}
