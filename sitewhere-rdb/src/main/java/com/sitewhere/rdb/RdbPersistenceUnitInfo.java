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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;

/**
 * Provides information required to create a JPA persistence unit.
 */
public class RdbPersistenceUnitInfo implements PersistenceUnitInfo {

    /** JPA version */
    private static final String JPA_VERSION = "2.1";

    /** Default persistence unit name */
    private static final String DEFAULT_UNIT_NAME = "sitewhere";

    /** Persistence unit name */
    private String persistenceUnitName = DEFAULT_UNIT_NAME;

    /** Provider information */
    private RdbProviderInformation<?> provider;

    /** Persistence options */
    private RdbPersistenceOptions options;

    /** List of class names being managed */
    private List<String> managedClassNames;

    /** DataSource */
    private DataSource jtaDataSource;

    /** Schema referenced */
    private String schema;

    /** Hibernate properties */
    private Properties properties;

    /** List of transformers */
    private List<ClassTransformer> transformers = new ArrayList<>();

    public RdbPersistenceUnitInfo(RdbProviderInformation<?> provider, RdbPersistenceOptions options,
	    List<String> managedClassNames, DataSource dataSource, String schema) {
	this.provider = provider;
	this.options = options;
	this.managedClassNames = managedClassNames;
	this.jtaDataSource = dataSource;
	this.schema = schema;
	this.properties = getHibernateProperties();
    }

    protected Properties getHibernateProperties() {
	Properties props = new Properties();
	props.put(Environment.DEFAULT_SCHEMA, schema);
	props.put(Environment.HBM2DDL_AUTO, options.getHbmToDdlAuto());
	props.put(Environment.DIALECT, provider.getDialect());
	props.put(Environment.SHOW_SQL, String.valueOf(options.isShowSql()));
	props.put(Environment.FORMAT_SQL, String.valueOf(options.isFormatSql()));
	props.put(Environment.NON_CONTEXTUAL_LOB_CREATION, String.valueOf(true));
	return props;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getPersistenceUnitName()
     */
    @Override
    public String getPersistenceUnitName() {
	return this.persistenceUnitName;
    }

    /*
     * @see
     * javax.persistence.spi.PersistenceUnitInfo#getPersistenceProviderClassName()
     */
    @Override
    public String getPersistenceProviderClassName() {
	return HibernatePersistenceProvider.class.getName();
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getTransactionType()
     */
    @Override
    public PersistenceUnitTransactionType getTransactionType() {
	return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getJtaDataSource()
     */
    @Override
    public DataSource getJtaDataSource() {
	return jtaDataSource;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getNonJtaDataSource()
     */
    @Override
    public DataSource getNonJtaDataSource() {
	return null;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getMappingFileNames()
     */
    @Override
    public List<String> getMappingFileNames() {
	return Collections.emptyList();
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getJarFileUrls()
     */
    @Override
    public List<URL> getJarFileUrls() {
	return Collections.emptyList();
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getPersistenceUnitRootUrl()
     */
    @Override
    public URL getPersistenceUnitRootUrl() {
	return null;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getManagedClassNames()
     */
    @Override
    public List<String> getManagedClassNames() {
	return managedClassNames;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#excludeUnlistedClasses()
     */
    @Override
    public boolean excludeUnlistedClasses() {
	return true;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getSharedCacheMode()
     */
    @Override
    public SharedCacheMode getSharedCacheMode() {
	return SharedCacheMode.NONE;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getValidationMode()
     */
    @Override
    public ValidationMode getValidationMode() {
	return ValidationMode.AUTO;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getProperties()
     */
    @Override
    public Properties getProperties() {
	return this.properties;
    }

    /*
     * @see
     * javax.persistence.spi.PersistenceUnitInfo#getPersistenceXMLSchemaVersion()
     */
    @Override
    public String getPersistenceXMLSchemaVersion() {
	return JPA_VERSION;
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getClassLoader()
     */
    @Override
    public ClassLoader getClassLoader() {
	return null;
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
	this.transformers.add(transformer);
    }

    /*
     * @see javax.persistence.spi.PersistenceUnitInfo#getNewTempClassLoader()
     */
    @Override
    public ClassLoader getNewTempClassLoader() {
	return null;
    }
}
