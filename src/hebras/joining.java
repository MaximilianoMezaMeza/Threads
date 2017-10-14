/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

/**
 *
 * @author wakamole
 */
class Sleeper extends Thread{
    private int duracion;
    public Sleeper(String name, int sleepTime){
        super(name);
        this.duracion=sleepTime;
        start();
    }
    public void run()
    {
        try
        {
            System.out.println(getName()+" a sido dormido");
            sleep(duracion);
        }
       catch(InterruptedException e)
        {
                System.out.println(getName()+" was interrupted. Interrrupcion: "+isInterrupted());
                }
        System.out.println(getName()+" has awakened");
    }
}
class Joiner extends Thread{
    private Sleeper sleeper;
    public Joiner(String name,Sleeper sleeper){
        super(name);
        this.sleeper=sleeper;
        start();
    }
    public void run()
    {
        try{
            sleeper.join(); //Busca una interrupción o despertar al objeto sleeper respectivo.
        }
        catch(InterruptedException e){
            System.out.println("Interrupted");
        }
        System.out.println(getName()+" join completed");
    }
}
public class joining {

   
    public static void main(String[] args) {
       Sleeper sleepy=new Sleeper("Sleepy",1500),grummy=new Sleeper("grummy",1500);
       Joiner dopey=new Joiner("dopey",sleepy),doc=new Joiner("Doc",grummy);
       grummy.interrupt();
    }
    
}
