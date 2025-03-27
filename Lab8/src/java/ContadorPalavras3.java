import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ContadorPalavras3 {
    public static void main(String[] args) {
    if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras3 <arquivos>");
            return;
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(10); // cria executor
        Map<String, Integer> resultados =new HashMap<>(); 
        List<Future<?>> futures = new ArrayList<>();
        
        for (String nomeArquivo : args) {
            Future<?> future = executor.submit(() -> {
                try {
                    int count = contarPalavras(nomeArquivo);
                    resultados.put(nomeArquivo, count);
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo " + nomeArquivo + ": " + e.getMessage());
                }
            });
            futures.add(future);
        }
        for (Future<?> future : futures) {
            try {
                future.get();
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
