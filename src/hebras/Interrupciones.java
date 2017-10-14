/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.io.*;
//Se implementan tres de 4 tipos de bloqueos:
//espera durmiento una hebra(sleep) , espera de una operacion de E/S, o espera de que se libere un recurso.
//el bloqueo que no se muestra es el uso de los métodos wait() y notify() o notifyAll()
class SleepBlocked implements Runnable{
    public void run(){
        try{
            TimeUnit.SECONDS.sleep(100);// Espera 100 segundos
        }catch(InterruptedException e){
            System.out.println("InterruptedException");
        }
        System.out.println("Exiting SleepBlocked.run()");
    }
}
//No se puede interrumpir directamente operaciones de E/S
class IOBlocked implements Runnable{
    private InputStream in;
    public IOBlocked(InputStream is){
        in=is;
    }
    public void run(){
        try{
            System.out.println("Waiting for read()");
            in.read();//Espera el ingreso de un dato
        }catch(IOException e){
            if(Thread.currentThread().isInterrupted())
            System.out.println("Interrupted from blocked I/O");
            else
                throw new RuntimeException(e);
        }
        System.out.println("Exiting IOBlocked.run()");
    }
}
//Tampoco se pueden interrumpir bloqueos de otras hebras.
class SynchronizedBlocked implements Runnable{
    public synchronized void f(){
        while(true){//Nunca libera el bloqueo
            Thread.yield();
        }
    }
    public SynchronizedBlocked(){
        new Thread(){
            public void run(){
                f();//Bloqueo adquirido por esta hebra.
            }
        }.start();
    }
    public void run(){ //Se queda lboqueado esperando que el Thread creado en el constructor libere el recurso
        //o función f().
        System.out.println("Try to call f()");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
    }
}
public class Interrupciones {

    private static ExecutorService exec=Executors.newCachedThreadPool();
    public static void test(Runnable r)throws InterruptedException{
        Future<?> f= exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Interruption "+r.getClass().getName());
    }
    public static void main(String[] args)throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0); //Puesto que las dos últimas interrupcones fallaron.
    }
    
}
