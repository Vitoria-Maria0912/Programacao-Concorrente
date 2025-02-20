import java.util.concurrent.locks.Condition;

public class Main {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Use: java Main <num_producers> <max_items_per_producer> <producing_time> <num_consumers> <consuming_time>");
            return;
        }
        
        int numProducers = Integer.parseInt(args[0]);
        int maxItemsPerProducer = Integer.parseInt(args[1]);
        int producingTime = Integer.parseInt(args[2]);
        int numConsumers = Integer.parseInt(args[3]);
        int consumingTime = Integer.parseInt(args[4]);
        
        Buffer buffer = new Buffer();
        Condition producerCondition;
        Condition consumerCondition;

        Thread produtor = new Thread(() -> {
            for (int i = 1; i <= numProducers; i++) { produzir(i, buffer, maxItemsPerProducer, consumingTime, producerCondition); }
        });      
        
        
        Thread consumidor = new Thread(() -> {
            for (int i = 1; i <= numConsumers; i++) { consumir(i, buffer, producingTime, consumerCondition);}
        }); 

        produtor.start();
        consumidor.start();

    }

    private static void produzir(int id, Buffer buffer, int maxItems, int sleepTime, Condition producerCondition){
        Producer producer = new Producer(id, buffer, maxItems, sleepTime, producerCondition);
        producer.produce();
    }

    private static void consumir(int id, Buffer buffer, int sleepTime, Condition consumerCondition){
        Consumer consumer = new Consumer(id, buffer, sleepTime, consumerCondition);
        consumer.process();
    }
}
