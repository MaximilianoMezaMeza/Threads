/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.*;

//Un semáforo contador permite que multiples tareas accedan a un recurso. simultaneamente

//Esta clase contendrá el conjunto compartido de elementos.
 class Pool<T>{
    private int size;
    private List<T> items=new ArrayList<T>();
    //Ojo que la siguiente variable es volatile.
    private volatile boolean[] checkedOut; //Lleva la cuenta de los objets que han sido extraídos.
    private Semaphore available;
    
    public Pool(Class<T> classObject,int size){
        this.size=size;
        checkedOut=new boolean[size];
        available=new Semaphore(size,true);
        //Cargar conjunto con objetos que puedan extraerse.
        for(int i=0;i<size;i++){
            try{
                //Presupone un constructor predeterminado.
                items.add(classObject.newInstance()); //Carga de objetos el conjunto compartido.
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    //Si se necesita un nuevo objeto.
    public T checkOut()throws InterruptedException{
        available.acquire(); //La instancia del objeto semáforo protege al método getItem()
        return getItem();
    }
    //Para devolver el objeto.
    public void checkIn(T x){
        if(releaseItem(x)) //Verifica si el objeto esta en el conjunto, si es así libera el semáforo
            available.release();
    }
    private synchronized T getItem(){
        for(int i=0;(i<size);i++){
            if(!checkedOut[i]){
                checkedOut[i]=true;
                return items.get(i);
            }
        }
        return null; //El semáforo impide llegar aquí.
    }
    private synchronized boolean releaseItem(T item){
        boolean isIn=false;
        int index=items.indexOf(item);
        //verifica si esta en la lista.
        if((index!=-1)&&(checkedOut[index])){
            checkedOut[index]=false;
            isIn=true;


        }
        return isIn;//No ha sido extraido.
    }
}

//Objeto que es costoso de crear, ya que su constructor tarda mucho en ejecutarse.
class Fat{
    private volatile double d;// Impedir optimización.
    private static int counter=0;
    private final int id=counter++;
    public Fat(){
        //Operación costa e interrumpible.
        for(int i=0;i<1000;i++)d+=(Math.PI+Math.E)/(double) i;
    }
    public void operation(){
        System.out.println(this);
    }
    public String toString(){
        return "Fat id: "+id;
    }
}
//Thread para extraer un recurso de un conjunto compartido.
class CheckOutTask<T> implements Runnable{
    private static int counter=0;
    private final int id=counter++;
    private Pool<T> pool;
    
    public CheckOutTask(Pool<T> pool){
        this.pool=pool;
    }
    
    public void run(){
     try{
         T item=pool.checkOut();
         System.out.println(this+" checked out "+item);
         TimeUnit.SECONDS.sleep(1);
         System.out.println(this+" checking in "+ item);
         pool.checkIn(item);
     }catch(InterruptedException e){
         
     }   
    }
    public String toString(){
        return "CheckOutTask "+id+" ";
    }
}
public class SemaphoreEjemplos {
    final static int SIZE=25;
    public static void main(String[] args) {
       final Pool<Fat> pool=new Pool<Fat>(Fat.class,SIZE);
       ExecutorService ex=Executors.newCachedThreadPool();
       for(int i=0;i<SIZE;i++){
           ex.execute(new CheckOutTask<Fat>(pool)); //Se comienza a usar las hebras de CheckOutTask
       }
       System.out.println("All CheckOutTask created");
       //Se utiliza la hebra correspondiente a main()
       List<Fat> list=new ArrayList<Fat>();
       for(int i=0;i<SIZE;i++){
           try{
               Fat f=pool.checkOut();
               System.out.println(i+ " : main() thread checked out");
               f.operation(); //Imprime la instancia de la clase Fat
               list.add(f); //Añade al ArrayList el objeto Fat extraído
           }catch(InterruptedException e){
               System.out.println("Error en main()");
           }
       }
       //Se crea otra hebra
       Future<?> blocked=ex.submit(new Runnable(){
           public void run(){
               try{
                   //El semáforo impide extracciones adicionales
                   //por lo que se bloquea la llamada
                   pool.checkOut();
               }catch(InterruptedException e){
                   System.out.println("Future<?> blocked Interrupted");
               }
           }
       });
       try{
           TimeUnit.SECONDS.sleep(2);
       }catch(InterruptedException e){
           
       }
       blocked.cancel(true);// Salir de la llamda bloqueada en blocked, por lo que se interrumpirá la tarea.
       System.out.println("Checking in object in "+ list);
       for(Fat f: list){
           pool.checkIn(f);
       }
       for(Fat f: list){ //Segunda devolución ignorada.
           pool.checkIn(f);
       }
       ex.shutdown();
    }
    
}
