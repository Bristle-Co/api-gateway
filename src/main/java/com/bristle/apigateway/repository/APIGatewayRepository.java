package com.bristle.apigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface APIGatewayRepository extends JpaRepository<String, String> {
    // created for future use
    // annotate with @Repository in the future
}
