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

### Reference Documentation

* [Spring Boot Documentation](https://docs.spring.io/spring-boot/index.html)
* [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
* [Flyway Documentation](https://docs.spring.io/spring-boot/api/rest/actuator/flyway.html)
* [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/reference/)
* [PostgreSQL Documentation](https://www.postgresql.org/docs/)
* [Maven Documentation](https://maven.apache.org/guides/index.html)
* [Docker Reference Guide](https://docs.docker.com)

### Default IAM users in DB

    Admin
    password: test123!
    userName: admin
  
    User
    password: test456!
    userName: testUser

### Default Book Types in DB

    NEW_RELEASE [100]
    REGULAR
    OLD_EDITION

### Application build/run description

1. Go to the project base directory f.e cd {base-dir}/sportyTask
2. Make: mvn clean install
3. Make: docker-compose -f local_stack/docker-compose.yml up --build
4. Go to Iam Directory: cd iam: mvn spring-boot:run
5. GO to Bookstore Directory: cd bookstore: mvn spring-boot:run
6. After running application you can retrieve jwt token by default iam users credentials
   mentioned above and use apis listed below.

### Quick Run Commands

```shell
mvn clean install 
```

```shell
docker-compose -f local_stack/docker-compose.yml up --build
```

```shell
cd iam && mvn spring-boot:run
```

```shell
cd bookstore && mvn spring-boot:run
```

## Explanation of the decisions made along the way

      Microservice Architecture: The system was designed as microservices to allow independent deployment,
      scaling, and clearer separation of concerns (e.g. order management, IAM, etc.).

      Shared Database: Although services are independent, a shared database (MySQL) was used initially
      for simplicity and consistency in data access and for isolated databaes. Flyway is used to manage DB
      schema changes across services.

      Flyway Migration Versioning: Each service has its own versioning strategy to prevent conflicts. 
      Migrations are versioned and coordinated to avoid overlap. 
      It allows more control and flexibility over db entities.

      Loyalty System Logic: Loyalty points accumulate beyond 10, and one point is given for every purchased item
      (excluding free items). Once the discount is applied, points reset to 0,
      and new points are earned starting from the current order.

      Security: OAuth2 with JWT tokens is used for authentication and authorization. 
      Method-level security is used to restrict access based on user roles.

      Docker: The solution uses Docker and docker-compose to set up a local database for development,
      ensuring a consistent environment for local development and testing.      

      Flexible Book Type Mechanism: A flexible book type mechanism is implemented, allowing admin 
      to create and manage book types dynamically, making it easier to adapt to changing business requirements
      without requiring code changes. Also provided default 3 types of books as mentioned in requirements.

### Iam service swagger

* [swagger url](http://localhost:8081/swagger-ui.html)

### Bookstore service swagger

* [swagger url](http://localhost:8080/swagger-ui.html)

### OauthTokenController Description

      Handles user authentication by issuing JWT tokens based on valid credentials. 
      Provides the necessary endpoint to request and receive access tokens for authenticated API access.

### UserInfoController Description

      Manages user-related information, including retrieving and updating user profiles, 
      loyalty points, and other user-specific data.

### BookInventoryAdminController Description

      Handles the management of books in the bookstore for admin users. This controller provides APIs
      that allow admins to perform CRUD operations on books, including adding, updating, and removing
      books from the inventory.

### BookTypeAdminController Description

      Handles the management of book types in the bookstore for admin users. This controller provides APIs
      that allow admins to perform CRUD operations on book types, such as creating, updating,
      and deleting types of books.

### OrderAdminController Description

      Manages orders in the bookstore. This controller provides APIs that allow admins to view,
      update, and delete customer orders, as well as manage the status and details of these orders.

### OrderItemAdminController Description

      Handles the management of individual order items. This controller provides APIs
      to view, add, update, and delete items within orders, ensuring admins can manage the
      specifics of each order efficiently.

### BookController Description

      Provides APIs for retrieving book details in the bookstore. This controller allows both admin and user roles
      to access information about available books, including their descriptions, prices, and availability.

### BookTypeController Description

      Provides APIs for retrieving information about different book types in the bookstore.
      Accessible by both admin and user roles, this controller allows users to view available 
      book types.

### OrderController Description

      Provides APIs for retrieving and managing orders in the bookstore. Accessible by both admin and
      user roles, this controller allows users to view their own orders and admins to manage all orders.

### OrderItemController Description

      Handles APIs for retrieving and managing order items in the bookstore. Accessible by both admin and user roles,
      it allows users to view their order items and admins to manage all order items.

### OrderProcessController Description

      Provides APIs for order preview and processing. Allows users to preview their cart, apply discounts, 
      and proceed with the order place. Accessible by both admin and user roles.

