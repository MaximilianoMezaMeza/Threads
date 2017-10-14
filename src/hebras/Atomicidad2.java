/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.util.concurrent.*;

class CircularSet{
    private int [] array;
    private int len;
    private int index=0;
    public CircularSet(int size){
        array=new int[size];
        len=size;
        for(int i=0;i<len;i++)array[i]=-1;
    }
    public synchronized void add(int i)
    {
        array[index]=i;
        index=++index%len;
    }
    public synchronized boolean contains(int val){
        boolean x=false;
        for(int i=0;i<len;i++){
            if(array[i]==val){
                x=true;
            }
            
        }
        return x;
    }
}
class SerialNumberGenerator{
    private static volatile int serialNumber=0;
    public static int nextSerialNumber(){ //Para resolver el problema esta función debe ser synchronized
        return serialNumber++; //no es una operación atómica
    }
}

public class Atomicidad2 /*SerialNumberChecker*/ {

    private static final int SIZE=10;
    private static CircularSet serials=new CircularSet(1000);
    private static ExecutorService e= Executors.newCachedThreadPool();
    
    static class SerialChecker implements Runnable{
        public void run(){
            while(true){
                int serial=SerialNumberGenerator.nextSerialNumber();
                /*Se supone que los numeros seriales son unicos y que a medida que se van generando
                se van guardando en el array del objeto CircularSet, pero como se puede ver en el if siguente
                existe duplicados, eso quiere decir que la operación de sumar un número se llevó acabo, y en
                algún momento se perdio la información. En resumen se hizo el calculo dos veces.
                Fijarse que la función Add() está al final de esta función.
                */
                if(serials.contains(serial)){ //se busca un duplicado.
                    System.out.println("Duplicado: "+serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        for(int i=0;i<10/*SIZE*/;i++){
            e.execute(new SerialChecker());
        }

    }
    
}
