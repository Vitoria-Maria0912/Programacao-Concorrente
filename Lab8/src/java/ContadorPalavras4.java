import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ContadorPalavras4 {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras4 <arquivos>");
            return;
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Map.Entry<String, Integer>>> futures = new ArrayList<>();
        
        for (String nomeArquivo : args) {
            Future<Map.Entry<String, Integer>> future = executor.submit(() -> {
                try {
                    int count = contarPalavras(nomeArquivo);
                    return new AbstractMap.SimpleEntry<>(nomeArquivo, count);
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo " + nomeArquivo + ": " + e.getMessage());
                    return new AbstractMap.SimpleEntry<>(nomeArquivo, 0);
                }
            });
            futures.add(future);
        }
        
        Map<String, Integer> resultados = new HashMap<>();
        for (Future<Map.Entry<String, Integer>> future : futures) {
            try {
                Map.Entry<String, Integer> resultado = future.get();
                resultados.put(resultado.getKey(), resultado.getValue());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Erro ao aguardar execução da tarefa: " + e.getMessage());
            }
        }
        
        executor.shutdown();
        
        int totalPalavras = resultados.values().stream().mapToInt(Integer::intValue).sum();
        resultados.forEach((arquivo, count) -> System.out.println(arquivo + ": " + count + " palavras"));
        System.out.println("Total de palavras: " + totalPalavras);
    }

    static int contarPalavras(String nomeArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        int count = 0;
        String linha;
        while ((linha = br.readLine()) != null) {
            count += linha.split("\\s+").length;
        }
        br.close();
        return count;
    }
}