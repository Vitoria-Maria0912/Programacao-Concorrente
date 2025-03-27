import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class ContadorPalavras2 {
    public static Semaphore semaphore;
    public static int total_sum;
        public static void main(String[] args) {
            if (args.length < 1) {
                System.err.println("Usage: java SumConcurrent filepath1 filepath2 filepathN");
                System.exit(1);
            }
    
            // Inicializa o semáforo com N/2 permissões
            semaphore = new Semaphore(args.length);

            // Cria uma thread para cada arquivo
            Thread[] threads = new Thread[args.length];
            for (int i = 0; i < args.length; i++) {
                threads[i] = new Thread(new FileContWordTask(args[i]));
                threads[i].start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Total de palavras: " + total_sum);
    
        }
    
        // Tarefa executada por cada thread
        static class FileContWordTask implements Runnable {
            private final String path;
    
            public FileContWordTask(String path) {
                this.path = path;
            }
    
            @Override
            public void run() {
                try {
                    // Adquire o semáforo para limitar o número de threads ativas
                    semaphore.acquire();

                // Calcula a soma do arquivo
                long sum = contarPalavras(path);
                total_sum+=sum;


                System.out.println(path + " : " + sum);
                
            } catch (IOException | InterruptedException e) {
                System.err.println("Error processing file: " + path);
                e.printStackTrace();
            } finally {
                // Libera o semáforo para permitir que outras threads executem
                semaphore.release();
            }
        }
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
