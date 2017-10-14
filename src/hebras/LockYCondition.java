/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class Carro{
    private Lock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    private boolean waxOn=false; //Esta variable determina que hebra comienza a ajecutarse y cual se "duerme"
    //mediante el método await()
    public void waxed(){
        lock.lock();
        try{
            waxOn=true;//Listo para pulir
            condition.signalAll();
        }finally{
            lock.unlock();
        }
    }
    public void buffed(){
        lock.lock();
        try{
            waxOn=false; //Listo para otra capa de cera.
            condition.signalAll();
        }finally{
            lock.unlock();
        }
    }
    public void waitForWaxing() throws InterruptedException{
        lock.lock();
        try{
            while(waxOn==false)
                condition.await();
        }finally{
            lock.unlock();
        }
    }
    public void waitForBuffing() throws InterruptedException{
        lock.lock();
        try{
            while(waxOn==true)
                condition.await();
        }finally{
            lock.unlock();
        }
    }
}
class WaxxOn implements Runnable{
    private Carro car;
    public WaxxOn(Carro c){car=c;}
    public void run(){
        
        try{
            while(!Thread.interrupted()){
            System.out.println("Wax On!");
            TimeUnit.MILLISECONDS.sleep(200);
            car.waxed();
            car.waitForBuffing();
            }
        }catch(InterruptedException e){
           System.out.println("Exiting via interrupted");
        }
        System.out.println("Ending wax On task");
    }
}
class WaxxOff implements Runnable{
    private Carro car;
    public WaxxOff(Carro c){car=c;}
    public void run(){
        try{
            while(!Thread.interrupted()){
                car.waitForWaxing();
                System.out.println("Wax Off!");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        }catch(InterruptedException e){
                    System.out.println("Exiting via interrupted");
                }
            System.out.println("Ending Wax Off task");
    }
}

public class LockYCondition {


    public static void main(String[] args) {
      Carro car=new Carro();
      ExecutorService ex=Executors.newCachedThreadPool();
      ex.execute(new WaxxOff(car));
      ex.execute(new WaxxOn(car));
      try{TimeUnit.SECONDS.sleep(5);}catch(InterruptedException e){}
      ex.shutdownNow();
    }
    
}
