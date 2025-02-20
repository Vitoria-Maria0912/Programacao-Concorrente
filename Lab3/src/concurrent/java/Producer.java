import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Producer {
    private final Buffer buffer;
    private final int maxItems;
    private final int sleepTime;
    private final int id;
    private final Lock mutex;
    private final Condition producer;
    
    public Producer(int id, Buffer buffer, int maxItems, int sleepTime) {
        this.id = id;
        this.buffer = buffer;
        this.maxItems = maxItems;
        this.sleepTime = sleepTime;
        this.mutex = new ReentrantLock();
        this.producer = mutex.newCondition();
    }
    
    public void produce() {
        for (int i = 0; i < maxItems; i++) {
            try {
                mutex.lock();
                    Thread.sleep(sleepTime);
                    while (buffer.size() >= 100) { producer.await(); }
                    int item = (int) (Math.random() * 100);
                    System.out.println("Producer " + id + " produced item " + item);
                    buffer.put(item);
                    // consumer.unlock();
                mutex.unlock();

            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
}
