package hebras.ejercicios;

public class ControlHebras implements Runnable {
    private static int idCount;
    private final int id;
    int countExecution=0;

    public ControlHebras() {
        id=idCount;
        idCount++;
        System.out.println("Inicio de hebra con id:"+id);
    }

    @Override
    public void run() {
        while(countExecution<10){
            System.out.println("Ejecucion de la hebra:"+id+" count:"+countExecution);
            Thread.yield();
            countExecution++;
        }
        System.out.println("Hebra id:"+id+ " ha terminado");
    }

    public static void main(String[] argv){
        for(int i=0;i<5;i++){
            new Thread(new ControlHebras()).start();
        }
    }
}
