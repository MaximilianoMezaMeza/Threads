/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

//bloqueo multiple?
public class Mutex {

   public synchronized void f1(int count){
       if(count-->0){
           System.out.println("f1() calling f2() with count "+count);
           f2(count);
       }
   }
   public synchronized void f2(int count){
       if(count-->0){
           System.out.println("f2() calling f1() with count "+count);
           f1(count);
       }
   }
    public static void main(String[] args) {
       final Mutex multipleLock= new Mutex();
       //Una sola hebra adquiere el bloqueo del objeto Mutex, y va avriando de f1 a f2 sin problemas.
       new Thread(){
           public void run(){
               multipleLock.f1(10);
           }
       }.start();
    }
    
}
