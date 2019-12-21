/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras.recursoscompartidos;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

abstract class IntGenerator{
    //El concepto de atomicidad tiene que ver con que una instruccion en un lenguaje de alto nivel tenga 
    //relación con una de bajo nivel como ensamblador.
    //El uso de las variables volatite ayuda a la atomicidad, pero no la asegura, o sea, JVM trabaja con
    //palabras de 32 bits, si por ejemplo se usara un double o long (que son de 64 bits) puede
    //provocar perdida de información (debido a las operaciones de ensamblador).
    //volatile quiere decir que si se realiza una escritura en esta variable todas las tareas(o lecturas)
    //podrán ver el cambio, incluso aunque se usen caches locales(en un sistema multiprocesador).
    //Los campos volatiles escriben inmediatamente en la memoria principal y la lectura también se realiza
    //en esta última.
    private volatile boolean canceled=false;//Flag que detendrá el bucle while de la función run()
    public abstract int next();
    public void cancel(){
        canceled=true;
    }
    public boolean isCanceled(){
        return canceled;
    }
}

class EvenChecker implements Runnable{
    private IntGenerator generator;// referencia a un objeto IntGenerator o EvenGenerator
    private int id;
    public EvenChecker(IntGenerator g,int ident)
    {
        this.generator=g;
        this.id=ident;
    }
    /*
    La operación run es dificil de controlar en situaciones donde se comparten recursos. Considera que 
    la operacion currentEvenValue no es una operación atómica (hablando de ensamblador), por lo que hay que
    tener mucho cuidado con esta función.
    */
    @Override
    public void run()
    {
        while(!generator.isCanceled())
        {
            int val=generator.next(); // next opera en un unico recurso compartido, objeto EvenGenerator
            if(val%2!=0)
            {
                System.out.println("Instancia "+generator.getClass().getName()+" "+id+" valor= "+val+" IMPAR)");
                generator.cancel();
            }
        }
    }
    
    public static void test(IntGenerator gp,int count)
    {
        System.out.println("Press control-C to exit\n");
        ExecutorService exec= Executors.newCachedThreadPool();
        for(int i=0;i<count;i++)
        {
            exec.execute(new EvenChecker(gp,i));
        }
        exec.shutdown();
        
    }
    public static void test(IntGenerator g)
    {
        test(g,10);
    }
}
class EvenGenerator extends IntGenerator{
    private int currentEvenValue=0;
    @Override
    public int next()
    {
        currentEvenValue++; //Posible peligro de que una tarea solo ejecute una suma, que dara como
        //resultado un valor impar en currentEvenValue.
        Thread.yield(); //Se usa la función yield(), para sugerir un cambio a otra tarea e inducir un error, 
        //para obtener un numero impar en currentEvenValue.
        currentEvenValue++;
        return currentEvenValue;
    }
}

class SynchronizedEvenGenerator extends IntGenerator{
    private int currentEvenValue=0;
    @Override
    //Los métodos o variables, archivos,etc que usen synchronized seran bloqueados para cualquier otra
    //hebra o tarea que quiera utilizarlo, solo la primera hebra que acceda al método(en este caso) podra
    //usar el objeto, si existen más de un método(o variable) synchronized no podrá ser usado por ninguna 
    // hebra o tarea hasta que la hebra que ocupa actualmente el objeto lo libere.
    public synchronized int next(){
        //en esta función (next) no debería existir fallo, pero como se incluye la función yield(), puede provocar
        // que currentEvenValue sea impar y provocar un fallo.
        currentEvenValue++;
        Thread.yield();
        currentEvenValue++;
        return currentEvenValue;
    }
}
//Clase que usa Lock.
class LockEvenGenerator extends IntGenerator
{
    private int currentEvenValue=0;
    private Lock lock=new ReentrantLock();
    public int next(){
        lock.lock();
        try{
        currentEvenValue++;
        Thread.yield();
        currentEvenValue++;
        return currentEvenValue;
        }
        finally{
            lock.unlock();
        }
    }
}
public class CompatirRecursos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        //Falla porque no hay sincronización con respecto al objeto compartido.
        System.out.println("Genera números impares\n");
        EvenChecker.test(new EvenGenerator()); //Se comparte un unico objeto EvenGenerator
        //Se supone que no hay falla porque se usa métodos sincronizados(synchronized)
        System.out.println("Uso de método Sincronizado\n"); //No ha fallado hasta el momento.
        EvenChecker.test(new SynchronizedEvenGenerator());
        //Uso de Lock y LockReentrant
        System.out.println("Uso de la interface Lock y la clase ReentrantLock\n");
        EvenChecker.test(new LockEvenGenerator());//No ha fallado hasta el momento
        
    }
    
}
