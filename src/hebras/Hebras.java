/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author wakamole
 */
public class Hebras {

    
    public static void main(String[] args) 
    {
      //Ejecutando la hebra correspondiente a main()
       LiftOff launch =new LiftOff();
       launch.run();
      //Ejecutando la hebra con un constructor Thread()
      Thread d= new Thread(new LiftOff()); 
      //El constructor sólo necesita un objeto Runnable
      d.start(); 
      //Inicialización necesaria para la hebra, he invoca el método run de Runnable.
      //El mensajevWait for LiftOff sale primero por que se ejecutan dos hebras, la de main() y 
      // y la hebra d, como d se sigue ejecutando durante un largo periodo de tiempo, main puede seguir
      //ejecutandose e imprimir la frase anterior.
      System.out.print("\nSerie de hebras\n ");
      for(int i=0;i<5;i++)
      {
          new Thread(new LiftOff()).start();
      }
      
      System.out.println("Wait for LiftOff...");
    }
    
}
class LiftOff implements Runnable
    {
        protected int countDown=10;
        private static int taskCount=0;
        private final int id=taskCount++;
        public LiftOff()
        {
            
        }
        public LiftOff(int countDown)
        {
            this.countDown=countDown;
        }
        public String status()
        {
            return "Instancia #" +id +"("+(countDown>0?countDown:"Liftoff")+")\n";
        }
        public void run()
        {
            while(countDown-->0)
            {
               // try
                {
                   System.out.print(status());
                   //antes se usaba:
                   //Thread.sleep(100);
                   //TimeUnit.MILLISECONDS.sleep(100);
                   Thread.yield();
                }
                //catch(InterruptedException e)
                {
                 //   System.out.println(e.toString());
                }
            }
        }
    }

