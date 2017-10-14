/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.*;
//Clase tenedor.
class Chopstick{
    private boolean taken=false;
    public synchronized void take()throws InterruptedException{
        while(taken){//El perimer hilo toma el tenedor, los demás hilos que vengan despues tendran que esperar.
            wait(); //No importa si  pasa por wait o no, el filósofo al final obtendrá el tenedor.
        }
        taken=true;
    }
    public synchronized void drop(){
        taken=false;
        notifyAll();
    }
}
class Philosopher implements Runnable{
    private Chopstick left;
    private Chopstick right;
    private int id;
    private int ponderFactor;
    private Random r=new Random(47);
    private int izq;
    private int der;
    public Philosopher(Chopstick left,Chopstick right,int id,int ponder,int izq,int der){
        this.id=id;
        this.ponderFactor=ponder;
        this.left=left;
        this.right=right;
        this.izq=izq;
        this.der=der;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                System.out.println(this+" "+"thinking");
                pause();
                //El filosofo tiene hambre.
                System.out.println(this+" grabbing right "+der);
                //Toma el tenedor derecho.
                right.take();
                System.out.println(this+" grabbing left "+izq);
                //Toma el tenedor izquierdo
                left.take();
                System.out.println(this+" eating");
                pause();
                System.out.println("drop chopsticks by "+id);
                right.drop();
                left.drop();
            }
        }catch(InterruptedException e){
            System.out.println(this+" "+"exiting via interrupted");
        }
    }
    public String toString(){
        return "Philosopher "+id;
    }
    private void pause()throws InterruptedException{
        if(ponderFactor==0){
            return;
        }
        TimeUnit.MILLISECONDS.sleep(r.nextInt(ponderFactor*250));
    }
}
public class Filosofos {
    
    public static void main(String[] args) {
        int ponder=0; //Si ponder lo igualas a 0 se producirá interbloqueo
        int size=3;
        ExecutorService ex=Executors.newCachedThreadPool();
        Chopstick [] sticks=new Chopstick[size];
        for(int i=0;i<size;i++){
            sticks[i]=new Chopstick();
        }
        for(int i=0;i<size;i++){
            ex.execute(new Philosopher(sticks[i],sticks[(i+1)%size],i,ponder,i,(i+1)%size));
           /*if(i<size-1)//Si quitamos el if se producirán interbloqueos  añadiendo el codigo que está en la parte superior.
           {
               ex.execute(new Philosopher(sticks[i],sticks[(i+1)%size],i,ponder,i,(i+1)%size));
           }else{
               ex.execute(new Philosopher(sticks[0],sticks[i],i,ponder,0,i));// Evita una espera circular
               // y por lo tanto también un interbloqueo 
           }*/
        }
        try{
            TimeUnit.SECONDS.sleep(5);
        }catch(InterruptedException e){
            System.out.println("Error en sleep de main()");
        }
        ex.shutdownNow();
    }
    
}
