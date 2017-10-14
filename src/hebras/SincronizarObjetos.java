/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

class DualSynch{
    private Object SincronizarObjetos= new Object();
    public synchronized void f(){
        for(int i=0;i<5;i++){
            System.out.println("f()");
            Thread.yield();
        }
    }
    public void g(){
        synchronized(SincronizarObjetos){
            for(int i=0;i<5;i++){
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}
public class SincronizarObjetos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final DualSynch ds=new DualSynch();
        new Thread(){
            public void run(){
                ds.f(); //Se ejecuta por la hebra creada.
            }
        }.start();
        ds.g();// Se ejecuta por la hebra correspondiente a main
    }
    
}
