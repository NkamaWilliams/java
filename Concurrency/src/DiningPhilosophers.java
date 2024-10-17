import java.util.concurrent.Semaphore;
public class DiningPhilosophers {
    public static void main(String[] args){
        int size = 5;
        String[] names = {"A", "B", "C", "D", "E"};
        Philosopher[] philosophers = new Philosopher[size];
        Chopstick[] chopsticks = new Chopstick[size];

        for (int i = 0; i < size; ++i){
            chopsticks[i] = new Chopstick();
        }

        for (int i = 0; i < size; ++i){
            if (i != size - 1){
                philosophers[i] = new Philosopher(chopsticks[i], chopsticks[(i + 1) % size], names[i]);
            }
            else {
                philosophers[i] = new Philosopher(chopsticks[(i + 1) % size], chopsticks[(i)], names[i]);
            }
            philosophers[i].start();
        }
    }
}

class Philosopher extends Thread{
    private final Chopstick left;
    private final Chopstick right;
    private int eatTimes;
    private final String name;
    public Philosopher(Chopstick left, Chopstick right, String name){
        this.left = left;
        this.right = right;
        this.name = name;
        //this.eatTimes = ((int) (Math.random() * 5)) + 1;
        this.eatTimes = 5;
    }

    public void think(){
        try{
            System.out.printf("Philosopher %s is thinking...\n", this.name);
            Thread.sleep((((int) (Math.random() * 2)) + 1) * 1000);
        }catch (InterruptedException err){
            err.printStackTrace();
        }
    }

    public void eat(){
        if(left.pickUp() && right.pickUp()){
            try{
                System.out.printf("Philosopher %s is eating...\n", this.name);
                Thread.sleep(((int) (Math.random() * 2)) + 1);
                --eatTimes;
            }catch (InterruptedException err){
                err.printStackTrace();
            }finally {
                this.left.putDown();
                this.right.putDown();
            }
        }
    }

    @Override
    public void run(){
        while (this.eatTimes > 0){
            this.think();
            this.eat();
        }
    }
}

class Chopstick{
    private final Semaphore semaphore = new Semaphore(1, true);
    public Chopstick(){}
    public boolean pickUp(){
        try{
            semaphore.acquire();
            return true;
        }catch (InterruptedException err){
            err.printStackTrace();
        }
        return false;
    }

    public void putDown(){
        semaphore.release();
    }
}