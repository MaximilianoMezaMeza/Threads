/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
/**
 *
 * @author wakamole
 */

class AdquiriendoLock{
    public ReentrantLock lock=new ReentrantLock();
    public ReentrantLock getLock()
    {
        return this.lock;
    }
    public void untimed(){
        boolean capture=lock.tryLock();
        try{
            System.out.println("tryLock:"+capture);
        }finally{
            if(capture){
                lock.unlock();
                System.out.println("Liberando lock de untimed");
            }
        }
    }
    public void timed(){
       boolean capture=false;
       try{
           capture=lock.tryLock(1,TimeUnit.MILLISECONDS); //Espera 1 milisegundo para intentar obtener el
           //bloqueo
       }catch(InterruptedException e){
           throw new RuntimeException (e);
       }
       try{
           System.out.println("tryLock(2,TimeUnite.SECONDS): "+ capture);
       }finally{
           if(capture){
               lock.unlock();
               System.out.println("Liberando lock de timed");
           }
       }
    }
}
public class JugosLock {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            final AdquiriendoLock al=new AdquiriendoLock();
            al.untimed();//True si el bloqueo está disponible.
            al.timed();//True si el bloqueo está disponible.
            //Estableciendo el bloqueo.
            new Thread(){
                {setDaemon(true);}
                public void run(){
                    al.getLock().lock();//Se hace un bloqueo, por lo que las funciones untimed() y timed
                    //no deberían funcionar.
                    System.out.println("Adquirido");
                }
            }.start();
            Thread.yield(); //Intentamos darle una oportunidad a una segunda tarea
            al.untimed(); //False, bloqueo adquirido por la tarea.
            al.timed();// False, bloqueo adquirido por la tarea.
    }
    
}
