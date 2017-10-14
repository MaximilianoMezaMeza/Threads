/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class BlockedMutex{
    private Lock lock=new ReentrantLock();
    public BlockedMutex(){
        //Adquirimos el bloqueo directamente para demostrar la interrupción.
        lock.lock();
    }
    public void f(){
        //Esto no estará disponible para una segunda tarea.
        try{
            lock.lockInterruptibly();//Adquiere el bloqueo a menos que la actual hebra genere una interrupción
            System.out.println("lock acquire in f()");
        }catch(InterruptedException e){
            System.out.println("Interrupted from lock acquisition in f()");
        }
    }
    
}
class Blocked2 implements Runnable{
    BlockedMutex block=new BlockedMutex();
    public void run(){
        System.out.println("Waiting for f() in BlackedMutex");
        block.f();
        System.out.println("Broken out of blocked call");
    }
}
public class BlockMutex {
    
    
    public static void main(String[] args) {
        Thread t=new Thread(new Blocked2());
        t.start();
        try{
        TimeUnit.SECONDS.sleep(5);
        }catch(InterruptedException e){}
        System.out.println("Issuing t.interrupt()");
        t.interrupt(); //Interrupción que permite salir del programa y del bloqueo constante 
        //Por medio de una excepción
        
    }
    
}
