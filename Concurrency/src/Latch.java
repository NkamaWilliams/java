import java.lang.Runnable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.*;
public class Latch {
    private static Account account = new Account();
    public static void main(String[] args) throws InterruptedException{
        int n = (int) (Math.random() * 30) + 20;
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch end = new CountDownLatch(n);

        ExecutorService exec = Executors.newCachedThreadPool();

        for(int i = 0; i < n; ++i){
            Thread t = new Thread(() -> {
                try{
                    start.await();
                    new addMoney().run();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                end.countDown();
            });
            t.start();
        }

        long startTime = System.nanoTime();
        start.countDown();
        end.await();
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1e9);
        System.out.println(account.getBalance());
    }

    private static class addMoney implements Runnable{
        public void run(){
            int amount = (int)(Math.random() * 10) + 1;
            System.out.println("Depositing $" + amount + "...");
            account.deposit(amount);
        }
    }
}
