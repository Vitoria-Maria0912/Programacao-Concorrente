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

public class Etapa1 {

    private static BlockingQueue<Integer> queue;
    private static Random random = new Random();

    public Etapa1(Integer size) {
        queue = new ArrayBlockingQueue<>(size);
    }
    
    public static void producer(Integer n) {
        
        try {
            Thread.sleep(n); 
            Integer nRandom = random.nextInt(10);

            queue.put(nRandom);
            System.out.println("Produzindo: "+ nRandom);
        } catch (InterruptedException e) {}
    }

    public static void consumer(int n) {

        try {
            Thread.sleep(n); 
            queue.take();
            System.out.println("Consumindo... ");
        } catch (InterruptedException e) {}
    }

    public static void main(String[] args) {
        System.out.println("Lab9 ...");    

        Integer nInteger = 10;

        ExecutorService executor = Executors.newFixedThreadPool(nInteger);

        List<Future<?>> futures = new ArrayList<>();

        while (true) {
            Future<?> future = executor.submit(() -> {
                producer(nInteger);
                consumer(nInteger);
            });
            futures.add(future);
        }
    }
}
