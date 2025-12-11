package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;
    private int processedCount = 0;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        System.out.println("Integrator started");
        
        while (processedCount < task.getTaskCount()) {
            try {
                double left, right, step;
                
                synchronized (task) {
                    // Проверяем, что все данные доступны
                    if (task.getFunction() == null) {
                        Thread.sleep(10);
                        continue;
                    }
                    
                    left = task.getLeftBorder();
                    right = task.getRightBorder();
                    step = task.getStep();
                    double integral = Functions.integrate(task.getFunction(), left, right, step);
                
                    System.out.printf("Integrator %d: left=%.4f, right=%.4f, step=%.4f, result=%.8f%n", 
                        processedCount, left, right, step, integral);
                
                    processedCount++;
                }
                
                // Имитация времени на вычисление
                Thread.sleep(10);
                
            } catch (IllegalArgumentException e) {
                System.out.printf("Integrator error in task %d: %s%n", processedCount, e.getMessage());
                processedCount++;
            } catch (InterruptedException e) {
                System.out.println("Integrator interrupted");
                return;
            } catch (Exception e) {
                System.out.printf("Unexpected error in integrator: %s%n", e.getMessage());
                // Продолжаем работу
            }
        }
        
        System.out.println("Integrator finished. Processed " + processedCount + " tasks.");
    }
}