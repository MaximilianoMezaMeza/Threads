/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

import java.util.concurrent.*;
import java.util.*;
import java.io.*;
class Sender implements Runnable{
    private Random r=new Random(47);
    private PipedWriter out=new PipedWriter();
    public PipedWriter getPipedWriter(){
        return out;
    }
    public void run(){
        try{
            while(true){
                for(char c='A';c<='z';c++){
                    out.write(c);
                   TimeUnit.MILLISECONDS.sleep(r.nextInt(500));
                }
                
            }
        }catch(IOException e){
                    System.out.println(e+" Sender write Exception.");
                    }catch(InterruptedException e){
                        System.out.println("Sender sleep Interrupted");
                    }
    }
}
class Receiver implements Runnable{
    private PipedReader in;
    public Receiver(Sender sender)throws IOException{
        in=new PipedReader(sender.getPipedWriter());
    }
    public void run(){
        try{
            while(true){
                // Se bloquea hasta que haya carácteres
                System.out.println("Read "+(char)in.read()+",");
            }
        }catch(IOException e){
            System.out.println(e+" Receiver read Exception");
        }
    }
}
public class EntradaSalida {


    public static void main(String[] args) {
        Sender sender=new Sender();
        try{
            Receiver receiver=new Receiver(sender);
            ExecutorService ex=Executors.newCachedThreadPool();
            ex.execute(sender);
            ex.execute(receiver);
            TimeUnit.SECONDS.sleep(4);
            ex.shutdownNow();
        }catch(IOException w){
            System.out.println("Error al crear Receiver en main");
        }catch(InterruptedException e){
            System.out.println("Error en sleep de main");
        }
    }
    
}
