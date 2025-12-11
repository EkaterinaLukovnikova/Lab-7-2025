package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable{
    private final Task task;
    private final Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }
    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            try {
                //функция логарифма с основанием (1;10)
                double base = 1.0 + 9 * random.nextDouble();
                Function log = new Log(base);

                //Левая граница (0; 100)
                double left = 100 * random.nextDouble();

                //Правая граница (100; 200)
                double right = 100 + 100 * random.nextDouble();

                //Шаг дискретизации (0; 1)
                double step = random.nextDouble();
                synchronized (task) {
                    task.setFunction(log);
                    task.setLeftBorder(left);
                    task.setRightBorder(right);
                    task.setStep(step);
                }
                System.out.printf("Generator %d: left=%.4f, right=%.4f, step=%.4f%n", i, left, right, step);
                // Имитация времени на генерацию
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Generator interrupted");
                return;
            }
        }
    }
}