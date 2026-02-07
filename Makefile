.PHONY: help start stop restart clean build test backend frontend docker-up docker-down

help:
	@echo "Premier League Title Race - Development Commands"
	@echo ""
	@echo "Docker Commands:"
	@echo "  make docker-up     - Start all services with Docker"
	@echo "  make docker-down   - Stop all Docker services"
	@echo "  make docker-clean  - Stop and remove all containers and volumes"
	@echo ""
	@echo "Local Development:"
	@echo "  make backend       - Run backend locally"
	@echo "  make frontend      - Run frontend locally"
	@echo "  make build         - Build both backend and frontend"
	@echo "  make test          - Run all tests"
	@echo ""
	@echo "Utilities:"
	@echo "  make clean         - Clean build artifacts"
	@echo "  make logs          - View Docker logs"

docker-up:
	docker-compose up --build

docker-down:
	docker-compose down

docker-clean:
	docker-compose down -v
	docker system prune -f

backend:
	cd backend && mvn spring-boot:run

frontend:
	cd frontend && npm install && npm start

build:
	cd backend && mvn clean package
	cd frontend && npm install && npm run build

test:
	cd backend && mvn test
	cd frontend && npm test

clean:
	cd backend && mvn clean
	cd frontend && rm -rf dist node_modules

logs:
	docker-compose logs -f

restart: docker-down docker-up
