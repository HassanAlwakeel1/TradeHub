package com.TradeHub.configuration;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Hassan Alwakeel (LinkedIn)",
                        url = "https://www.linkedin.com/in/hassan-alwakeel-617537287/"
                ),
                description = "Welcome to the OpenAPI documentation for TradeHub, an e-commerce platform, built with Java and Spring Boot. It provides RESTful endpoints to manage users, products, carts, orders, and payments.",
                title = "TradeHub",
                version = "1.0"
        ),
        externalDocs = @ExternalDocumentation(
                description = "GitHub",
                url = "https://github.com/HassanAlwakeel1"
        )
)
public class OpenApiConfig {
}
