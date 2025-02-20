import java.util.concurrent.locks.Condition;

class Producer {
    private final Buffer buffer;
    private final int maxItems;
    private final int sleepTime;
    private final int id;
    
    public Producer(int id, Buffer buffer, int maxItems, int sleepTime, Condition producerCondition) {
        this.id = id;
        this.buffer = buffer;
        this.maxItems = maxItems;
        this.sleepTime = sleepTime;
    }
    
    public Producer(int id2, Buffer buffer2, int maxItems2, int sleepTime2, Condition producerCondition) {
        //TODO Auto-generated constructor stub
    }

    public void produce() {
        for (int i = 0; i < maxItems; i++) {
            try {
                Thread.sleep(sleepTime);
                int item = (int) (Math.random() * 100);
                System.out.println("Producer " + id + " produced item " + item);
                buffer.put(item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
