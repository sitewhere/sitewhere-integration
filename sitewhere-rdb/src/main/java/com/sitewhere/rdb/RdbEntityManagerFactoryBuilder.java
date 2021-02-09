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

    public static EntityManagerFactory buildFrom(RdbProviderInformation<?> provider, RdbPersistenceOptions options,
	    List<Class<?>> entityClasses, DataSource dataSource, String schema) {
	PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(provider, options, entityClasses, dataSource,
		schema);
	return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), null)
		.build();
    }

    protected static RdbPersistenceUnitInfo getPersistenceUnitInfo(RdbProviderInformation<?> provider,
	    RdbPersistenceOptions options, List<Class<?>> entityClasses, DataSource dataSource, String schema) {
	List<String> classNames = entityClasses.stream().map(Class::getName).collect(Collectors.toList());
	return new RdbPersistenceUnitInfo(provider, options, classNames, dataSource, schema);
    }
}
