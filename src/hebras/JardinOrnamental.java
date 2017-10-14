/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
import java.util.*;
//Problema de la terminación de hebras.
class Count{
    private int count=0;
    private Random r=new Random(47);
    //Probar la siguiente función con y sin synchronized.
    public int increment(){
        int t=count;
        if(r.nextBoolean()){
            Thread.yield();//También ayuda a generar errores ya que este método sugiere el cambio de la hebra
            //actual a otra hebra
        }
        return (count=++t);// Posiblemente aquí se genere un error si este método no está sincronizado.
    }
    public synchronized int value(){
        return this.count;
    }
}

class Entrance implements Runnable{
    private static Count count=new Count();
    private static List<Entrance> entrances=new ArrayList<Entrance>();
    private int number=0;
    private final int id;
    private static volatile boolean canceled=false;
    public static void cancel(){
        canceled=true;
    }
    public Entrance(int id){
        this.id=id;
        //Mantener esta tarea en una lista. También impide que se aplique la depuración de memoria a las tareas muertas
        entrances.add(this);
    }
    public void run(){
        while(!canceled){
            
            synchronized(this){
                ++number;
            }
            //Si el metodo increment() no es synchronized los valores Total se pueden repetir, por lo que 
            //si se da esta condición, siempre el valor de count que en la consola se ve como Total sera menor que
            //la suma de la varaible number de la clase Entrance.
            System.out.println(this+" Total= "+count.increment());
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(InterruptedException e){
                System.out.println("sleep interrupted");
            }
        }
        System.out.println("Stopping "+this);
    }
    public synchronized int getValue(){
        return number;
    }
    @Override
    public String toString(){
        return "Entrance "+id+": "+getValue();
    }
    public static int getTotalCount(){
        return count.value();
    }
    public static int sumEntrance(){ 
        int sum=0;
        for(Entrance entrance: entrances)sum+=entrance.getValue();
        return sum;
    }
}
public class JardinOrnamental {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      ExecutorService ex=Executors.newCachedThreadPool();
      for(int i=0;i<5;i++){
          ex.execute(new Entrance(i));
      }
      try{//Ejecutar durante un tiempo, luego detenerse y recompilar los datos.
          
          TimeUnit.SECONDS.sleep(5); //*******Este método determina el valor de number OOJOOOOOOOOO***************
          //Modificalo para comprobarlo.
      }catch(InterruptedException e){
          
      }
      Entrance.cancel();// Variable cancel estática, todos comparten el mismo espacio de memoria?
      ex.shutdown();
      try{
      if(!ex.awaitTermination(250,TimeUnit.MILLISECONDS))//Espera a que todos las hebras terminen.
          //retorna false o true, dependiendo si terminaron todas la hebras o no.
          System.out.println("Some task were not terminated!");
          
      System.out.println("Total Count: "+Entrance.getTotalCount());
          System.out.println("Sum of Entrances: "+Entrance.sumEntrance());
         
      }
      catch(InterruptedException q){}
      
    }
    
}
