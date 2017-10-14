/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
public class CloseResource {
//Como la operaciones de entrada y salida no se puede interrumpir se hace este ejemplo donde se cerrarán los recursos.
    public static void main(String[] args) throws Exception {
        ExecutorService ex=Executors.newCachedThreadPool();
        ServerSocket server=new ServerSocket(8080);
        InputStream socketInput=new Socket("localhost",8080).getInputStream();
        ex.execute(new IOBlocked(socketInput)); //LA clase IOBlocked está en el archivo Interrupciones.java
        ex.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Shuttin down all Threads");
        ex.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing "+socketInput.getClass().getName());
        socketInput.close();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing "+System.in.getClass().getName());
        System.in.close();//Libera la hebra bloqueada.
    }
    
}
