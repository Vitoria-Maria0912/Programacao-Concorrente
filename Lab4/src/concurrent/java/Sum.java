import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class Sum {

    private static long totalSoma = 0;

    private static Semaphore semaforo;

    private static Map<Long, Set<String>> maps = new HashMap<>();

    public static int sum(FileInputStream fis) throws IOException {
        
	int byteRead;
        int sum = 0;
        
        while ((byteRead = fis.read()) != -1) {
        	sum += byteRead;
        }

        return sum;
    }

    public static long sum(String path) throws IOException {

        Path filePath = Paths.get(path);
        if (Files.isRegularFile(filePath)) {
       	    FileInputStream fis = new FileInputStream(filePath.toString());
            return sum(fis);
        } else {
            throw new RuntimeException("Non-regular file: " + path);
        }
    }

    public static class InnerSum implements Runnable {

        private final String path;

        public InnerSum(String path){
            this.path = path;
        }

        @Override
        public void run() {
            try {
                semaforo.acquire();
                long sum = sum(this.path);
                System.out.println(path + " : " + sum); 

                totalSoma += sum;
                
                maps.get(sum).add(path);
                maps.put(sum, maps.get(sum));

            } catch (Exception e) {

            } finally {
                semaforo.release();
            }
        }
        
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
            System.exit(1);
        }

        semaforo = new Semaphore((args.length / 2));

        Thread[] threads = new Thread[args.length];

        for (int i = 0; i < args.length; i++){
            threads[i] = new Thread (new InnerSum(args[i]));
            threads[i].start();
        }

        for(Thread thread: threads){
            thread.join();
        }

        System.out.println("Soma total : " + totalSoma);

        for(Long key: maps.keySet()){
            // Map<Long, Set<String>>
            System.out.println("Sum: " + maps.get(key));
        }

    }
}
