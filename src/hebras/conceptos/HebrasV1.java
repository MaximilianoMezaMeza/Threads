package hebras.conceptos;

public class HebrasV1 {
    public static void main(String[] args)
    {
        //Ejecutando la hebra correspondiente a main()
        //Ejecutando la hebra en un objeto Thread()
        Thread d= new Thread(new LiftOff());
        //El constructor sólo necesita un objeto Runnable
        d.start();
        System.out.println("Wait for LiftOff...");
    }}
