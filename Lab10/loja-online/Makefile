# Makefile - Comandos úteis para o projeto Loja Online Concorrente

# Caminho para o wrapper Gradle
GRADLE = ./gradlew
SWAGGER = http://localhost:8080/swagger-ui/index.html

# Limpa manualmente a pasta build antes do build normal
clean-build:
	rm -rf build
	$(GRADLE) clean build

run:
	java -jar build/libs/lab10-0.0.1-SNAPSHOT.jar

clean:
	rm -rf build

# Executa a aplicação diretamente via Gradle
dev:
	$(GRADLE) bootRun
	
test:
	$(GRADLE) test

# Abre o Swagger UI no navegador padrão (Linux, MacOS ou Windows)
open-swagger:
	@echo "Abrindo Swagger UI..."
	@if command -v xdg-open > /dev/null; then \
		xdg-open $(SWAGGER); \
	elif command -v open > /dev/null; then \
		open $(SWAGGER); \
	elif command -v start > /dev/null; then \
		start $(SWAGGER); \
	else \
		echo "❌ Nenhum comando de abrir URL disponível (xdg-open, open, start)"; \
	fi
