/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
//Las operaciones wait(), notify y notifyAll() deben hacerse en un método o sección sincronizada, porque se exige que
//se libere un bloqueo y los métodos synchronized siempre adquieren el bloqueo de un objeto.
class Car{
    private boolean waxOn=false; //Ojo, no es volatil.
    public synchronized void waxed(){
        waxOn=true;
        notifyAll();
    }
    public synchronized void buffed(){
        waxOn=false;
        notifyAll(); //libera el wait.
    }
    public synchronized void waitForWaxing() throws InterruptedException{
        while(waxOn==false){
            wait();
        }
    }
    public synchronized void waitForBuffing() throws InterruptedException{
        while(waxOn==true){
            wait(); //La función wait() libera al objeto de la hebra que posee el bloqueo 
            //y además suspende la ejecución de esta, permitiendo que otra terea se ejecute. 
        }
    }
}

class WaxOn implements Runnable{
    private Car car;
    public WaxOn(Car c){
        car=c;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                System.out.println("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        }catch(InterruptedException e){
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending wax On task");
    }
}

class WaxOff implements Runnable{
    private Car car;
    public WaxOff(Car c){
        this.car=c;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                car.waitForWaxing();// Quizas en la primera ejecución no sirve para nada, pero en la segunda 
                //si servirá
                System.out.println("Wax Off!");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
            
        }catch(InterruptedException e){
            System.out.println("Exiting via interrupted");
        }
    }
}
public class UsoWait {

    public static void main(String[] args) {
        Car c=new Car();
        ExecutorService ex=Executors.newCachedThreadPool();
        ex.execute(new WaxOff(c));
        ex.execute(new WaxOn(c));
        try{ TimeUnit.SECONDS.sleep(5);}catch(InterruptedException e){} 
        //El programa se ejecutara durante 5 segundos.
        ex.shutdownNow();// Para unobjeto ExecutorService este método ejecuta interrupt(), generando una interrupción
        //que detendrá las hebras y el programa.
    }
    
}
