/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import hebras.conceptos.LiftOff;

import java.util.concurrent.*;
import java.io.*;
// La clase LiftOff está en la clase Hebras, toma un numero que se ingresa en el constructor y luego la hebra
//lo va decrementando.
class LiftOffRunner implements Runnable{
    private BlockingQueue<LiftOff> rockets;
    public LiftOffRunner(BlockingQueue<LiftOff> q){
        this.rockets=q;
    }
    public void add(LiftOff lo){
        try{
            rockets.put(lo);
            
        }catch(InterruptedException e){
            System.out.println("Interrupted during put()");
        }
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                LiftOff rocket= rockets.take();
                rocket.run();// Utilizar la hebra retornada del método take()
            }
        }catch(InterruptedException e){
                   System.out.println("Waking from take()");
            }
            System.out.println("Exiting LiftOffRunner");
        }
}


public class HebrasYColas {

    static void getKey(){
    try{
        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }catch(java.io.IOException e){
        throw new RuntimeException(e);
         }
    }
    static void getKey(String message){
        System.out.println(message);
        getKey();
    }
    static void test(String msg,BlockingQueue<LiftOff> queue){
        System.out.println(msg);
        LiftOffRunner runner=new LiftOffRunner(queue);
        Thread t=new Thread(runner);
        t.start();
        for(int i=0;i<5;i++)//Se añaden 5 hebras
        {
            runner.add(new LiftOff(5));//Se decrementa de 4 hasta 0 (donde aparece LiftOff)
        }
        getKey("Press Enter ("+msg+")");
        t.interrupted();
        System.out.println("Finished "+msg+" Test");
    }
    public static void main(String[] args) {
        test("LinkedBlockingQueue",new LinkedBlockingQueue<LiftOff>() );//Tamaño ilimitado.
        test("ArrayBlockingQueue",new ArrayBlockingQueue<LiftOff>(3));//Tamaño fijo.
        test("SynchronousQueue",new SynchronousQueue<LiftOff>());//Tamaño igual a 1.
 
    }
    
}
