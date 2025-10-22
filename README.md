# 🎬 MovieFlix - AI-Powered Movie Recommendation System

A modern, intelligent movie recommendation system powered by Spring AI and OpenAI GPT-4, providing personalized movie suggestions through natural language conversations.

## 🏗️ Architecture

**Clean Architecture-Based Design:**

```
MovieFlix (Single Spring Boot Application)
└── com.example.MovieFlix/
    ├── controller/          # REST API endpoints
    ├── service/             # Business logic layer
    ├── repository/          # Data access layer
    ├── model/               # Entities & DTOs
    ├── config/              # Spring configurations
    ├── security/            # Security & JWT
    └── exception/           # Exception handling
```

## 🚀 Tech Stack

### Backend

- **Spring Boot 3.5.6** - Main framework
- **Spring AI (1.0.0-M3)** - OpenAI GPT-4 integration for vibe analysis
- **Spring Security** - JWT authentication
- **Spring Data JPA** - Database ORM
- **MySQL 8.0** - Primary database
- **WebClient** - Reactive HTTP client for external APIs

### Frontend (Coming Soon)

- **React 18+** with TypeScript
- **Tailwind CSS** - Styling
- **Vite** - Build tool

### External APIs

- **OpenAI GPT-4** - Natural language processing & recommendations
- **OMDB API** - Movie metadata

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.9+
- Docker & Docker Compose (for containerized setup)
- MySQL 8.0 (if running locally without Docker)
- Redis (if running locally without Docker)

## 🔑 API Keys Required

1. **OpenAI API Key**: Get from [OpenAI Platform](https://platform.openai.com/) - Required for AI-powered vibe analysis
2. **OMDB API Key**: Get free key from [OMDB API](http://www.omdbapi.com/apikey.aspx) - Required for movie metadata

## ⚙️ Setup Instructions

### 1. Create Environment File

```bash
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# API Keys (Required)
OPENAI_API_KEY=sk-your-actual-openai-key
OMDB_API_KEY=your-actual-omdb-key

# Security
JWT_SECRET=your-secure-random-64-character-secret-key-here

# Database
MYSQL_URL=jdbc:mysql://localhost:3306/movieflix?createDatabaseIfNotExist=true
MYSQL_USERNAME=root
MYSQL_PASSWORD=password
```

### 2. Option 1: Docker (Recommended)

1. **Clone the repository**

```bash
cd MovieFlix
```

2. **Create `.env` file** (use `.env.example` as template)

```bash

# Edit .env and add your API keys
OPENAI_API_KEY=sk-your-actual-openai-key
OMDB_API_KEY=your-actual-omdb-key
```

3. **Start all services**

```bash
docker-compose up -d
```

4. **Check logs**

```bash
docker-compose logs -f app
```

5. **Access the application**

- API: http://localhost:8080
- Health: http://localhost:8080/actuator/health

### Option 2: Local Development

1. **Start MySQL**

```bash
# Using Docker
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=movieflix \
  -p 3306:3306 \
  mysql:8.0
```

2. **Start Redis**

```bash
# Using Docker
docker run -d --name redis -p 6379:6379 redis:7-alpine
```

3. **Configure environment variables**

```bash
export OPENAI_API_KEY=sk-your-key
export OMDB_API_KEY=your-key
export MYSQL_URL=jdbc:mysql://localhost:3306/movieflix
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=password
```

4. **Build and run**

```bash
mvn clean install
mvn spring-boot:run
```

## 📁 Project Structure

```
MovieFlix/
├── src/main/java/com/example/MovieFlix/
│   ├── MovieFlixApplication.java     # Main Spring Boot application
│   ├── controller/                   # REST API controllers
│   │   ├── AuthController.java       # Authentication endpoints
│   │   ├── HealthController.java     # Health check
│   │   ├── MovieController.java      # Movie search & details
│   │   └── RecommendationController.java  # AI recommendations
│   ├── service/                      # Business services
│   │   ├── AuthService.java          # Authentication logic
│   │   ├── OmdbService.java          # OMDB API client
│   │   ├── MovieVibeService.java     # AI vibe analysis
│   │   └── MovieRecommendationService.java  # AI recommendations
│   ├── repository/                   # Data access repositories
│   │   └── UserRepository.java       # User data access
│   ├── model/                        # Domain models & DTOs
│   │   ├── entities/                 # JPA entities
│   │   │   ├── User.java             # User entity
│   │   │   └── ApiResponse.java      # Response wrapper
│   │   └── dto/                      # Data Transfer Objects
│   │       ├── auth/                 # Auth DTOs
│   │       ├── omdb/                 # OMDB API DTOs
│   │       ├── MovieVibeResponse.java
│   │       ├── RecommendedMovie.java
│   │       └── RecommendationResponse.java
│   ├── config/                       # Spring configurations
│   │   ├── SecurityConfig.java       # Security & authentication
│   │   └── WebClientConfig.java      # WebClient for APIs
│   ├── common/                       # Common utilities
│   │   └── JwtUtil.java              # JWT token utilities
│   └── exception/                    # Exception handling
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
├── src/main/resources/
│   ├── application.yml               # Main configuration
│   └── application-dev.yml           # Dev profile
├── .env                              # Environment variables (git-ignored)
├── docker-compose.yml                # Docker services
├── Dockerfile                        # Application container
├── pom.xml                           # Maven dependencies
└── README.md                         # This file
```

## 🔌 API Endpoints

### Health Check

```
GET    /api/health/ping          - Simple health check
GET    /actuator/health          - Detailed health status
```

### Authentication

```
POST   /api/auth/register        - Register new user
POST   /api/auth/login           - Login and get JWT token
```

### Movies & Search

```
GET    /api/movies/search?s={query}&page={page}  - Search movies
GET    /api/movies/{title}?year={year}           - Get movie details by title
```

### AI-Powered Recommendations (Phase 4 ✅)

```
GET    /api/recommendations/{title}              - Get vibe analysis + recommendations
GET    /api/recommendations/{title}?year={year}  - Get recommendations with year disambiguation
GET    /api/recommendations/vibe/{title}         - Analyze movie vibe only (no recommendations)
```

**Example Usage:**

```bash
# Get full recommendations for Inception
curl "http://localhost:8080/api/recommendations/Inception"

# Disambiguate by year (e.g., Dune 2021 vs 1984)
curl "http://localhost:8080/api/recommendations/Dune?year=2021"

# Just analyze the vibe without recommendations
curl "http://localhost:8080/api/recommendations/vibe/The%20Dark%20Knight"
```

**Sample Response:**

```json
{
  "vibe": {
    "title": "Inception",
    "year": "2010",
    "vibe": "Mind-bending thriller",
    "themes": ["reality", "dreams", "identity", "time"],
    "moods": ["suspenseful", "cerebral", "intense"],
    "reasoning": "The film creates a complex emotional atmosphere..."
  },
  "recommendations": [
    {
      "title": "The Matrix",
      "year": "1999",
      "reason": "Shares the exploration of layered realities..."
    },
    {
      "title": "Memento",
      "year": "2000",
      "reason": "Similar non-linear narrative structure..."
    }
  ]
}
```

## 🧪 Testing

### Unit Tests

```bash
# Run all tests
mvn test

# Skip tests during build
mvn clean install -DskipTests
```

### API Testing (Phase 4)

```bash
# Health check
curl http://localhost:8080/api/health/ping

# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Search movies
curl "http://localhost:8080/api/movies/search?s=inception&page=1"

# Get movie details
curl "http://localhost:8080/api/movies/Inception?year=2010"

# Get AI recommendations (Phase 4)
curl "http://localhost:8080/api/recommendations/Inception"

# Analyze vibe only
curl "http://localhost:8080/api/recommendations/vibe/The%20Dark%20Knight"
```

## 🔧 Development

### Build

```bash
mvn clean package
```

### Run locally

```bash
mvn spring-boot:run
```

### Docker build

```bash
docker build -t movieflix:latest .
```

## 📊 Database Migrations

Using Flyway for version-controlled database changes:

```bash
# Migrations are located in: src/main/resources/db/migration/
# Format: V{version}__{description}.sql
# Example: V1__create_users_table.sql

# Flyway runs automatically on application startup
```

## 🌐 Environment Variables

| Variable                 | Description               | Default                               | Required |
| ------------------------ | ------------------------- | ------------------------------------- | -------- |
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | dev                                   | No       |
| `SERVER_PORT`            | Server port               | 8080                                  | No       |
| `MYSQL_URL`              | MySQL JDBC URL            | jdbc:mysql://localhost:3306/movieflix | Yes      |
| `MYSQL_USERNAME`         | MySQL username            | root                                  | Yes      |
| `MYSQL_PASSWORD`         | MySQL password            | -                                     | Yes      |
| `OPENAI_API_KEY`         | OpenAI API key            | -                                     | Yes      |
| `OMDB_API_KEY`           | OMDB API key              | -                                     | Yes      |
| `JWT_SECRET`             | JWT signing secret        | -                                     | Yes      |

## 📈 Monitoring

Access monitoring endpoints:

- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Prometheus: http://localhost:8080/actuator/prometheus

## 🚦 Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database health
curl http://localhost:8080/actuator/health/db

# Redis health
curl http://localhost:8080/actuator/health/redis
```

## 🐛 Troubleshooting

### Application won't start

1. Check if MySQL and Redis are running
2. Verify API keys in environment variables
3. Check logs: `docker-compose logs -f app`

### Database connection issues

```bash
# Check MySQL is accessible
docker exec -it movieflix-mysql mysql -u root -p

# Verify database exists
SHOW DATABASES;

# Use the movieflix database
USE movieflix;

# Show tables
SHOW TABLES;
```

### OpenAI API issues

1. Verify your API key is valid and has credits
2. Check the API key format (should start with `sk-`)
3. Ensure the key is properly set in `.env` file
4. Restart the application after changing environment variables

## 📝 Development Phases

- [x] **Phase 1**: Foundation & Setup

  - [x] Project structure
  - [x] Dependencies setup
  - [x] Docker configuration
  - [x] Configuration files

- [x] **Phase 2**: Authentication & Security

  - [x] JWT implementation
  - [x] User registration/login
  - [x] User entity and repository
  - [x] Security configuration
  - [x] Global exception handling
  - [x] MySQL database integration

- [x] **Phase 3**: Metadata Service

  - [x] OMDB API client integration
  - [x] Movie search functionality
  - [x] Movie details retrieval
  - [x] Error handling for external API
  - [x] Response DTOs
  - [x] WebClient configuration
  - [x] Integration testing

- [x] **Phase 4**: AI-Powered Vibe Analysis & Recommendations

  - [x] Spring AI OpenAI integration
  - [x] Vibe analysis service using GPT-4
  - [x] AI recommendation engine
  - [x] Recommendation controller with REST endpoints
  - [x] Simple, focused architecture (no user tracking)

- [ ] **Phase 5**: Enhanced AI Features

  - [ ] Conversation history & context
  - [ ] Vector embeddings for semantic search
  - [ ] Multi-turn recommendation dialogues

- [ ] **Phase 6**: Agent Orchestration (MCP)

  - [ ] Natural language processing
  - [ ] Tool definitions
  - [ ] Context management

- [ ] **Phase 7**: Frontend
  - [ ] React setup
  - [ ] Tailwind styling
  - [ ] Component library

## 🤝 Contributing

(Add contribution guidelines here)

## 📄 License

(Add license information here)

## 🔗 Links

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [OMDB API Documentation](http://www.omdbapi.com/)

---

**Status**: Phase 4 - AI-Powered Vibe Recommendations Complete ✅ | Ready for Testing 🚀
