import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Etapa2 {

    private static BlockingQueue<Integer> queue;
    private static Random random = new Random();

    public Etapa2(Integer size) {
        queue = new ArrayBlockingQueue<>(size);
    }
    
    public static void producer(Integer n) {
        
        try {
            Thread.sleep(n); 
            for (int i = 0; i < 10000; i++){
                Integer nRandom = random.nextInt(10);
    
                queue.put(nRandom);
                System.out.println("Produzindo: "+ nRandom);
            }
        } catch (InterruptedException e) {}
    }

    public static void consumer() {

        try {
            Thread.sleep(600); 
            queue.take();
            System.out.println("Consumindo... ");
        } catch (InterruptedException e) {}
    }

    public static void main(String[] args) {
        System.out.println("Lab9 ...");    

        Integer nInteger = 10;

        ExecutorService executor = Executors.newFixedThreadPool(nInteger);

        executor.submit(() -> {
            while (true) {
                producer(nInteger);
            }
        });

        executor.submit(() -> {
            while (true) {
                consumer();
            }
        });
    }
}
