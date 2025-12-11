package threads;

import functions.Function;
import functions.basic.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread{
    private final Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Generator(Task task, Semaphore dataReady, Semaphore dataProcessed){
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }

    @Override
    public void run(){
        Random random = new Random();
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {

                double base = 1 + random.nextDouble() * 9;
                Function logFunction = new Log(base);

                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                dataProcessed.acquire();
                task.setFunction(logFunction);
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setStep(step);

                System.out.printf("(Generator %d)Задание: левая граница = %.6f, правая граница = %.6f, шаг = %.6f%n", i, left, right, step);
                dataReady.release();                 
            }      
        } catch (InterruptedException e) {
            System.out.println("Генератор был прерван");
        }
    }
}