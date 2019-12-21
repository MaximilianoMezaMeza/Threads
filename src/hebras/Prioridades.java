/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
/**
 *
 * Muestra el cambio de prioridades en la ejecución de las hebras.
 * El método run contiene una operación contosa, por lo que al ser una operación larga, puede generar un cambio
 * de contexto a un nuevo thread.
 *
 * Al tutlizar el bucle for de main, aumenta la cantidad de tareas a ejecutar y también aumenta la posibilidad de cambio de contexto,
 * ya que las operaciones en el método run son muy costosas en cuanto a uso de procesador.
 *
 * Si que quita el bucle for, se ve una ejeción mas uniforme, donde las tareas con prioridad 10 se ejecutan rápidamente,
 * mientras que las con prioridad 1 son relegadas al final de la ejecución del software
 *
 * Thread[pool-1-thread-2,10,main]
 *
 * pool-1-thread-x: x indica el grupo de ejecución.
 * ,10 :corresponde a la prioridadujy
 *
 *
 */


public class Prioridades {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService exec= Executors.newCachedThreadPool();
        for(int i =0;i<5;i++)
        {
                exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
                exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        }
        exec.shutdown();
    }
    
}
//Prioridad
 class SimplePriorities implements Runnable
{
     private int countDown =5;
     private volatile double d;
     private int priority;
     
     public SimplePriorities(int P)
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