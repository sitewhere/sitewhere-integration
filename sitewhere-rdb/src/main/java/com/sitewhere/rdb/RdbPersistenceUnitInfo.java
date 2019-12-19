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

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

import com.sitewhere.rdb.multitenancy.DataSourceMultiTenantConnectionProviderImpl;
import com.sitewhere.rdb.multitenancy.TenantIdentifierResolverImpl;

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

    private List<String> managedClassNames;

    private Properties properties;

    private List<ClassTransformer> transformers = new ArrayList<>();

    /** Multitenant connection provider */
    private DataSourceMultiTenantConnectionProviderImpl multitenant = new DataSourceMultiTenantConnectionProviderImpl();

    /** Tenant identifier resolver */
    private TenantIdentifierResolverImpl resolver = new TenantIdentifierResolverImpl();

    public RdbPersistenceUnitInfo(List<String> managedClassNames) {
	this.managedClassNames = managedClassNames;
	this.properties = getHibernateProperties();
    }

    protected Properties getHibernateProperties() {
	Properties props = new Properties();
	props.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
	props.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multitenant);
	props.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, resolver);
	props.put(Environment.DIALECT, PostgreSQL94Dialect.class.getName());
	props.put(Environment.SHOW_SQL, String.valueOf(true));
	props.put(Environment.FORMAT_SQL, String.valueOf(true));
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
	return null;
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
