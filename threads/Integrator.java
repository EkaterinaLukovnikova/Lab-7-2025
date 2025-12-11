package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Integrator(Task task, Semaphore dataReady, Semaphore dataProcessed){
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }

    @Override
    public void run(){

        for (int i = 0; i < task.getTaskCount(); ++i){
            try {

                double left; 
                double right; 
                double step; 
                double result;

                dataReady.acquire();
                    left = task.getLeftBorder();
                    right = task.getRightBorder();
                    step = task.getStep();

                    result = Functions.integrate(task.getFunction(), left, right, step);
                    System.out.printf("(Integrator %d) Результат: левая граница = %.6f, правая граница = %.6f, шаг = %.6f, значение интеграла =  %.6f", i, left, right, step, result);
                System.out.printf("\n---------\n");
                    dataProcessed.release();

            } catch (InterruptedException e) {
                System.out.println("Integrator прерван");
            }
        }
    }
}