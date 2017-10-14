/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
class NeedsCleanup{
    private final int id;
    public NeedsCleanup(int ident){
        this.id=ident;
        System.out.println("NeedsCleanup "+ this.id);
    }
    public void cleanup(){
        System.out.println("Cleaning up "+id);
    }
}
class Blocked3 implements Runnable{
    private volatile double d=0.0;
    public void run(){
        try{
        while(!Thread.interrupted()){ //Verifica si el hilo ha sido interrumpido
            NeedsCleanup n1= new NeedsCleanup(1);
            try{
                //Si se ejecuta la operación la instrucción Thread.Interrupt() en la operación "bloqueante"(se refiere
                // a la operación sleep)entonces 
                //el bucle terminará en con una exception de tipo InterruptedException en el catch de abajo.
                System.out.println("Sleeping");
                 TimeUnit.SECONDS.sleep(1);//Instrucción bloqueante
               
                NeedsCleanup n2=new NeedsCleanup(2);
                try{
                    //Si se ejecuta la instrucción Thread.interrupt() en el código "no bloqueante" 
                    //(se refiere al bucle for, ya que es una operacion larga pero no es bloqueante en sí) este 
                    //terminará bien debido a que el bucle while terminará de ejecutarse.
                    System.out.println("Calculating");
                    //Operación larga (instrucción no bloqueante)
                    for(int i=0;i<2500000;i++){
                        d=d+(Math.PI+Math.E)/d;
                    }
                    System.out.println("Finished time-consuming operation");
                }finally{
                    n2.cleanup();
                }
                
            }finally{
                n1.cleanup();
            }
        }
            System.out.println("Exiting via while() test");
        
    }catch(InterruptedException e){
       System.out.println("Exiting via InterruptedException");
    }
    }
}
public class ComprobarInterrupcion {

  
    public static void main(String[] args) throws Exception {
        Thread t=new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        t.interrupt();
    }
    
}
