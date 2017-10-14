/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
/**
 *
 * @author wakamole
 */
public class Prioridades {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService exec= Executors.newCachedThreadPool();
        //for(int i =0;i<5;i++)
        //{
                exec.execute(new simplePriorities(Thread.MIN_PRIORITY));
                exec.execute(new simplePriorities(Thread.MAX_PRIORITY));
        //}
        exec.shutdown();
    }
    
}
//Prioridad
 class simplePriorities implements Runnable
{
     private int countDown =5;
     private volatile double d;
     private int priority;
     
     public simplePriorities(int P)
     {
         this.priority=P;
     }
     public String toString()
     {
         return (Thread.currentThread()+": "+countDown);
     }
     @Override
     public void run()
     {
         Thread.currentThread().setPriority(priority);
         while(true)
         {
             for(int i=1;i<100000;i++)
             {
                 d+=(Math.PI +Math.E)/(double)i;
                 if(i%1000==0)
                 {
                     //System.out.println("yield i="+i);
                     //Thread.yield();// Sugiere al planificador un cambio a otra hebra con la misma prioridad
                     try
                     {
                         Thread.sleep(100);
                     }
                     catch(InterruptedException e)
                     {
                         System.out.println("Error"+e.toString());
                     }
                 }
                 System.out.println(this+"  i= "+i+"  d="+d);
                 if(--countDown==0)return;
             }
         }
     }
}