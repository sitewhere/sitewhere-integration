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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sitewhere.rdb.spi.IRdbTenantEngine;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.IFunctionIdentifier;

public class FlywayConfig {

    /** Static logger instance */
    private static Logger LOGGER = LoggerFactory.getLogger(FlywayConfig.class);

    /** Flyway migrations folder on filesystem */
    private static final String FLYWAY_DIR = "/workspace/sitewhere/flyway/migrations";

    /**
     * Convert a function identifier into a schema name.
     * 
     * @param function
     * @return
     */
    public static String getSchemaName(IFunctionIdentifier function) {
	return function.getPath().replaceAll("[-]", "");
    }

    /**
     * Migrate tenant data if needed.
     * 
     * @param dataSource
     * @param function
     * @throws SiteWhereException
     */
    public static void migrateTenantData(IRdbTenantEngine<?> engine, DataSource dataSource,
	    IFunctionIdentifier function) throws SiteWhereException {
	String area = getSchemaName(function);
	try {
	    Path migrations = Files.createDirectories(Paths.get(FLYWAY_DIR));
	    for (String resourceName : engine.getFlywayMigrations()) {
		InputStream resourceStream = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(String.format("db/migrations/%s", resourceName));
		if (resourceStream == null) {
		    LOGGER.warn(String.format("Migration not found for '%s'. Skipping.", resourceName));
		    continue;
		}
		Path target = migrations.resolve(resourceName);
		if (!Files.exists(target)) {
		    LOGGER.info(String.format("Copying migration resource to: %s", target.toString()));
		    Files.copy(resourceStream, target, StandardCopyOption.REPLACE_EXISTING);
		}
	    }

	    Flyway flyway = Flyway.configure().locations(String.format("filesystem:%s", migrations.toString()))
		    .dataSource(dataSource).schemas(area).load();
	    flyway.migrate();
	} catch (FlywayException e) {
	    throw new SiteWhereException("Flyway migration failed.", e);
	} catch (IOException e) {
	    throw new SiteWhereException("Unable to bootstrap migrations to filesystem.", e);
	}
    }
}
