package co.uk.ordnancesurvey.api.db;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Configuration class picking up database connection settings from
 * application.properties file
 */
@Configuration
public class DataSourceConfiguration {

	@Bean(name = "wfs")
	@ConfigurationProperties("wfs.datasource")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

}
