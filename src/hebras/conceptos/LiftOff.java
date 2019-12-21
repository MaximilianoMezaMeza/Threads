package hebras.conceptos;


public class LiftOff implements Runnable
{
    protected int countDown=10;
    private static int taskCount=0;
    private final int id=taskCount++;
    public LiftOff()
    {

    }
    public LiftOff(int countDown)
    {
        this.countDown=countDown;
    }
    public String status()
    {
        return "Instancia #" +id +"("+(countDown>0?countDown:"Liftoff")+")\n";
    }
    public void run()
    {
        while(countDown-->0)
        {
            // try
            {
                System.out.print(status());
                //antes se usaba:
                //Thread.sleep(100);
                //TimeUnit.MILLISECONDS.sleep(100);
                Thread.yield();
            }
            //catch(InterruptedException e)
            {
                //   System.out.println(e.toString());
            }
        }
    }
}