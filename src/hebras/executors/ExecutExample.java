/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hebras.executors;

import hebras.conceptos.LiftOff;

import java.util.concurrent.*;

/**
 * @author wakamole
 * Los Objetos Executor, permite crear y administrar las hebras de forma automática. Evitando así tulizar la clase Thread
 */
public class ExecutExample {


    public static void main(String[] args) {
        ExecutorService ex0 = Executors.newFixedThreadPool(5);
        ExecutorService ex1 = Executors.newSingleThreadExecutor();
        ExecutorService ex2 = Executors.newCachedThreadPool();
        System.out.println("Executors.newFixedThreadPool\n");
        for (int i = 0; i < 5; i++) {
            ex0.execute(new LiftOff());
        }
        ex0.shutdown();
        System.out.println("Executors.newSinlgeThreadExcecutor\n");
        for (int i = 0; i < 5; i++) {
            ex1.execute(new LiftOff());
        }
        ex1.shutdown();
        System.out.println("Executors.newCachedThreadPool\n");
        for (int i = 0; i < 5; i++) {
            ex2.execute(new LiftOff());
        }
        ex2.shutdown();
    }

}
