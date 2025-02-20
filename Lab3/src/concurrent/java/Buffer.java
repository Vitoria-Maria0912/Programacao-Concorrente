import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class Buffer {
    private final List<Integer> data = new ArrayList<>();
    private final Semaphore mutex = new Semaphore(1);

    public void put(int value) {
        // região crítica - proteger
        // ver se o buffer tá cheio
        try {
            mutex.acquire();
            
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
        
            data.add(value);
            mutex.release();
            // região critica - proteger
            System.out.println("Inserted: " + value + " | Buffer size: " + data.size());
        }
    }

    public int remove() {
        if (!data.isEmpty()) {
            // região crítica - proteger
            try {

            mutex.acquire();
            } 
            int value = data.remove(0);
            // região crítica - proteger
            System.out.println("Removed: " + value + " | Buffer size: " + data.size());
            return value;
        }
        return -1;
    }

    public int size(){
        return this.data.size();
    }
}
