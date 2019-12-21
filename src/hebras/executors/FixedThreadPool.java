package hebras.executors;

import hebras.conceptos.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *Executors.newFixedThreadPool
 * Esta clase de Executor nos permite determinar la cantidad de hebras a utilizar, ahorrando tiempo en la creación de hebras durante
 * la ejecución del programa. Las herbas creadas previamente seran reutilizadas para la ejecución de las tareas pendientes.
 */
public class FixedThreadPool {
    public static void main(String [] argv) {
        ExecutorService ex0 = Executors.newFixedThreadPool(5);
        System.out.println("Executors.newFixedThreadPool\n");
        for (int i = 0; i < 5; i++) {
            ex0.execute(new LiftOff());
        }
    }
}
