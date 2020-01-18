/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.communication.mqtt;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.hawtdispatch.DispatchQueue;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Extends {@link TenantEngineLifecycleComponent} with base functionality for
 * connecting to MQTT.
 */
public class MqttLifecycleComponent extends TenantEngineLifecycleComponent {

    /** Default connection timeout in seconds */
    public static final long DEFAULT_CONNECT_TIMEOUT_SECS = 5;

    /** Configuration parameters */
    private IMqttConfiguration configuration;

    /** MQTT client */
    private MQTT mqtt;

    /** Hawtdispatch queue */
    private DispatchQueue queue;

    public MqttLifecycleComponent(LifecycleComponentType type, IMqttConfiguration configuration) {
	super(type);
	this.configuration = configuration;
    }

    /**
     * Gets information about the broker.
     * 
     * @return
     * @throws SiteWhereException
     */
    public String getBrokerInfo() throws SiteWhereException {
	return mqtt.getHost().toString();
    }

    /**
     * Get a {@link FutureConnection} to the MQTT broker.
     * 
     * @return
     * @throws SiteWhereException
     */
    public FutureConnection getConnection() throws SiteWhereException {
	FutureConnection connection = mqtt.futureConnection();
	try {
	    Future<Void> future = connection.connect();
	    future.await(DEFAULT_CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS);
	    return connection;
	} catch (Exception e) {
	    throw new SiteWhereException("Unable to connect to MQTT broker.", e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#start(com.sitewhere.spi
     * .server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	this.queue = Dispatch.createQueue(getComponentId().toString());
	this.mqtt = configure();
    }

    /**
     * Configure trust store.
     * 
     * @param sslContext
     * @param trustStorePath
     * @param trustStorePassword
     * @return
     * @throws Exception
     */
    protected TrustManagerFactory configureTrustStore(SSLContext sslContext, String trustStorePath,
	    String trustStorePassword) throws Exception {
	getLogger().info("MQTT client using truststore path: " + trustStorePath);
	TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	KeyStore tks = KeyStore.getInstance("JKS");
	File trustFile = new File(trustStorePath);
	tks.load(new FileInputStream(trustFile), trustStorePassword.toCharArray());
	tmf.init(tks);
	return tmf;
    }

    /**
     * Configure key store.
     * 
     * @param sslContext
     * @param keyStorePath
     * @param keyStorePassword
     * @return
     * @throws Exception
     */
    protected KeyManagerFactory configureKeyStore(SSLContext sslContext, String keyStorePath, String keyStorePassword)
	    throws Exception {
	getLogger().info("MQTT client using keystore path: " + keyStorePath);
	KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	KeyStore ks = KeyStore.getInstance("JKS");
	File keyFile = new File(keyStorePath);
	ks.load(new FileInputStream(keyFile), keyStorePassword.toCharArray());
	kmf.init(ks, keyStorePassword.toCharArray());
	return kmf;
    }

    /**
     * Handle configuration of secure transport.
     * 
     * @param mqtt
     * @throws SiteWhereException
     */
    protected void handleSecureTransport(MQTT mqtt) throws SiteWhereException {
	getLogger().info("MQTT client using secure protocol '" + configuration.getProtocol() + "'.");
	boolean trustStoreConfigured = (getConfiguration().getTrustStorePath() != null)
		&& (getConfiguration().getTrustStorePassword() != null);
	boolean keyStoreConfigured = (getConfiguration().getKeyStorePath() != null)
		&& (getConfiguration().getKeyStorePassword() != null);
	try {
	    SSLContext sslContext = SSLContext.getInstance("TLS");
	    TrustManagerFactory tmf = null;
	    KeyManagerFactory kmf;

	    // Handle trust store configuration.
	    if (trustStoreConfigured) {
		tmf = configureTrustStore(sslContext, getConfiguration().getTrustStorePath(),
			getConfiguration().getTrustStorePassword());
	    } else {
		getLogger().info("No trust store configured for MQTT client.");
	    }

	    // Handle key store configuration.
	    if (keyStoreConfigured) {
		kmf = configureKeyStore(sslContext, getConfiguration().getKeyStorePath(),
			getConfiguration().getKeyStorePassword());
		sslContext.init(kmf.getKeyManagers(), tmf != null ? tmf.getTrustManagers() : null, null);
	    } else if (trustStoreConfigured) {
		sslContext.init(null, tmf != null ? tmf.getTrustManagers() : null, null);
	    }

	    mqtt.setSslContext(sslContext);
	    getLogger().info("Created SSL context for MQTT receiver.");
	} catch (Throwable t) {
	    throw new SiteWhereException("Unable to configure secure transport.", t);
	}
    }

    /**
     * Configures MQTT connectivity based on configuration.
     * 
     * @return
     * @throws SiteWhereException
     */
    public MQTT configure() throws SiteWhereException {
	MQTT mqtt = new MQTT();

	boolean usingSSL = getConfiguration().getProtocol().startsWith("ssl");
	boolean usingTLS = getConfiguration().getProtocol().startsWith("tls");

	// Optionally set client id.
	if (getConfiguration().getClientId() != null) {
	    mqtt.setClientId(getConfiguration().getClientId());
	    getLogger().info("MQTT connection will use client id '" + getConfiguration().getClientId() + "'.");
	}
	// Set flag for clean session.
	mqtt.setCleanSession(getConfiguration().isCleanSession());
	getLogger().info("MQTT clean session flag being set to '" + getConfiguration().isCleanSession() + "'.");

	if (usingSSL || usingTLS) {
	    handleSecureTransport(mqtt);
	}
	// Set username if provided.
	if (!StringUtils.isEmpty(getConfiguration().getUsername())) {
	    mqtt.setUserName(getConfiguration().getUsername());
	}
	// Set password if provided.
	if (!StringUtils.isEmpty(getConfiguration().getPassword())) {
	    mqtt.setPassword(getConfiguration().getPassword());
	}
	try {
	    mqtt.setHost(getConfiguration().getProtocol() + "://" + getConfiguration().getHostname() + ":"
		    + getConfiguration().getPort());
	    return mqtt;
	} catch (URISyntaxException e) {
	    throw new SiteWhereException("Invalid hostname for MQTT server.", e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.server.lifecycle.LifecycleComponent#stop(com.sitewhere.spi.
     * server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void stop(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	if (queue != null) {
	    queue.suspend();
	}
    }

    protected IMqttConfiguration getConfiguration() {
	return configuration;
    }

    protected DispatchQueue getQueue() {
	return queue;
    }
}