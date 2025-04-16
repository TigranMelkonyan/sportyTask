## Project Overview

    This project is a Spring Boot microservices-based application designed to manage an online bookstore.
    It provides features to maintain book inventory, handle the purchasing process with flexible pricing 
    strategies, and manage customer loyalty rewards.

    The application is split into two main services:
    Bookstore Service: Manages the book inventory, pricing logic, and purchase processing.
    It handles operations such as adding, updating, and deleting books, as well as applying discounts 
    based on book type, bundle size, and loyalty point eligibility.
    IAM (Identity and Access Management) Service: Responsible for customer management and loyalty tracking.
    It stores user information, awards loyalty points for purchases, and allows customers 
    to redeem points for free books. This service also handles secure authentication and authorization
    using OAuth2 and JWT-based token security.

## Architecture Overview

    The IAM service follows a layered architectural design pattern, which is well-suited for
    REST-based CRUD operations — a common and effective approach for implementing security-focused services.

    The Bookstore service follows a layered architectural pattern, as its core functionality revolves around 
    structured, domain-driven business logic and REST-based CRUD operations. This pattern promotes clear
    separation of concerns between the presentation, service, and data access layers, making the system easier
    to maintain and extend.

