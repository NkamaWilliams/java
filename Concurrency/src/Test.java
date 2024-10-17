import java.lang.Runnable;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Test {
    static ProduceConsume producerConsumer = new ProduceConsume();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(new Producer());
        executor.execute(new Consumer());
        executor.shutdown();

        while (!executor.isShutdown()){

        }
        System.out.println("Program complete");
    }

    static class Producer implements Runnable{
        @Override
        public void run(){
            try{
                for (int i = 0; i < 10; i++) {
                    producerConsumer.produce();
                    Thread.sleep((int) (Math.random()*10) + 1);
                }
            } catch (InterruptedException err){
                System.out.println(err.getMessage());
            }
        }
    }

    static class Consumer implements Runnable{
        @Override
        public void run(){
            int produce;
            try{
                for (int i = 0; i < 10; i++) {
                    produce = producerConsumer.consume();
                    System.out.println("\t\t\t\tConsumed " + produce);
                    Thread.sleep((int) (Math.random() * 10) + 1);
                }
            } catch (InterruptedException err){
                System.out.println(err.getMessage());
            }
        }
    }
}

class ProduceConsume{
    private LinkedList<Integer> product = new LinkedList<>();
    private final int CAPACITY = 1;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    ProduceConsume() {

    }
    public int consume(){
        lock.lock();
        int val = 0;
        try{
            while (product.isEmpty()) {
                System.out.println("\t\t\t\tWaiting for not empty trigger");
                notEmpty.await();
            }
            val = product.remove();
            notFull.signalAll();
        } catch (InterruptedException err){
            System.out.println(err.getMessage());
        } finally {
            lock.unlock();
        }
        return val;
    }

    public void produce(){
        lock.lock();
        int val = (int) (Math.random() * 10) + 1;

        try{
            while (product.size() == CAPACITY){
                System.out.println("Waiting for not full trigger");
                notFull.await();
            }
            product.offer(val);
            System.out.println("Produced " + val);
            notEmpty.signalAll();
        } catch (InterruptedException err){
            System.out.println(err.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
