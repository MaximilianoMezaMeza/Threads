/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras.conceptos;

import hebras.conceptos.LiftOff;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author wakamole
 */
public class MyThread {

    
    public static void main(String[] args) 
    {
      //Ejecutando la hebra correspondiente a main()
       LiftOff launch =new LiftOff();
       launch.run();
      //Ejecutando la hebra en un objeto Thread()
      java.lang.Thread d= new java.lang.Thread(new LiftOff());
      //El constructor sólo necesita un objeto Runnable
      d.start(); 
      //Inicialización necesaria para la hebra, he invoca el método run de Runnable.
      //El mensaje Wait for LiftOff sale primero por que se ejecutan dos hebras, la de main() y
      // y la hebra d, como d se sigue ejecutando durante un largo periodo de tiempo, main puede seguir
      //ejecutandose e imprimir la frase anterior.
      System.out.print("\nSerie de hebras\n ");
      for(int i=0;i<5;i++)
      {
          new java.lang.Thread(new LiftOff()).start();
      }
      
      System.out.println("Wait for LiftOff...");
    }
    
}

