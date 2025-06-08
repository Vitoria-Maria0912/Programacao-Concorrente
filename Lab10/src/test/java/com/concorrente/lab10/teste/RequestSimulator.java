package com.concorrente.lab10.teste;

import java.net.URI;
import java.net.http.*;
import java.util.Random;
import java.util.concurrent.*;

public class RequestSimulator {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Random random = new Random();
    private static final int THREADS = 20;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        for (int i = 0; i < THREADS; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        int escolha = random.nextInt(4);
                        switch (escolha) {
                            case 0 -> consultarProdutos();
                            case 1 -> realizarCompra("1234", 1); // ID do produto a ser comprado
                            case 2 -> atualizarEstoque("1234", 5); // Simulando atualização
                            case 3 -> gerarRelatorio();
                        }
                        Thread.sleep(random.nextInt(500));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static void consultarProdutos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/products"))
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Consulta de produtos realizada.");
    }

    private static void realizarCompra(String id, int quantity) throws Exception {
        String json = String.format("{\"id\":\"%s\", \"quantity\":%d}", id, quantity);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/purchase"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Compra realizada.");
    }

    private static void atualizarEstoque(String id, int quantity) throws Exception {
        String json = String.format("{\"quantity\": %d}", quantity);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/products/" + id + "/stock"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Estoque atualizado.");
    }

    private static void gerarRelatorio() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/sales/report"))
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Relatório gerado.");
    }
}
