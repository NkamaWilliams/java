import java.lang.Runnable;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Exam {
    static Account myAccount = new Account();
    public static void main(String[] args){

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; ++i){
            exec.execute(new Withdraw());
        }
        for (int i = 0; i < 100; ++i){
            exec.execute(new Deposit());
        }
        exec.shutdown();
        while (!exec.isTerminated()){

        }
        System.out.println(myAccount.getBalance());
    }
    static class Deposit implements Runnable{
        @Override
        public void run(){
            myAccount.deposit((int) (Math.random() * 3) + 1);
            System.out.println("Deposited $1");
        }
    }

    static class Withdraw implements Runnable{
        @Override
        public void run(){
            int amount = (int)(Math.random() * 10) + 1;
            myAccount.withdraw(amount);
            System.out.println("\t\t\tWithdrew $" + amount);
        }
    }
}
class Account{
    private int amount;
    private ReentrantLock lock = new ReentrantLock();
    private final Condition newDeposit = lock.newCondition();

    Account(){
        this.amount = 0;
    }

    Account(int bal){
        this.amount = bal;
    }

    public int getBalance(){
        return this.amount;
    }

    public void deposit(int amt){
        lock.lock();
        this.amount += amt;
        try {
            Thread.sleep(5);
        } catch (InterruptedException err){
            err.printStackTrace();
        }
        newDeposit.signal();
        lock.unlock();
    }

    public void withdraw(int amt){
        lock.lock();
        try{
            while(this.amount < amt){
                System.out.println("Insufficient balance, need $" + (amt - amount) + "more");
                newDeposit.await();
            }
            this.amount -= amt;
        } catch (InterruptedException e){
            e.printStackTrace();
            
        } finally {
            lock.unlock();
        }
        }
}