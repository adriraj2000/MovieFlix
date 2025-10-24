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
- **Spring AI (1.0.0-M3)** - OpenAI GPT-4 integration
- **Spring Security** - JWT authentication
- **Spring Data JPA** - Database ORM
- **MySQL 8.0** - User data storage
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
│   ├── MovieFlixApplication.java        # Main Spring Boot application
│   ├── controller/                      # REST API controllers
│   │   ├── AuthController.java          # Authentication endpoints
│   │   ├── HealthController.java        # Health check
│   │   └── RecommendationController.java # Main recommendation endpoint
│   ├── service/                         # Business services
│   │   ├── AuthService.java             # Authentication logic
│   │   ├── OmdbService.java             # OMDB API client
│   │   └── AIRecommendationService.java # AI-powered recommendations
│   ├── repository/                      # Data access
│   │   └── UserRepository.java          # User data access
│   ├── model/                           # Domain models & DTOs
│   │   ├── entities/                    # JPA entities
│   │   │   ├── User.java                # User entity
│   │   │   └── ApiResponse.java         # Response wrapper
│   │   └── dto/                         # Data Transfer Objects
│   │       ├── auth/                    # Auth DTOs
│   │       └── omdb/                    # OMDB API DTOs
│   ├── config/                          # Spring configurations
│   │   ├── SecurityConfig.java          # Security configuration
│   │   └── WebClientConfig.java         # WebClient for APIs
│   ├── common/                          # Common utilities
│   │   └── JwtUtil.java                 # JWT token utilities
│   └── exception/                       # Exception handling
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
├── src/main/resources/
│   ├── application.yml                  # Main configuration
│   └── application-dev.yml              # Dev profile
├── .env                                 # Environment variables (git-ignored)
├── docker-compose.yml                   # Docker services
├── Dockerfile                           # Application container
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
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

### Movie Recommendations (Main Feature)

```
GET    /api/recommendations?title={title}&year={year}    - Get AI-powered recommendations
```

**Example Usage:**

```bash
# Get recommendations for Inception
curl "http://localhost:8080/api/recommendations?title=Inception"

# Get recommendations with year (for disambiguation)
curl "http://localhost:8080/api/recommendations?title=Dune&year=2021"
```

**How It Works (3 Steps):**

1. **Step 1**: Fetch movie metadata from OMDB API
2. **Step 2**: Infer emotional vibe from metadata using OpenAI (atmosphere, themes, tone)
3. **Step 3**: Generate 5 similar movie recommendations based on the vibe

✨ **No database, no setup, no configuration!** Works on first use.

**Sample Response (Recommendations):**

```json
{
  "sourceTitle": "Inception",
  "sourceYear": "2010",
  "sourceGenre": "Action, Sci-Fi, Thriller",
  "vibe": "Inception is a mind-bending psychological thriller that explores the nature of reality through complex narrative layers. The film creates an intense, cerebral atmosphere with themes of memory, perception, and the subconscious. Its tone blends high-stakes action with philosophical depth.",
  "recommendations": [
    {
      "title": "The Matrix",
      "year": "1999",
      "reason": "Like Inception, it questions the nature of reality and features a protagonist discovering hidden layers of existence."
    },
    {
      "title": "Memento",
      "year": "2000",
      "reason": "Shares Inception's non-linear narrative structure and explores memory manipulation."
    },
    {
      "title": "Shutter Island",
      "year": "2010",
      "reason": "Features a similar psychological thriller atmosphere with reality-questioning themes."
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

# Get AI recommendations (Phase 4 - works immediately!)
curl "http://localhost:8080/api/recommendations?title=Inception"

# Get recommendations with year disambiguation
curl "http://localhost:8080/api/recommendations?title=The%20Matrix&year=1999"
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

- [x] **Phase 3**: OMDB Integration

  - [x] OMDB API client
  - [x] Movie metadata fetching
  - [x] Error handling for external API
  - [x] Response DTOs
  - [x] WebClient configuration

- [x] **Phase 4**: AI-Powered Movie Recommendations

  - [x] OpenAI chat integration (GPT-4)
  - [x] Vibe inference from metadata
  - [x] Recommendation generation
  - [x] Zero-setup architecture (works immediately)
  - [x] Single public endpoint

- [ ] **Phase 5**: Frontend Development

  - [ ] React setup with TypeScript
  - [ ] Tailwind styling
  - [ ] Component library

- [ ] **Phase 6**: Agent Orchestration (MCP)
  - [ ] Natural language processing
  - [ ] Tool definitions
  - [ ] Context management

## 🤝 Contributing

(Add contribution guidelines here)

## 📄 License

(Add license information here)

## 🔗 Links

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [OMDB API Documentation](http://www.omdbapi.com/)

---

**Status**: Phase 4 Complete - AI-Powered Movie Recommendations ✅ | Ready for Testing 🚀
