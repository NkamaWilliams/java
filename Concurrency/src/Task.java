import java.lang.Runnable;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Task {

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; ++i){
//            executor.execute(new Thread(new AddDollar()));
        }
        executor.shutdown();
        while (!executor.isTerminated()){

        }
//        System.out.println(acc.getBalance());
    }


}

//class MultiPrint implements Runnable{
//    private int num;
//    private int amount;
//    public MultiPrint(int number, int amt){
//        this.num = number;
//        this.amount = amt;
//    }
//
//    public MultiPrint(){
//        this.num = 9;
//        this.amount = 100;
//    }
//    @Override
//    public void run(){
//        Thread t = new Thread(new Wow("Wow"));
//        t.start();
//        try{
//            t.join();
//        }catch (InterruptedException err){
//            err.printStackTrace();
//        }
//        for (int i = 1; i <= this.amount; ++i){
//            System.out.printf(this.num + "\t");
//            if (i % 10 == 0){
//                System.out.println();
//            }
//
////            if (i == 50){
////                try{
////                    Thread.sleep(1000);
////                }
////                catch (InterruptedException err){
////                    System.out.println("There was an error");
////                }
////            }
//        }
//    }
//}
//
//class Wow implements Runnable{
//    private String string;
//    public Wow(String name){
//        this.string = name;
//    }
//    @Override
//    public void run(){
//        for (int i = 0; i < 100; ++i){
//            System.out.printf(this.string + "\t");
//            if ((i+1)%5 == 0){
//                System.out.println();
//            }
//        }
//    }
//}