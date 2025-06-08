
---

## **Loja Online - API Concorrente** 🛒🚀  

Este é um projeto de **API REST concorrente** para uma loja online, desenvolvido em **Java** utilizando **Spring Boot** e o pacote `java.util.concurrent`. O sistema **não utiliza JPA nem banco de dados**, priorizando o controle de concorrência com estruturas da linguagem.

---

### **📌 Tecnologias utilizadas**  
- **Java 17+**  
- **Spring Boot 3.4.4**  
- **Gradle**  
- **Springdoc OpenAPI (Swagger)**  
- **ConcurrentHashMap** para gerenciamento concorrente  

---

### **🚀 Como executar o projeto**  

1. **Clone o repositório:**  
   ```sh
   git clone https://github.com/Vitoria-Maria0912/Lab10-Concorrente.git
   
   cd Lab10-Concorrente/loja-online
   ```

2. **Compile o projeto (limpando a pasta build):**  
   ```sh
   make clean-build
   ```

3. **Execute a aplicação (modo produção):**  
   ```sh
   make run
   ```

4. **(Alternativo) Rode em modo desenvolvimento com hot reload:**  
   ```sh
   make dev
   ```

---

### ✅ **Comandos disponíveis via Makefile**

| Comando       | Descrição                                                  |
|---------------|------------------------------------------------------------|
| `make build`  | Remove a pasta `build/` e recompila o projeto com o Gradle |
| `make run`    | Executa o `.jar` gerado após o build                       |
| `make dev`    | Executa diretamente o Spring Boot (`bootRun`)              |
| `make test`   | Roda os testes automatizados                               |
| `make clean`  | Remove apenas a pasta `build/` manual                      |

---

### **📚 Acesse a documentação Swagger**  

Com a aplicação executando, rode o seguinte comando no seu terminal:

```sh
make open-swagger
```

A interface Swagger permite explorar todos os endpoints, testar requisições e visualizar modelos de dados da API.

---

### **📌 Endpoints disponíveis**  

| Método  | Endpoint               | Descrição                          |
|---------|------------------------|-------------------------------------|
| `GET`   | `/products`            | Lista todos os produtos             |
| `POST`  | `/products`            | Adiciona um novo produto            |
| `GET`   | `/products/{id}`       | Busca um produto pelo ID            |
| `PUT`   | `/products/{id}/stock` | Atualiza o estoque de um produto    |
| `POST`  | `/purchase`            | Realiza uma compra                  |
| `GET`   | `/sales/report`        | Gera um relatório de vendas         |

#### **📍 Exemplo de requisição `POST /products`**
```json
{
  "id": "1",
  "name": "Smartphone",
  "price": 1999.99,
  "quantity": 10
}
```

#### **📍 Exemplo de requisição `POST /purchase`**
```json
{
  "id": "1",
  "quantity": 2
}
```

---
