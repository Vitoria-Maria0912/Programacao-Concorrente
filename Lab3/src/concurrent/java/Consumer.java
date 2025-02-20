import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Consumer {
    private final Buffer buffer;
    private final int sleepTime;
    private final int id;
    private final Lock mutex = new ReentrantLock();
    private final Condition producerCondition;
    private final Condition consumerCondition;

    public Consumer(int id, Buffer buffer, int sleepTime) {
        this.id = id;
        this.buffer = buffer;
        this.sleepTime = sleepTime;
        this.producerCondition = mutex.newCondition();
        this.consumerCondition = mutex.newCondition();
    
    }

    public void process() {
        mutex.lock();
        try {
            while (true) {
                while (buffer.size() != 0) {
                    consumerCondition.wait();
                }

                int item = buffer.remove();
                if (item == -1)
                    break;
                System.out.println("Consumer " + id + " consumed item " + item);
                producerCondition.notify();
            }
            // Thread.sleep(sleepTime);
        }

        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {

            mutex.unlock();
        }
    }

}
