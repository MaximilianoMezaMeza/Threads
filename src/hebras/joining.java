/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras;

/**
 * @author wakamole
 * El método join permite que una hebra A espere que una hebra B (en la que se ejcuta el método join()) termine su tarea
 * para luego ejecutar parte de su tarea
 * <p>
 * En el ejemplo siguiente: Sleeper será la clase que se ejecutara, por otra parte la clase Joiner esperará la ejecución
 * de la instancia Sleeper que esta en su definicón de clase.
 * <p>
 * La clase Joiner contiene una instancia del objeto Sleeper. En el método run, ejecutará la función join() que pertenece
 * a Sleeper
 */
class Sleeper extends Thread {
    private int duracion;

    public Sleeper(String name, int sleepTime) {
        super(name);
        this.duracion = sleepTime;
        start();
    }

    public void run() {
        try {
            System.out.println(getName() + " a sido dormido");
            sleep(duracion);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted. Interrrupcion: " + isInterrupted());
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    public void run() {
        try {
            sleeper.join(); //Busca una interrupción o despertar al objeto sleeper respectivo.
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}

public class joining {


    public static void main(String[] args) {
        Sleeper sleepy = new Sleeper("Sleepy", 1500);
        Sleeper grummy = new Sleeper("Grummy", 1500);
        Joiner dopey = new Joiner("Dopey", sleepy);
        Joiner doc = new Joiner("Doc", grummy);
        grummy.interrupt();
    }

}
