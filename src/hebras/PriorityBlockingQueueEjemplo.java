/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.*;

class PrioritizedTask implements Runnable, Comparable<PrioritizedTask>{
    private Random r=new Random(34);
    private static int counter=0;
    private final int id=counter++;
    private  int priority;
    protected static List<PrioritizedTask> sequence=new ArrayList<PrioritizedTask>();
    public PrioritizedTask (int priority){
        this.priority=priority;
        sequence.add(this);
    }
    public int compareTo(PrioritizedTask arg){
        return priority<arg.priority ? 1:(priority>arg.priority ? -1:0);
    }
    @Override
    public void run(){
        try{
            TimeUnit.MILLISECONDS.sleep(r.nextInt(250));
           }catch(InterruptedException e){
                    
                    }
        System.out.println(this);
        }
    
    
    public String toString(){
     return String.format("[%1$-3d]",priority)+" Task "+id;   
     }
    public String summary(){
        return "("+id+":"+priority+")";
    }
    public static class EndSentinel extends PrioritizedTask{
        private ExecutorService ex;
        public EndSentinel(ExecutorService e){
            super(-1);//La menor prioridad en este programa, por lo que esta hebra se ejecutará al último.
            ex=e;
        }
        public void run(){
            int count=0;
            for(PrioritizedTask pt: sequence){
                System.out.print(pt.summary());
                if((++count%5)==0){
                    System.out.println("\n");
                }
            }
                System.out.print("\n");
                System.out.println("\n"+this+" Calling shutdownNow()");
                ex.shutdownNow();
            
        }
    }
    
}

class PrioritizedTaskProducer implements Runnable{
    private Random r=new Random(45);
    private Queue<Runnable> queue;
    private ExecutorService ex;
    public PrioritizedTaskProducer(Queue<Runnable> q,ExecutorService e){
        queue=q;
        ex=e;
    }
    public void run(){
        //Cola no limitada; nunca se bloquea.
        //Rellenarla rápidamente con prioridades aleatorias.
        for(int i=0;i<20;i++){
            queue.add(new PrioritizedTask(r.nextInt(10)));
            Thread.yield();
        }
        //Introducir tareas de prioridad máxima.
    try{
        for(int i=0;i<10;i++){
            TimeUnit.MILLISECONDS.sleep(250);
            queue.add(new PrioritizedTask(10));
        }
        //Añadir tareas, primero las de menor prioridad.
        for(int i=0;i<10;i++)
            queue.add(new PrioritizedTask(i));
            //Un centinela para detener todas las tareas.
            queue.add(new PrioritizedTask.EndSentinel(ex));
        
    }catch(InterruptedException e){
        
    }
    System.out.println("Finished PrioritizedTaskProducer"); // puede terminar en la mitad del la ejecución 
    //del hilo correspondiente a la instancnia de PrioritizedTaskConsumer
    }
}

class PrioritizedTaskConsumer implements Runnable{
    private PriorityBlockingQueue<Runnable> q;
    public PrioritizedTaskConsumer(PriorityBlockingQueue a){
        this.q=a;
    }
    public void run(){
        try{
            while(!Thread.interrupted())
                q.take().run(); //Se bloqueara cuando q no tenga elementos
            }catch(InterruptedException e){
                    
                    }
        System.out.println("Finished PrioritizedTaskConsumer");
        }
}


public class PriorityBlockingQueueEjemplo {

    public static void main(String[] args) {
     Random r=new Random(23);
     ExecutorService ex=Executors.newCachedThreadPool();
     PriorityBlockingQueue<Runnable> queue=new PriorityBlockingQueue<Runnable>();//Tiene como interface a Queue<E>
     ex.execute(new PrioritizedTaskProducer(queue,ex));
     ex.execute(new PrioritizedTaskConsumer(queue));
    }
    
}
