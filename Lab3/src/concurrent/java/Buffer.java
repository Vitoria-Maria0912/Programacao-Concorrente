import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Buffer {
    private final List<Integer> data = new ArrayList<>();
    private final Lock mutex = new ReentrantLock();

    public void put(int value) {
        // região crítica - proteger
        // ver se o buffer tá cheio
        try {
            mutex.lock();
        } finally {
        
            data.add(value);
            mutex.unlock();
            // região critica - proteger
            System.out.println("Inserted: " + value + " | Buffer size: " + data.size());
        }
    }

    public int remove() {
        if (!data.isEmpty()) {
            // região crítica - proteger
            try {

            mutex.lock();
            } finally { mutex.unlock(); }

            int value = data.remove(0);
            // região crítica - proteger
            System.out.println("Removed: " + value + " | Buffer size: " + data.size());
            return value;
        }
        return -1;
    }

    public int size(){ return this.data.size(); }
}
