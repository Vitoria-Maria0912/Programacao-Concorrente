package com.concorrente.lab10;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.concorrente.lab10.dto.ProdutoDTO;
import com.concorrente.lab10.exception.ProdutoNaoEncontradoException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LojaOnlineApplicationTests {

	@Nested
	class ProdutoControllerTest {

		@LocalServerPort
		int port;

		private String baseUrl;
		private final RestTemplate restTemplate = new RestTemplate();

		@BeforeEach
		void setup() { this.baseUrl = "http://localhost:" + this.port; }

		@ParameterizedTest
		@ValueSource(strings = {"banana", "maca", "uva"})
		void testCadastrarProduto_concorrente(String id) throws InterruptedException {
			ExecutorService executor = Executors.newFixedThreadPool(3);

			Runnable task = () -> {
				ProdutoDTO produto = new ProdutoDTO(id, "Produto " + id, 5.0, 10);
				HttpEntity<ProdutoDTO> request = new HttpEntity<>(produto);
				try {
					restTemplate.postForEntity(baseUrl + "/products", request, Void.class);
				} catch (Exception e) {
					System.out.println("Erro ao cadastrar " + id);
				}
			};

			for (int i = 0; i < 3; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(3, TimeUnit.SECONDS);
		}

		@ParameterizedTest
		@ValueSource(strings = {"39059", "28443"})
		void testConsultarProduto_concorrente(String id) throws InterruptedException {
			ProdutoDTO produto = new ProdutoDTO(id, "Produto " + id, 10.0, 20);
			restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), Void.class);

			ExecutorService executor = Executors.newFixedThreadPool(2);

			Runnable task = () -> {
				ResponseEntity<ProdutoDTO> response = restTemplate.getForEntity(baseUrl + "/products/" + id, ProdutoDTO.class);
				assertTrue(response.getStatusCode().is2xxSuccessful());
			};

			for (int i = 0; i < 5; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(3, TimeUnit.SECONDS);
		}

		@ParameterizedTest
		@CsvSource({ "2847839, 1", "38783, 2", "48754, 4", "123456, 6", "987654, 8", "1234567, 10" })
		void testAltasTaxasDeConsultaDeProdutos(int id, int threadCount) throws InterruptedException {
			Map<String, Object> produto = Map.of(
				"id", id,
				"name", "Produto Teste",
				"price", 10.0,
				"quantity", 5
			);
			restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), Void.class);

			ExecutorService executor = Executors.newFixedThreadPool(threadCount);

			Runnable task = () -> {
				ResponseEntity<ProdutoDTO> response = restTemplate.getForEntity(baseUrl + "/products/" + id, ProdutoDTO.class);
				assertTrue(response.getStatusCode().is2xxSuccessful());
			};

			for (int i = 0; i < 5; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(3, TimeUnit.SECONDS);
		}

		@ParameterizedTest
		@CsvSource({ "id3985, 15, 1", "id13982, 30, 5", "id3849083, 45, 10" })
		void testAtualizarEstoqueEmSimultaneo(String id, int novoEstoque, int threadCount) throws InterruptedException {
			ProdutoDTO produto = new ProdutoDTO(id, "Produto " + id, 1.0, 10);
			restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), Void.class);

			ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			Runnable task = () -> {
				ProdutoDTO update = new ProdutoDTO(null, null, 0.0, novoEstoque);
				restTemplate.exchange(baseUrl + "/products/" + id + "/stock", HttpMethod.PUT, new HttpEntity<>(update), Void.class);
			};

			for (int i = 0; i < 3; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(3, TimeUnit.SECONDS);
		}

		@ParameterizedTest
		@ValueSource(ints = {2, 4, 6, 10})
		void testCompraConcorrenteMesmoProduto(int threadCount) throws InterruptedException {
		    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		    CountDownLatch startLatch = new CountDownLatch(1);
		    CountDownLatch doneLatch = new CountDownLatch(threadCount);

		    Runnable task = () -> {
		        try {
		            startLatch.await(); // Aguarda o sinal para começar
		            Map<String, Object> body = Map.of("id", "caneta", "quantity", 2);
		            restTemplate.postForEntity(baseUrl + "/purchase", body, Void.class);
		            System.out.println("✔ Compra OK");
		        } catch (Exception e) {
		            System.out.println("✖ Falha na compra: " + e.getMessage());
		        } finally {
		            doneLatch.countDown();
		        }
		    };

		    for (int i = 0; i < threadCount; i++) {
		        executor.submit(task);
		    }

		    startLatch.countDown(); // Libera todas as threads ao mesmo tempo
		    doneLatch.await(); // Espera todas terminarem
		    executor.shutdown();
		}
	}

	@Nested
	class VendaControllerTest {

		@LocalServerPort
		int port;

		private String baseUrl;
		private final RestTemplate restTemplate = new RestTemplate();

		@BeforeEach
		void setup() { this.baseUrl = "http://localhost:" + this.port; }

		@ParameterizedTest
		@ValueSource(strings = {"92839", "39823u", "1234"})
		void testCompraConcorrente(String id) throws InterruptedException {
			ProdutoDTO produto = new ProdutoDTO(id, "Produto " + id, 1.0, 10);
			restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), ProdutoNaoEncontradoException.class);

			ExecutorService executor = Executors.newFixedThreadPool(4);
			Runnable task = () -> {
				Map<String, Object> body = Map.of("id", id, "quantity", 3);
				try {
					restTemplate.postForEntity(baseUrl + "/purchase", body, Void.class);
				} catch (Exception e) {}
			};

			for (int i = 0; i < 5; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(4, TimeUnit.SECONDS);
		}

		@ParameterizedTest
		@ValueSource(strings = {"3984", "495", "1234"})
		void testCompraInexistenteConcorrente(String id) throws InterruptedException {

			ExecutorService executor = Executors.newFixedThreadPool(4);
			Runnable task = () -> {
				Map<String, Object> body = Map.of("id", id, "quantity", 3);
				try {
					restTemplate.postForEntity(baseUrl + "/purchase", body, ProdutoNaoEncontradoException.class);
				} catch (Exception e) {
					System.out.println("Erro na compra de " + id);
				}
			};

			for (int i = 0; i < 5; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(4, TimeUnit.SECONDS);
		}

		@ParameterizedTest
		@CsvSource({ "28738, 0", "39850, 1", "1234, 2" })
		void testCompraEstoqueInsuficienteConcorrente(String id, int quantity) throws InterruptedException {
			ProdutoDTO produto = new ProdutoDTO(id, "Produto " + id, 1.0, quantity);
			try {
				restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), Void.class);
				
			} catch (Exception e) { }

			ExecutorService executor = Executors.newFixedThreadPool(4);
			Runnable task = () -> {
				Map<String, Object> body = Map.of("id", id, "quantity", 3);
				try {
					restTemplate.postForEntity(baseUrl + "/purchase", body, Void.class);
				} catch (Exception e) {}
			};

			for (int i = 0; i < 5; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(4, TimeUnit.SECONDS);
		}

		@RepeatedTest(5)
		void testRelatorioDuranteUsoConcorrente(RepetitionInfo repetitionInfo) {
			String id = "produto-" + repetitionInfo.getCurrentRepetition();

			Map<String, Object> produto = Map.of(
				"id", id,
				"name", "Produto Teste",
				"price", 10.0,
				"quantity", 5
			);
			restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), Void.class);

			// Realizar compra
			Map<String, Object> compra = Map.of("id", id, "quantity", 1);
			restTemplate.postForEntity(baseUrl + "/purchase", new HttpEntity<>(compra), Void.class);

			// Gerar relatório
			ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/sales/report", Map.class);

			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertTrue(((Integer) response.getBody().get("totalSales")) >= 1);
		}

		@ParameterizedTest
		@CsvSource({ "1, 287409", "5, 39850", "10, 0498" })
		void testRelatorioDuranteUsoIntensoDaLoja(int threadCount, String id) throws InterruptedException {

			Map<String, Object> produto = Map.of(
				"id", id,
				"name", "Produto Teste",
				"price", 10.0,
				"quantity", 5
			);

			ExecutorService executor = Executors.newFixedThreadPool(4);
			Runnable task = () -> {
				Map<String, Object> compra = Map.of("id", id, "quantity", 3);
				try {
					restTemplate.postForEntity(baseUrl + "/products", new HttpEntity<>(produto), Void.class);
					restTemplate.postForEntity(baseUrl + "/purchase", compra, Void.class);
		
					// Gerar relatório
					ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/sales/report", Map.class);
		
					assertEquals(HttpStatus.OK, response.getStatusCode());
					assertTrue(((Integer) response.getBody().get("totalSales")) >= 1);
				} catch (Exception e) {}
			};

			for (int i = 0; i < 5; i++) executor.submit(task);
			executor.shutdown();
			executor.awaitTermination(4, TimeUnit.SECONDS);
			
		}
	}
}
