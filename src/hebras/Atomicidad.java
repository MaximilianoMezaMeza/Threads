/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
/**
 * Este código demuestra el concepto de atomicidad,y la posibilidad de falla incluso usando un método
 * synchronized
 * 
 */
public class Atomicidad extends Thread {
      private int i=0; // variable no declarada volatile y que será modificada.
      //como no es volatile, no se asegura que sea modificada e inmediatamente se transmita ese 
      //cambio a la memoria. Existe la posibilidad (o más bien así será) de que los cambios efectuados
      //en esta variable sean guardado en la cache de cada uno de los 
      //procesadores( si se cuenta con multinúleos)
      
      public int getValue(){//Operación atómica, Tanto getValue() como evenIncrement deberían 
          //ser synchronized, debido a que las operaciones i++ no son atómicas y este método puede consultar 
          //el valor de i antes de que se complete alguna de estas operaciones.
          return i;
      }
      private synchronized void evenIncrement(){ //función que creara el problema
          i++; // estas operaciones no son atómicas, tienen el peligro de generar un error.
          i++;
      } 
      public void run(){
          while(true){
              evenIncrement();
          }
      }
    
    public static void main(String[] args) {
        //La dificultad está en la posibilidad de que el hilo que maneja a main pueda afectar 
        //el hilo de la clase a.
        ExecutorService e=Executors.newCachedThreadPool();
        Atomicidad a=new Atomicidad();
        e.execute(a);
        while(true){
            int val=a.getValue();
            if(val%2!=0){
                System.out.println(val);
                System.exit(0);
            }
        }
        
    }
    
}
