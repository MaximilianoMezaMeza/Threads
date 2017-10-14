/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;
// Este código busca demostrar que es aveces es más eficiente tener sólo una sección sincronizada en un método a
// tener un método completo sincronizado.
class Pair{
    private int x,y;
    public Pair(int x,int y){
        this.x=x;
        this.y=y;
    }
    public Pair(){
        this(0,0);
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void incrementX(){
        x++;
    }
    public void incrementY(){
        y++;
    }
    @Override
    public String toString(){
        return "x= "+this.x+" y= "+this.y;
    }
    public class PairValueNotEqualException extends RuntimeException{
        public PairValueNotEqualException(){
            super("Pair values not equal: "+Pair.this);
        }
    }
    //Tanto la variable y como x deben ser iguales.
    public void checkState(){
        if(x!=y){
            throw new PairValueNotEqualException();
        }
    }
}

//Proteger un objeto Pair
abstract class PairManager{
    public AtomicInteger checkCounter=new AtomicInteger(0);
    protected Pair p= new Pair();
    private List<Pair> storage=Collections.synchronizedList(new ArrayList<Pair>());
    //Collections.synchronizedList significa que se puede usar de forma segura con hilos
    public synchronized Pair getPair(){
        return new Pair(p.getX(),p.getY());
    }
    //Asumimos que esta es una operación de larga duración.
    protected void store(Pair p){
        storage.add(p);
        try{
                    TimeUnit.MILLISECONDS.sleep(50);
        }catch(InterruptedException i){
            System.out.println("Interrupcion: "+i.toString());
        }
    }
    public abstract void increment(); //No es declarado como synchronized, pero luego si se usa así
}
//Las hebras no podran acceder muy seguido al objeto PairManager1 debido a que generalemente está bloqueado
//Por eso en los resultados finales el checkCounter de pm1 es menor
class PairManager1 extends PairManager{
    public synchronized void increment(){// Aquí el método se declara como synchronized
        p.incrementX();
        p.incrementY();
        store(getPair());// no es necesario protegerla pero para probarlo de hace
    }
}
//Debido a que solo una parte del método es increment esta sincronizado, las hebras tienen acceso a los demás
//miembros del objeto, permitiendo ejecutar mas acciones. Por lo anteriorel valor de checkCounter de pm2 es mayor.
class PairManager2 extends PairManager{
    public void increment(){
        Pair t;
        synchronized(this){ //Se debe pasar como parámetro el objeto que se desea sincronizar.
            //Con esto no se pueden invocar otros métodos synchronized ni secciones críticas del objeto.
            //En resumen sólo reduce el ambito de sincronización.
            p.incrementX();
            p.incrementY();
            t=getPair();
        }
        store(t);// Aquí se deja fuera, probando que no en necesario la sincronización.
    }
}

class PairManipulator implements Runnable{
    private PairManager pm;
    public PairManipulator(PairManager pm){
        this.pm=pm;
    }
    @Override
    public void run(){
        while(true){
            pm.increment();
        }
    }
    @Override
    public String toString(){
        return "Pair: "+pm.getPair()+" checkCounter= "+pm.checkCounter.get();
    }
}

class PairChecker implements Runnable{
    private PairManager pm;
    public PairChecker(PairManager pm){
        this.pm=pm;
    }
    public void run(){
       while(true){
           pm.checkCounter.incrementAndGet();// No es synchronized
           pm.getPair().checkState();//Si es synchronized
       }
    }
}

public class SeccionCritica {

    static void testApproaches(PairManager pman1,PairManager pman2){
        ExecutorService ex=Executors.newCachedThreadPool();
        PairManipulator pm1=new PairManipulator(pman1),pm2=new PairManipulator(pman2);
        PairChecker pcheck1=new PairChecker(pman1),pcheck2=new PairChecker(pman2);
        ex.execute(pm1);
        ex.execute(pm2);
        ex.execute(pcheck1);
        ex.execute(pcheck2);
        try{
            TimeUnit.MILLISECONDS.sleep(500);
        }catch(InterruptedException e){
            System.out.println("Sleep interrupted");
        }
        System.out.println("pm1: "+pm1+ "\npm2: "+pm2);// Esta función se ejecutará en algun momento.
        //Con la siguiente función se terminará el programa. Esto explica el final del programa
        // ya que las tareas se ejecutaran indefinidamente sin la siguiente función.
        System.exit(0);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        PairManager pman1=new PairManager1();// Método sincrónico
        PairManager pman2=new PairManager2();//Sólo tiene un sección sincronica.
        testApproaches(pman1,pman2);
        
    }
    
}
