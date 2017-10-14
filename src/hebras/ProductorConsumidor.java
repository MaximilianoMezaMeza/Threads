
package hebras;
import java.util.concurrent.*;
//Este código ejemplifica lo que sucede en un restaurante donde un mesero pide comida a un chef y este debe preparalo
class Meal{
    private final int orderNum;
    public Meal(int orderNum){this.orderNum=orderNum;}
    @Override
    public String toString(){return "Meal "+orderNum;}
}
class WaitPerson implements Runnable{
    private Restaurant restaurant;
    public WaitPerson(Restaurant a){ 
        this.restaurant=a;
    }
    public void run(){
        try{
            while(!Thread.interrupted())
            {
                synchronized(this){
                    //La siguiente sintaxis siempre se debe implementar para que no se pierda una señal, 
                    //debe ser while(no se cumple la condicion)wait()
                    while(restaurant.meal==null)
                    wait();//Para que el cocinero prepare un plato.
                }
                System.out.println("Waitperson got "+restaurant.meal);
                synchronized(restaurant.chef){// Notifica a todas las hebras tipo Chef
                    restaurant.meal=null;
                    restaurant.chef.notifyAll();// Listo para otro.
                }
            }
        }catch(InterruptedException e){
            System.out.println("WaitPerson interuupted");
        }
    }
}
class Chef implements Runnable{
    private Restaurant restaurant;
    private int count=0;
    public Chef(Restaurant r){
        this.restaurant=r;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                synchronized(this){
                    while(restaurant.meal!=null)
                        wait();//Para que se lleva el plato.
                }  //Libera el bloqueo pero duerme los hilos
                if(++count==10){
                    System.out.println("Out of food, closing");
                    restaurant.ex.shutdownNow();
                }
                //En la ejecución se verá una impresión del tipo |Order up!| porque la interrupción solo genera una
                //InterruptedException cuando la tarea intenta iniciar una tarea bloqueante, osea la interrupción se generará
                // cuando se ejecute el método sleep()
                System.out.println("Order up! ");
                synchronized(restaurant.waitperson){
                    restaurant.meal=new Meal(count);
                    restaurant.waitperson.notifyAll(); //Notifica a todos las hebras waitPerson
                }
                TimeUnit.MILLISECONDS.sleep(100);
                //Si se elimina el método sleep(), el bucle terminará correctamente y no lanzará la excepcion 
                //InterrumpedException, si no se elimina se ejecutará la excepción.
            }
        }catch(InterruptedException e){
            System.out.println("Chef interrupted");
        }
    }
}
class Restaurant{
    Meal meal;
    ExecutorService ex=Executors.newCachedThreadPool();
    public WaitPerson waitperson=new WaitPerson(this);//El objeto waitperson trabaja sobre el mismo objeto que Chef
    Chef chef=new Chef(this);//
    public Restaurant(){
        ex.execute(chef);
        ex.execute(waitperson);
    }
}
public class ProductorConsumidor {


    public static void main(String[] args) {
        new Restaurant();
    }
    
}
