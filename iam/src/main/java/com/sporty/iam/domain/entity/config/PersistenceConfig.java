package com.sporty.iam.domain.entity.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Tigran Melkonyan
 * Date: 2/18/25
 * Time: 4:13 PM
 */
@EnableJpaRepositories(basePackages = {"com.sporty.iam"})
@EntityScan(basePackages = {"com.sporty.iam"})
@Configuration
public class PersistenceConfig {
}
