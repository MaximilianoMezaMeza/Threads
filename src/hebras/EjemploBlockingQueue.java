/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
import java.util.*;
//Ejemplo de uso de BlockingQueue
//Las colas son bloqueantes, no es necesario usar wait o notify para la sincronización, cada objeto usa su 
//propia cola, por que no es necesaria una sincronización
class Toast{
    public enum Status{Dry,BUTTERED,JAMMED}
    private Status status=Status.Dry;
    private final int id;
    public Toast(int id){
        this.id=id;
    }
    public void butter(){
        status=Status.BUTTERED;
    }
    public void jam(){
        status=Status.JAMMED;
    }
    public Status getStatus(){
        return status;
    }
    public int getId(){
        return id;
    }
    public String toString(){
        return "Toast "+id+" : "+status;
    }
}
class ToastQueue extends LinkedBlockingQueue<Toast>{}
//Creando tostadas
class Toaster implements Runnable{
    private ToastQueue toastQueue;
    private int count=0;
    private Random r=new Random(57);
    public Toaster(ToastQueue tq){
        toastQueue=tq;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(500);
                //Hacer tostada
                Toast t=new Toast(count++);
                System.out.println(t);
                //Insertar en la cola
                toastQueue.put(t);
            }
            
        }catch(InterruptedException e){
            System.out.println("Toaster interrrupted");
        }
        System.out.println("Toaster off");
    }
}
//Untar la mantequilla tostada.
class Butterer implements Runnable{
    private ToastQueue dryQueue,butteredQueue;
    public Butterer(ToastQueue dry, ToastQueue buttered){
        dryQueue=dry;
        butteredQueue=buttered;
    }
    public void run(){
        try{
            while(!Thread.interrupted())//Se bloquea hasta que haya otra tostada disponible
            {    Toast t=dryQueue.take();
                 t.butter();
                 System.out.println(t);
                 butteredQueue.put(t);
            }
        }catch(InterruptedException e){
            System.out.println("Buttered interrupted");
        }
        System.out.println("Butterer off");
    }
}
//Ponerle mermelada sobre la tostada untada de mantequilla
class Jammer implements Runnable{
    private ToastQueue butteredQueue, finishedQueue;
    public Jammer(ToastQueue buttered, ToastQueue finished){
        butteredQueue=buttered;
        finishedQueue=finished;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
               //Se bloquea hasta que haya otra tostada disponible.
                Toast t=butteredQueue.take();
                t.jam();
                System.out.println(t);
                finishedQueue.put(t);
            }
        }catch(InterruptedException e){
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }
}
//Consumir la tostada
class Eater implements Runnable{
    private ToastQueue finishedQueue;
    private int counter=0;
    public Eater(ToastQueue a){
        this.finishedQueue=a;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                //Se bloquea hasta que haya otra tostada disponible
                Toast t=finishedQueue.take();
                //Verificar que la tostatda se ha preparado correctamente
                // y que las tostadas llevan mermelada
                if((t.getId()!=counter++) ||(t.getStatus()!=Toast.Status.JAMMED)){
                    System.out.println(">>>>Error "+t);
                    System.exit(1);
                }
                else{
                    System.out.println("Chomp, Que rico!!!!" +t);
                }
            }
        }catch(InterruptedException e){
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}
public class EjemploBlockingQueue {


    public static void main(String[] args) {
       ToastQueue dryQueue=new ToastQueue(),butteredQueue=new ToastQueue(),finishedQueue=new ToastQueue();
       ExecutorService ex=Executors.newCachedThreadPool();
       ex.execute(new Toaster(dryQueue));//Primero se tuestan
       ex.execute(new Butterer(dryQueue,butteredQueue));//Segundo se sacan las tostadas y se les pone mantquilla
       ex.execute(new Jammer(butteredQueue,finishedQueue));//Tercero se les pone mermelada
       ex.execute(new Eater(finishedQueue));//Finalmente se comen
       try{
           TimeUnit.SECONDS.sleep(5);
        }catch(InterruptedException e){
           System.out.println("Error en sleep() de main()");
        }
       ex.shutdownNow();
    }
    
}
