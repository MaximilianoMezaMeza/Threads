package hebras.exception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExceptionThread
 * Esta clase demuestra que las exepciones que escapen de un Thread (en este caso el Thread solo lanza una excepción, pero es la
 * hebra de main quien la intenta capturar) no puede ser controladas ni capturadas
 */
public class ExceptionThread implements Runnable {

    @Override
    public void run() {
        throw new RuntimeException();
    }
    public static void main(String [] args){
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new ExceptionThread());
        }catch(RuntimeException e){
            //Esto no se ejecutará
            System.out.println("Exception ha sido capturada");
        }
    }
}
