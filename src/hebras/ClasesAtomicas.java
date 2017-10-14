/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;


public class ClasesAtomicas implements Runnable {
    private AtomicInteger i=new AtomicInteger(0);  //tipo de clase atómica
    public int getValue(){
        return i.get();
    }
    private void evenIncrement(){
        i.addAndGet(2);
    }
    @Override
    public void run(){
        while(true){
          evenIncrement();        
        }
    }
    public static void main(String[] args) {
        //Se ejecuta durante 10 segundo, luego el programa termina.
        new Timer().schedule(new TimerTask(){
            public void run(){
                System.err.println("Aborting");
                System.exit(0);
            }
        }, 100000); //Primer argumento clase anónima, segundo argumento tiempo.
        
    ExecutorService ex=Executors.newCachedThreadPool();
    ClasesAtomicas a=new ClasesAtomicas(); //Tipo de clase atómica
    ex.execute(a);
    //No ocurren errores debido a la atomicidad de las clases atómicas.
    while(true){
        int val=a.getValue();
        if(val%2!=0){
            System.out.println(val);
            System.exit(0);
        }
    }
    }
    
}
