/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.*;

class Blocker{
    synchronized void waitingCall(){ //Como son synchronized adquieren el bloqueo en un objeto
    try{
        while(!Thread.interrupted()){
            wait();
            System.out.println(Thread.currentThread()+" ");
        }
    }catch(InterruptedException e){
        
    }
    }
    synchronized void prod(){notify();} //La llamada a este método no afectará a ptras hebras que no sean de la instacia 
    //correspondiente
    synchronized void prodAll(){notifyAll();}
}
class Task implements Runnable{
    static Blocker blocker= new Blocker();
    public void run(){ blocker.waitingCall();}
}
class Task2 implements Runnable{
    static Blocker blocker=new Blocker();
    public void run(){ blocker.waitingCall(); }
}
public class NotifyAll {
    public static void main(String[] args) {
       ExecutorService ex=Executors.newCachedThreadPool();
       for(int i=0;i<5;i++){
           ex.execute(new Task());//clase completamente distinta a Task2, por lo que no afectará notifyAll() a las hebras
           //de Task2
       }
       ex.execute(new Task2());
       
      Timer timer=new Timer();
      timer.scheduleAtFixedRate(new TimerTask(){
       boolean prod=true;
       public void run(){
           if(prod){
               System.out.println("notify() ");
               Task.blocker.prod();
               prod=false;
           }else{
               System.out.println("notifyAll()");
               Task.blocker.prodAll();
               prod=true;
           }
       }
    },400,400);
      try{
          TimeUnit.SECONDS.sleep(5);//Ejecutar durante un tiempo
          timer.cancel();
          System.out.println("Timer canceled");
          TimeUnit.MILLISECONDS.sleep(500);
          System.out.println("Task2.blocker.prodAll() ");
          Task2.blocker.prodAll();
          TimeUnit.MILLISECONDS.sleep(500);
          System.out.println("Shutting down");
          ex.shutdownNow();
      }catch(InterruptedException e){}
    }
    
}
