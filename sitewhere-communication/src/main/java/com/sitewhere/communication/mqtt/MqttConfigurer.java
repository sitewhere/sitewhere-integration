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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.fusesource.mqtt.client.MQTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sitewhere.spi.SiteWhereException;

/**
 * Common MQTT configuration functionality.
 */
public class MqttConfigurer {

    /** Static logger instance */
    private static Logger LOGGER = LoggerFactory.getLogger(MqttConfigurer.class);

    /**
     * Configures MQTT connectivity based on configuration.
     * 
     * @return
     * @throws SiteWhereException
     */
    public static MQTT configure(IMqttConfiguration configuration) throws SiteWhereException {
	MQTT mqtt = new MQTT();

	boolean usingSSL = configuration.getProtocol().startsWith("ssl");
	boolean usingTLS = configuration.getProtocol().startsWith("tls");

	// Optionally set client id.
	if (configuration.getClientId() != null) {
	    mqtt.setClientId(configuration.getClientId());
	    LOGGER.info("MQTT connection will use client id '" + configuration.getClientId() + "'.");
	}
	// Set flag for clean session.
	mqtt.setCleanSession(configuration.isCleanSession());
	LOGGER.info("MQTT clean session flag being set to '" + configuration.isCleanSession() + "'.");

	if (usingSSL || usingTLS) {
	    handleSecureTransport(mqtt, configuration);
	}
	// Set username if provided.
	if (!StringUtils.isEmpty(configuration.getUsername())) {
	    mqtt.setUserName(configuration.getUsername());
	}
	// Set password if provided.
	if (!StringUtils.isEmpty(configuration.getPassword())) {
	    mqtt.setPassword(configuration.getPassword());
	}
	try {
	    mqtt.setHost(
		    configuration.getProtocol() + "://" + configuration.getHostname() + ":" + configuration.getPort());
	    return mqtt;
	} catch (URISyntaxException e) {
	    throw new SiteWhereException("Invalid hostname for MQTT server.", e);
	}
    }

    /**
     * Handle configuration of secure transport.
     * 
     * @param mqtt
     * @throws SiteWhereException
     */
    protected static void handleSecureTransport(MQTT mqtt, IMqttConfiguration configuration) throws SiteWhereException {
	LOGGER.info("MQTT client using secure protocol '" + configuration.getProtocol() + "'.");
	boolean trustStoreConfigured = (configuration.getTrustStorePath() != null)
		&& (configuration.getTrustStorePassword() != null);
	boolean keyStoreConfigured = (configuration.getKeyStorePath() != null)
		&& (configuration.getKeyStorePassword() != null);
	try {
	    SSLContext sslContext = SSLContext.getInstance("TLS");
	    TrustManagerFactory tmf = null;
	    KeyManagerFactory kmf;

	    // Handle trust store configuration.
	    if (trustStoreConfigured) {
		tmf = configureTrustStore(sslContext, configuration.getTrustStorePath(),
			configuration.getTrustStorePassword());
	    } else {
		LOGGER.info("No trust store configured for MQTT client.");
	    }

	    // Handle key store configuration.
	    if (keyStoreConfigured) {
		kmf = configureKeyStore(sslContext, configuration.getKeyStorePath(),
			configuration.getKeyStorePassword());
		sslContext.init(kmf.getKeyManagers(), tmf != null ? tmf.getTrustManagers() : null, null);
	    } else if (trustStoreConfigured) {
		sslContext.init(null, tmf != null ? tmf.getTrustManagers() : null, null);
	    }

	    mqtt.setSslContext(sslContext);
	    LOGGER.info("Created SSL context for MQTT receiver.");
	} catch (Throwable t) {
	    throw new SiteWhereException("Unable to configure secure transport.", t);
	}
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
    protected static TrustManagerFactory configureTrustStore(SSLContext sslContext, String trustStorePath,
	    String trustStorePassword) throws Exception {
	LOGGER.info("MQTT client using truststore path: " + trustStorePath);
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
    protected static KeyManagerFactory configureKeyStore(SSLContext sslContext, String keyStorePath,
	    String keyStorePassword) throws Exception {
	LOGGER.info("MQTT client using keystore path: " + keyStorePath);
	KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	KeyStore ks = KeyStore.getInstance("JKS");
	File keyFile = new File(keyStorePath);
	ks.load(new FileInputStream(keyFile), keyStorePassword.toCharArray());
	kmf.init(ks, keyStorePassword.toCharArray());
	return kmf;
    }
}
