package com.cl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author: chenl
 * @since: 2025/9/3 17:42
 * @description:
 */
@Configuration
public class DatabaseConfig implements EnvironmentAware {

    private Environment env;

    @Bean(name = "dsMain")
    @Primary
    public DataSource dsMain () {
        HikariConfig config = new HikariConfig();
        config.setPoolName(env.getProperty("spring.datasource.name"));
        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        config.setJdbcUrl(env.getProperty("spring.datasource.url"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));
        //config.setConnectionTestQuery(env.getProperty("datasource.main.validationQuery"));
        config.setMinimumIdle(Integer.valueOf(env.getProperty("spring.datasource.hikari.minimum-idle")));
        config.setMaximumPoolSize(Integer.valueOf(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
        config.setIdleTimeout(Integer.valueOf(env.getProperty("spring.datasource.hikari.connection-timeout")));

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dsMain) {
        JdbcTemplate template = new
                JdbcTemplate();
        template.setDataSource(dsMain);
        return template;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

}
