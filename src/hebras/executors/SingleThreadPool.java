package hebras.executors;

import hebras.conceptos.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Executors.newSingleThreadExecutor
 * Crea una sola hebra, la cual se encargará de ejecutar cada una de las tareas, si se envían muchas tareas ,
 * estan quedaran en cola esperando ser ejecutadas.
 */
public class SingleThreadPool {
    public static void main(String[] args) {
        ExecutorService ex1 = Executors.newSingleThreadExecutor();

        System.out.println("Executors.newSinlgeThreadExcecutor\n");
        for (int i = 0; i < 5; i++) {
            ex1.execute(new LiftOff());
        }
        ex1.shutdown();
    }
}
