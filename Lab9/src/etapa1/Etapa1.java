import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Etapa1 {

    private BlockingQueue<Integer> queue;
    private Random random = new Random();

    public Etapa1(int size) {
        this.queue = new ArrayBlockingQueue<>(size);
    }
    
    public void producer(int n) {
        Time sleep = new Time(n);
        
        try {
            sleep.wait(n); ////////////
            int nRandom = random.nextInt(10);

            this.queue.put(nRandom);
        } catch (InterruptedException e) {}
    }

    public void consumer(int n) {


    }
}
