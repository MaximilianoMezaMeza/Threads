/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
//ReadWriteLock permite tener multiples lectores simultaneamente siempre y cuando ninguno de ellos este intentando
//realizar una escritura. Si alquien adquiere el bloqueo de escritura no se permite ninguna lectura hasta que el
//bloqueo se libere.
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

class ReaderWriterListTest{
    ExecutorService ex=Executors.newCachedThreadPool();
    private final static int SIZE=100;
    private static Random rand=new Random(45);
    private ReadWriteLockEjemplo<Integer> list=new ReadWriteLockEjemplo<Integer>(SIZE,0);
    private class Writer implements Runnable{
        public void run(){
            try{
                for(int i=0;i<20;i++){ //Prueba de dos segundos.
                    list.set(i,rand.nextInt());// index,element
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            }catch(InterruptedException e){
                
            }
            System.out.println("Writer finished, shutting down");
            ex.shutdownNow();
        }
    }
    private class Reader implements Runnable{
    public void run(){
        try{
            while(!Thread.interrupted()){
                for(int i=0;i<SIZE;i++){
                    list.get(i);
                    TimeUnit.MILLISECONDS.sleep(1);
                }
            }
        }catch(InterruptedException e){
            System.out.println("Reader interrupted");
        }
    }
    }
    
    public ReaderWriterListTest(int readers,int writers){
        for(int i=0;i<readers;i++)
            ex.execute(new Reader());
        for(int i=0;i<writers;i++)
            ex.execute(new Writer());
    }
}
public class ReadWriteLockEjemplo<T> {

private ArrayList<T> lockedList;
    //Realiza una ordenación equitiva.
    private ReentrantReadWriteLock lock=new ReentrantReadWriteLock(true);
    public ReadWriteLockEjemplo(int size,T initialValue){
        lockedList =new ArrayList<T>(Collections.nCopies(size,initialValue));
    }
    public T set(int index,T element){
        Lock wlock=lock.writeLock();
        wlock.lock();
        try{
            return lockedList.set(index,element);
        }finally{
            wlock.unlock();
        }
    }
    public T get(int index){
        Lock rlock=lock.readLock();
        rlock.lock();
        try{
            //Mostrar que múltiples lectores pueden adquirir el bloqueo de lectura
            if(lock.getReadLockCount()>1)
                System.out.println("lock.getReadLockCount() "+lock.getReadLockCount());
                return lockedList.get(index);
            
        }finally{
            rlock.unlock();
        }
    }
   
    public static void main(String[] args) {
            new ReaderWriterListTest(30,1);
    }
    
}
