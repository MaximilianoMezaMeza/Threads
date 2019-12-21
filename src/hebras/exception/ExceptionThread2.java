package hebras.exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Este ejemplo nos permite capturar las excepciones que son lanzadas desde los Threads. Esto es realizado por la clase
 * MyUncaughtExceptionHandler  y que permite definir la forma en la que se desea tratar la excepción en el correspondiente Thread
 *
 * Luego la clase HandlerThreadFactory permite crear instancias de Objetos Thread definiendo la rutina de capturar las Excepciones
 * usando el método setUncaughtExceptionHandler donde le es asignada una instancia del objeto MyUncaughtExceptionHandler. El
 * Objeto HandlerThreadFactory es utilizado como parámetro en la funcion newCachedThreadPool() para crear todas las instancias de
 * Thread tengan la caracteristica de capturar excepciones.
 *
 * Otra forma más práctica de hacerlo sin utlizar la clase HandlerThreadFactory Es la siguiente:
 *
 *  public static void main(String[] args){
 *          //Definimos que todas las instancias de la clase Thread una rutina de captura de Excepciones
 *          Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
 *         ExecutorService executorService= Executors.newCachedThreadPool();
 *         executorService.execute(new ExceptionThread2());
 *     }
 *
 *
 */

public class ExceptionThread2 implements Runnable {
    @Override
    public void run() {
        Thread t =Thread.currentThread();
        System.out.println("run() by "+t);
        System.out.println("Exception Handler = "+t.getUncaughtExceptionHandler());
        throw new RuntimeException();
    }
    public static void main(String[] args){
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        ExecutorService executorService= Executors.newCachedThreadPool(new HandlerThreadFactory());
        executorService.execute(new ExceptionThread2());
    }
}


class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught "+e);
    }
}

class HandlerThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        System.out.println(this+" creating new thread");
        Thread t=new Thread(r);
        System.out.println("created "+t);
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        System.out.println("Exception Handler = "+t.getUncaughtExceptionHandler());
        return t;
    }
}




