package hebras.ejercicios;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class AbstractSumGenerator{
    private volatile boolean cancelar=true;
    public abstract double next();
    public boolean isCancelar() {
        return cancelar;
    }
    public void cancelar() {
        this.cancelar = false;
    }
}

class SumGenerator extends AbstractSumGenerator{
    private volatile double valueGenerated=0;
    @Override
    public  double  next() {
        valueGenerated++;
        Thread.yield();
        valueGenerated++;
        return valueGenerated;
    }
}

class Consumer implements Runnable{

    private final int id;
    AbstractSumGenerator sumGenerator;

    public Consumer(int id, AbstractSumGenerator sumGenerator) {
        this.id = id;
        this.sumGenerator = sumGenerator;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (sumGenerator.isCancelar()) {
            double value=sumGenerator.next();
            if (value % 2 != 0) {
                System.out.println("Mi id: " + id + ", El numero: " + value + " es IMPAR");
                sumGenerator.cancelar();
            }// else {
             //   System.out.println("Mi id: " + id + ", El numero: " + value + " es PAR");
            //}
        }
    }


}

public class MiCompartirRecursos {

    public static void main(String[] argv){
        ExecutorService executorService= Executors.newCachedThreadPool();
        System.out.println("Comienza la ejecución");
        SumGenerator sumGenerator=new SumGenerator();
        for(int i=0;i<10;i++){
            executorService.execute(new Consumer(i,sumGenerator));
        }
        System.out.println("Operación Completa");
        executorService.shutdown();
    }
}
