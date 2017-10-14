/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;
/**
 *
 * @author wakamole
 */
public class ExecutExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      ExecutorService ex=Executors.newFixedThreadPool(5);
      ExecutorService ex1=Executors.newSingleThreadExecutor();
      ExecutorService ex2=Executors.newCachedThreadPool();
      System.out.println("Executors.newFixedThreadPool\n");
      for(int i =0;i<5;i++)
      {
          ex.execute(new LiftOff());
      }
      ex.shutdown();
      System.out.println("Executors.newSinlgeThreadExcecutor\n");
      for(int i =0;i<5;i++)
      {
          ex1.execute(new LiftOff());
      }
      ex1.shutdown();
      System.out.println("Executors.newCachedThreadPool\n");
      for(int i =0;i<5;i++)
      {
          ex2.execute(new LiftOff());
      }
      ex2.shutdown();
    }
    
}
