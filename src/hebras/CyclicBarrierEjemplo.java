/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
import java.util.*;
//Simula un juego de carrera de caballos escrito por el autor cuando era estudiante.
class Horse implements Runnable{
    private static int counter=0;
    private final int id=counter++;
    private int strides=0;
    private static Random r=new Random(24);
    private static CyclicBarrier barrier;
    public Horse(CyclicBarrier c){
        barrier=c;
    }
    public synchronized int getStrides(){
        return strides;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                synchronized(this){
                strides+=r.nextInt(3);// producirá 0,1 o 2
            }
                barrier.await();//El objeto barrirer esperará a que todas las hebras Horses ejecuten el método
                //await()
            }
        }catch(InterruptedException e){
            //Una buena forma de salir.
            System.out.print("Salio por interrupción");
        }catch(BrokenBarrierException e){
            //Esta queremos probarla.
            System.out.printf("Salio por BrokenBarries");
            throw new RuntimeException(e);
        }
    }
    public String toString(){
        return "Horse "+id+" ";
    }
    public String tracks(){
        StringBuilder s=new StringBuilder();
        for(int i=0;i<getStrides();i++){
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
}
public class CyclicBarrierEjemplo {
    static final int FINISH_LINE=75;
    private List<Horse> horses=new ArrayList<Horse>();
    private ExecutorService ex=Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    public CyclicBarrierEjemplo(int nHorses, final int pause){
        barrier=new CyclicBarrier(nHorses,new Runnable(){ //CycleBarrier se inicia con nHorses, el cual determina
            //la cantidad de hilos que deben ejecutar await(). Cuando todos los hilos (igual a la cantidad nHorses)
            //ejecuten await() se iniciará el método run() de la clase anónima declarada en CyclicBarrier
           public void run(){
               StringBuilder s=new StringBuilder();
               for(int i=0;i<FINISH_LINE;i++){
                   s.append("=");//La valla en el hipódromo
               }
               System.out.println(s);
               for(Horse horse:horses){
                   System.out.println(horse.tracks());
               }
               for(Horse horse:horses){
                   if(horse.getStrides()>= FINISH_LINE){
                       System.out.println(horse+" won!");
                       ex.shutdownNow();
                       return;
                   }
               }
               try{
                   TimeUnit.MILLISECONDS.sleep(pause);
               }catch(InterruptedException e){
                   System.out.println("barrier-action sleep interrupted");
               }
           } 
        });
        for(int i=0;i<nHorses;i++){
            Horse horse=new Horse(barrier);
            horses.add(horse);
            ex.execute(horse);
        }
        
    }
    public static void main(String[] args) {
        int nHorses=7;
        int pause=200;
        new CyclicBarrierEjemplo(nHorses,pause);
    }
    
}
