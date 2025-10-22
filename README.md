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
- **Spring AI** - OpenAI GPT-4 integration
- **Spring Security** - JWT authentication
- **Spring Data JPA** - Database ORM
- **MySQL 8.0** - Primary database
- **Redis** - Caching layer

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

1. **OpenAI API Key**: Get from [OpenAI Platform](https://platform.openai.com/) (Phase 5)
2. **OMDB API Key**: Get free key from [OMDB API](http://www.omdbapi.com/apikey.aspx) (Phase 3)

## ⚙️ Setup Instructions

### 1. Create Environment File

```bash
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# Add your actual API keys here
OPENAI_API_KEY=sk-your-actual-openai-key
OMDB_API_KEY=your-actual-omdb-key
JWT_SECRET=your-secure-random-secret

# Database (when you need it in Phase 2+)
MYSQL_PASSWORD=your-mysql-password
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
│   ├── MovieFlixApplication.java  # Main Spring Boot application
│   ├── controller/                # REST API controllers
│   │   └── HealthController.java
│   ├── service/                   # Business services
│   │   # (Auth, Movie, Metadata, Recommendation services)
│   ├── repository/                # Data access repositories
│   │   # (JPA repositories)
│   ├── model/                     # Domain models & DTOs
│   │   └── ApiResponse.java
│   ├── config/                    # Spring configurations
│   │   ├── CorsConfig.java
│   │   └── RedisConfig.java
│   ├── security/                  # Security & JWT
│   │   └── SecurityConfig.java
│   └── exception/                 # Exception handling
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
├── src/main/resources/
│   ├── application.yml            # Main configuration
│   ├── application-dev.yml        # Dev profile
│   ├── application-prod.yml       # Prod profile
│   └── db/migration/              # Flyway migrations
├── docker-compose.yml             # Docker services
├── Dockerfile                     # Application container
├── pom.xml                        # Maven dependencies
└── README.md                      # This file
```

## 🔌 API Endpoints (Coming in Next Phases)

### Authentication

```
POST   /api/auth/register    - Register new user
POST   /api/auth/login       - Login and get JWT token
POST   /api/auth/refresh     - Refresh JWT token
GET    /api/auth/me          - Get current user info
```

### Movies & Search

```
GET    /api/movies/search?q={query}   - Search movies
GET    /api/movies/{imdbId}           - Get movie details
```

### Recommendations

```
POST   /api/recommendations           - Get personalized recommendations
GET    /api/recommendations/history   - Get recommendation history
POST   /api/chat                      - Natural language movie chat
```

### User Preferences

```
GET    /api/users/preferences         - Get user preferences
POST   /api/users/preferences         - Update preferences
GET    /api/users/favorites           - Get favorites
POST   /api/users/favorites           - Add to favorites
GET    /api/users/watchlist           - Get watchlist
POST   /api/users/watchlist           - Add to watchlist
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
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
| `MYSQL_URL`              | MySQL JDBC URL            | jdbc:mysql://localhost:3306/movieflix | Yes      |
| `MYSQL_USERNAME`         | MySQL username            | root                                  | Yes      |
| `MYSQL_PASSWORD`         | MySQL password            | password                              | Yes      |
| `REDIS_HOST`             | Redis host                | localhost                             | Yes      |
| `REDIS_PORT`             | Redis port                | 6379                                  | Yes      |
| `OPENAI_API_KEY`         | OpenAI API key            | -                                     | Yes      |
| `OMDB_API_KEY`           | OMDB API key              | -                                     | Yes      |
| `JWT_SECRET`             | JWT signing secret        | -                                     | Yes      |
| `CORS_ALLOWED_ORIGINS`   | CORS origins              | http://localhost:3000                 | No       |

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
```

### Redis connection issues

```bash
# Check Redis is accessible
docker exec -it movieflix-redis redis-cli ping
```

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

- [ ] **Phase 4**: Core Movie Service

  - [ ] Database schema
  - [ ] User preferences
  - [ ] Watch history

- [ ] **Phase 5**: AI Recommendation Service

  - [ ] Spring AI integration
  - [ ] Recommendation engine
  - [ ] Embeddings

- [ ] **Phase 6**: Agent Orchestration

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

**Status**: Phase 3 - Metadata Service Complete ✅ | Ready for Phase 4 - Core Movie Service 🚀
