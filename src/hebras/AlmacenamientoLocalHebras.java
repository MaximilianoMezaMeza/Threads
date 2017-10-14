/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.*;
//Se crearán 5 instancias de está clase por lo que habrá 5 espacios de memoria para cada una de las instancias de la
//hebra correspoendiente que manejara un tipo concreto de valor de tipo ThreadLocal.
//En resumen ThreadLocal permite que una y sólo una instancia de una hebra manipule la variable correspondiente a su
//instancia, evitando asi las condiciones de carrera.
class Accessor implements Runnable{
    private final int id;
    public Accessor(int i){
        this.id=i;
    }

    public void run(){ //Por cada hebra creada se va ejecutando el método increment() el cual a su vez
        //llama al método get() de ThreadLocal y este a su vez llama a initialValue de la misma clase(TheradLocal).
     while(!Thread.currentThread().isInterrupted()){
         AlmacenamientoLocalHebras.increment();
         System.out.println(this);
         Thread.yield();
     }
    }
    @Override
    public String toString(){
        return "#"+id+": "+AlmacenamientoLocalHebras.get();
    }
}
public class AlmacenamientoLocalHebras {

    private static ThreadLocal<Integer> value= new ThreadLocal<Integer>(){// no es un tipos List o ArrayList
        //Para no confundirla.
      private Random rand=new Random(47);
      @Override
      protected synchronized Integer initialValue(){ //Este método inicializa el valor de un objeto ThreadLocal
          //Se ejecuta cada vez que se usa el método get().
          return rand.nextInt(10000);
      }
    };
    public static void increment(){
        value.set(value.get()+1);
    } 
    public static int get(){
        return value.get();
    }
    public static void main(String[] args) {
        ExecutorService ex=Executors.newCachedThreadPool();
        for(int i=0;i<5;i++){
            //Cada instancia tendrá un valor distinto en value, y irá aumentando a medida que la hebra se ejecute
            // debido a la llamada al método increment().
            ex.execute(new Accessor(i));
        }
        try{
        TimeUnit.SECONDS.sleep(3);
        }catch(InterruptedException e){}
        ex.shutdownNow();// Todos los objetos Accessors terminarán.
    }
    
}
