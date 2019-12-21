package hebras.conceptos;

public class MoreBasicThread {

    public static void main(String [] argv) {
        for (int i = 0; i < 5; i++) {
            new java.lang.Thread(new LiftOff()).start();
        }

        System.out.println("Wait for LiftOff...");
    }

}
