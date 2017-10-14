/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.*;


// La clase CoutDownLatch se usa para sincronizar una o mas tareas forzandolas a esperar a que se complete 
//un conjunto de operaciones que esten siendo realizadas por otras tareas.
class TaskPortion implements Runnable{
    private static int counter=0;
    private final int id=counter++;
    private static Random r=new Random(24);//Es de tipo estático, por lo que todas las hebras comparten esta variable
    //El método nextInt() de la clase Random es seguro con respecto a la concurrencia, por lo que no hay problemas.
    
    private  CountDownLatch latch;
    
    TaskPortion(CountDownLatch a){
        this.latch=a;
    }
    public void run(){
        try{
            doWork();
            latch.countDown();//Cuenta hacia abajo hasta llegar a cero.
        }catch(InterruptedException e){
            
        }
    }
    public void doWork() throws InterruptedException{
        TimeUnit.MILLISECONDS.sleep(r.nextInt(200));
        System.out.println(this+" completado");
    }
    public String toString(){
        return String.format("%1$-3d",id);
    }
} 
//Espera sobre CountDownLatch
class WaitingTask implements Runnable{
    private static int counter=0;
    private final int id=counter++;
    private CountDownLatch latch;
    public WaitingTask(CountDownLatch a){
        this.latch=a;
    }
    public void run(){
        try{
            latch.await();// La hebra correspondiente espera hasta que por el método coutDown() ejecutado en la 
            //clase TaskPortion llegue a cero (desde 100) 
            //Fijarse que WaitingTask y TaskPortion comparten la misma instancia de la clase CountDownLatch
            System.out.println("Latch barrier passed for "+this);
        }catch(InterruptedException e){
            System.out.println(this+" interrupted");
        }
    }
    public String toString(){
        return String.format("WaitingTask %1$-3d ",id);
    }
}

public class CountDownLatchEjemplo {
    static final int size=100;
    
    public static void main(String[] args) {
        ExecutorService ex=Executors.newCachedThreadPool();
   //Todos deben compartir un único objeto CountDownLatch
    CountDownLatch latch=new CountDownLatch(size);// Se inicializa el conteo en 100

    for(int k=0;k<10;k++){
        ex.execute(new WaitingTask(latch));
    }
    for(int i=0;i<size;i++){
        ex.execute(new TaskPortion(latch));
    }
    System.out.println("Launched all task");
    ex.shutdown();

    }
    
}
