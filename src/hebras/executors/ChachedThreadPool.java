package hebras.executors;

import hebras.conceptos.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors.newCachedThreadPool
 * Esta tipo de Executor permite crear multiples hebras y reutilizar algunas que ya terminaron su tarea.
 */
public class ChachedThreadPool {

    public static void main(String[] argv){
        ExecutorService ex2 = Executors.newCachedThreadPool();
        System.out.println("Executors.newFixedThreadPool\n");
        for (int i = 0; i < 5; i++) {
            ex2.execute(new LiftOff());
        }
        ex2.shutdown();
    }
}
