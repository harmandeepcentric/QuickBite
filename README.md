# QuickBite Menu Management API

A RESTful API for managing restaurant menu items, built with Spring Boot and SQLite database following Test-Driven Development (TDD) practices.

## 🚀 Features

- **Complete CRUD Operations** - Create, Read, Update, and Delete menu items
- **Advanced Search & Filtering** - Search by name/description, filter by category, dietary tags, and price range
- **Pagination Support** - Efficient handling of large menu datasets
- **Data Validation** - Comprehensive input validation with detailed error messages
- **OpenAPI/Swagger Documentation** - Interactive API documentation
- **SQLite Database** - Lightweight, serverless database for data persistence
- **Docker Support** - Containerized application for easy deployment
- **Test Coverage** - Built following TDD with comprehensive test suite

## 📋 API Endpoints

### Menu Items Management

| Method | Endpoint                  | Description                                   |
| ------ | ------------------------- | --------------------------------------------- |
| GET    | `/api/v1/menu-items`      | Get all menu items (with optional pagination) |
| GET    | `/api/v1/menu-items/{id}` | Get menu item by ID                           |
| POST   | `/api/v1/menu-items`      | Create new menu item                          |
| PUT    | `/api/v1/menu-items/{id}` | Update entire menu item                       |
| PATCH  | `/api/v1/menu-items/{id}` | Partially update menu item                    |
| DELETE | `/api/v1/menu-items/{id}` | Delete menu item (soft delete)                |

### Search & Filtering

| Method | Endpoint                                                       | Description                   |
| ------ | -------------------------------------------------------------- | ----------------------------- |
| GET    | `/api/v1/menu-items/search?q={term}`                           | Search by name or description |
| GET    | `/api/v1/menu-items/category/{category}`                       | Get items by category         |
| GET    | `/api/v1/menu-items/dietary-tag/{tag}`                         | Get items by dietary tag      |
| GET    | `/api/v1/menu-items/price-range?minPrice={min}&maxPrice={max}` | Get items by price range      |

### Metadata

| Method | Endpoint                          | Description                    |
| ------ | --------------------------------- | ------------------------------ |
| GET    | `/api/v1/menu-items/categories`   | Get all available categories   |
| GET    | `/api/v1/menu-items/dietary-tags` | Get all available dietary tags |

## 🛠️ Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.1.5** - Application framework
- **Spring Data JPA** - Data persistence
- **SQLite** - Database
- **Maven** - Build tool
- **OpenAPI 3** - API documentation
- **JUnit 5** - Testing framework
- **Docker** - Containerization

## 📊 Data Model

### MenuItem

```json
{
  "id": 1,
  "name": "Margherita Pizza",
  "description": "Classic pizza with fresh mozzarella, tomato sauce, and basil",
  "price": 12.99,
  "category": "Main Course",
  "dietaryTag": "Vegetarian",
  "createdAt": "2025-09-20T10:30:00",
  "updatedAt": "2025-09-20T10:30:00"
}
```

### Field Validation

- **name**: Required, 2-100 characters, must be unique
- **description**: Optional, max 500 characters
- **price**: Required, must be > 0.01, max 999,999.99
- **category**: Required, 2-50 characters
- **dietaryTag**: Optional, max 100 characters

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use included Maven wrapper)
- Docker (optional, for containerized deployment)

### Running Locally

1. **Clone the repository**

   ```bash
   git clone https://github.com/harmandeepcentric/QuickBite.git
   cd QuickBite
   ```

2. **Build the application**

   ```bash
   ./mvnw clean package
   ```

3. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**
   - API: http://localhost:8080/api/v1/menu-items
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

### Running with Docker

1. **Build the Docker image**

   ```bash
   docker build -t quickbite-api .
   ```

2. **Run the container**
   ```bash
   docker run -p 8080:8080 -v quickbite-data:/app/data quickbite-api
   ```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## 📖 API Documentation

The API documentation is automatically generated using OpenAPI 3 and is available at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 📝 Sample API Usage

### Create a Menu Item

```bash
curl -X POST http://localhost:8080/api/v1/menu-items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Margherita Pizza",
    "description": "Classic pizza with fresh mozzarella, tomato sauce, and basil",
    "price": 12.99,
    "category": "Main Course",
    "dietaryTag": "Vegetarian"
  }'
```

### Get All Menu Items

```bash
curl http://localhost:8080/api/v1/menu-items
```

### Search Menu Items

```bash
curl "http://localhost:8080/api/v1/menu-items/search?q=pizza"
```

### Get Items by Category

```bash
curl http://localhost:8080/api/v1/menu-items/category/Main%20Course
```

### Update Menu Item

```bash
curl -X PUT http://localhost:8080/api/v1/menu-items/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Deluxe Margherita Pizza",
    "price": 15.99
  }'
```

### Delete Menu Item

```bash
curl -X DELETE http://localhost:8080/api/v1/menu-items/1
```

## 🧪 Testing Strategy

This project follows **Test-Driven Development (TDD)** principles:

1. **Unit Tests** - Test individual components in isolation
2. **Integration Tests** - Test component interactions
3. **API Tests** - Test REST endpoints
4. **Repository Tests** - Test data access layer

### Test Coverage

- Minimum 80% code coverage target
- Comprehensive validation testing
- Error scenario testing
- Business logic verification

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlite:quickbite.db
spring.jpa.hibernate.ddl-auto=update

# Server Configuration
server.port=8080

# Documentation
springdoc.swagger-ui.path=/swagger-ui.html
```

### Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set to `prod` for production
- `SPRING_DATASOURCE_URL`: Override database URL
- `SERVER_PORT`: Override server port

## 🐳 Docker Configuration

### Multi-stage Build

The Dockerfile uses multi-stage building for optimized image size:

1. **Build Stage**: Compile and package the application
2. **Runtime Stage**: Create minimal runtime image

### Features

- Non-root user for security
- Health check endpoint
- Volume for database persistence
- Optimized JVM settings

## 🔒 Security

- **Input Validation**: Comprehensive validation using Bean Validation
- **SQL Injection Protection**: Parameterized queries via JPA
- **Error Handling**: Sanitized error responses
- **Docker Security**: Non-root user execution

## 🚀 Deployment

### Production Considerations

1. **Database**: Consider upgrading to PostgreSQL for production
2. **Monitoring**: Add application monitoring (e.g., Micrometer, Prometheus)
3. **Logging**: Configure structured logging
4. **Security**: Add authentication and authorization
5. **Caching**: Implement Redis for performance optimization

### Environment Profiles

- **development**: Local development with detailed logging
- **test**: Testing environment with H2 in-memory database
- **production**: Production-optimized settings

## 📊 Performance

- **Response Time**: < 200ms for single item operations
- **Pagination**: Efficient handling of large datasets
- **Database**: Optimized queries with proper indexing
- **Caching**: Ready for caching layer implementation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow TDD: Write tests first, then implement
4. Commit changes (`git commit -m 'Add amazing feature'`)
5. Push to branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **QuickBite Development Team** - _Initial work_

## 📞 Support

For support and questions:

- Email: api-support@quickbite.com
- Issues: [GitHub Issues](https://github.com/harmandeepcentric/QuickBite/issues)

 ## One thing Copilot helped you achieve faster.

Overall, project development is very fast with Copilot. I started by providing it with some information, and it generated many files based on the description.

 ## One time you had to reject or refactor Copilot’s code

Most of the time, it gives good results, but I noticed it was using some outdated code syntax even though newer approaches are available. SonarQube helped me identify this, so I corrected the code and asked Copilot again to use the latest syntax.

---

**Built with ❤️ following TDD principles and Spring Boot best practices.**
