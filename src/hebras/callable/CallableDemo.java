package hebras.callable;

import java.util.ArrayList;
import java.util.concurrent.*;

class TaskWithResult implements Callable<String> {
    private  int id;

    public TaskWithResult(int id) {
        this.id = id;
    }


    @Override
    public String call() throws Exception {
        return "Result of TaskWithResult "+ id;
    }
}

public class CallableDemo{
    public static void main(String [] argv){
        ExecutorService executorService= Executors.newCachedThreadPool();
        ArrayList<Future<String>> results=new ArrayList<Future<String>>();

        for(int i=0;i<10;i++){
            results.add(executorService.submit(new TaskWithResult(i)));
        }

        for(Future<String> result:results){
            try {
                System.out.println(result.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }finally {
                executorService.shutdown();
            }
        }
    }
}
