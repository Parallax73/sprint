# Sprint

A real-time Security Information and Event Management (SIEM) system built on a distributed microservices architecture.

## Overview

Sprint is a scalable, real-time SIEM platform designed to collect, process, analyze, and respond to security events across distributed infrastructure. The system leverages a microservices architecture to provide high availability, fault tolerance, and horizontal scalability for enterprise security monitoring.

## Architecture

Sprint is composed of five core microservices that work together to provide comprehensive security event management:

```
┌─────────────┐
│   Agent     │ ──┐
└─────────────┘   │
                  │     ┌──────────────┐      ┌─────────────┐
┌─────────────┐   ├────►│    Ingest    │─────►│  Analyzer   │
│   Agent     │ ──┤     └──────────────┘      └─────────────┘
└─────────────┘   │            │                      │
                  │            │                      │
┌─────────────┐   │            ▼                      ▼
│   Agent     │ ──┘     ┌──────────────┐      ┌─────────────┐
└─────────────┘         │    Kafka     │      │  Core API   │
                        └──────────────┘      └─────────────┘
                               │                      │
                               │                      │
                        ┌──────▼──────┐        ┌──────▼──────┐
                        │  Discovery  │        │  PostgreSQL │
                        └─────────────┘        └─────────────┘
```

### Microservices

- **Agent**:  Lightweight data collection agents deployed on monitored systems to gather security events and system logs
- **Ingest**: High-throughput ingestion service that receives events from agents and publishes to the event stream
- **Analyzer**: Real-time event analysis and correlation engine for threat detection and anomaly identification
- **Core API**: RESTful API gateway providing unified access to system functionality and data queries
- **Discovery**: Service registry and discovery mechanism enabling dynamic service location and health monitoring

### Infrastructure Components

- **Apache Kafka**:  Distributed event streaming platform for reliable message queuing and event processing
- **Apache ZooKeeper**: Coordination service for distributed configuration and synchronization
- **PostgreSQL**:  Persistent data store for events, alerts, and system configuration

## Technology Stack

- **Runtime**: Java (Spring Boot)
- **Build Tool**: Maven
- **Message Broker**: Apache Kafka 7.5.0
- **Database**: PostgreSQL 15
- **Containerization**: Docker & Docker Compose
- **Service Discovery**: Spring Cloud Netflix Eureka (via Discovery service)

## Prerequisites

- Docker 20.10 or higher
- Docker Compose 2.0 or higher
- Java 17 or higher (for local development)
- Maven 3.8 or higher (for local development)

## Quick Start

### Using Docker Compose

1. Clone the repository:
```bash
git clone https://github.com/Parallax73/sprint. git
cd sprint
```

2. Start the infrastructure services:
```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Apache Kafka on port 29092 (external) and 9092 (internal)
- Apache ZooKeeper on port 2181

3. Build and run individual microservices (see service-specific README files for detailed instructions)

### Verifying Installation

Check that all infrastructure services are running:
```bash
docker-compose ps
```

Expected output should show `postgres`, `kafka`, and `zookeeper` services in the `Up` state.

## Configuration

### Database Configuration

Default PostgreSQL credentials (change for production):
- **User**: sprint_user
- **Password**: admin
- **Database**: sprint_db
- **Port**: 5432

### Kafka Configuration

- **External Bootstrap Server**: localhost:29092 (for applications running on host)
- **Internal Bootstrap Server**: kafka:9092 (for containerized applications)
- **ZooKeeper Connection**: zookeeper:2181

## Development

### Project Structure

```
sprint/
├── agent/              # Data collection agent service
├── analyzer/           # Event analysis service
├── core-api/           # Core API gateway service
├── discovery/          # Service discovery service
├── ingest/             # Event ingestion service
├── compose.yaml        # Docker Compose infrastructure definition
└── README.md           # This file
```

### Building Services

Each microservice can be built independently using Maven:

```bash
cd <service-name>
./mvnw clean package
```

### Running Services Locally

1.  Ensure infrastructure services are running via Docker Compose
2. Navigate to the desired service directory
3. Run the service:
```bash
./mvnw spring-boot:run
```

Refer to individual service README files for service-specific configuration and port assignments.

## Service Documentation

Detailed documentation for each microservice:

- [Agent Service](./agent/README.md)
- [Analyzer Service](./analyzer/README.md)
- [Core API Service](./core-api/README.md)
- [Discovery Service](./discovery/README.md)
- [Ingest Service](./ingest/README.md)

## Deployment

### Production Considerations

- Configure appropriate resource limits for containers
- Enable authentication and encryption for Kafka
- Use strong database credentials and enable SSL connections
- Implement network policies to restrict service-to-service communication
- Configure persistent volumes for data retention
- Set up monitoring and logging aggregation
- Implement backup and disaster recovery procedures

### Scaling

Sprint microservices can be scaled horizontally:

```bash
docker-compose up -d --scale ingest=3 --scale analyzer=2
```

Kafka partitioning should be configured based on expected throughput and the number of consumer instances.

## Monitoring

Each microservice exposes health check endpoints via Spring Boot Actuator:

- Health:  `http://<service>:<port>/actuator/health`
- Metrics: `http://<service>:<port>/actuator/metrics`

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome.  Please ensure:

- Code follows existing style conventions
- All tests pass before submitting pull requests
- New features include appropriate test coverage
- Documentation is updated to reflect changes

## Support

For issues, questions, or contributions, please open an issue in the GitHub repository. 