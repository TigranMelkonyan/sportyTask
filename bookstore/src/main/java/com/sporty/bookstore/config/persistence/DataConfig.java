package com.sporty.bookstore.config.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 2:13 PM
 */
@EnableJpaRepositories(basePackages = "com.sporty.bookstore.repository")
@EnableJpaAuditing
@Configuration
@EntityScan(basePackages = "com.sporty.bookstore")
public class DataConfig {
}
