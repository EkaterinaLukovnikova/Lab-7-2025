package threads;

import functions.Function;

public class Task {
    private Function f;
    private double leftX;
    private double rightX;
    private double step;
    private int taskCount;
    private volatile boolean newDataAvailable = false;

    public Task() {
        this.taskCount = 0;
    }

    public Function getFunction() {
        return f;
    }

    public void setFunction(Function f) {
        this.f = f;
    }

    public double getLeftBorder() {
        return leftX;
    }
    public void setLeftBorder (double leftX) {
        this.leftX = leftX;
    }

    public double getRightBorder() {
        return rightX;
    }
    public void setRightBorder (double rightX) {
        this.rightX = rightX;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
    
        this.step = step;
    }

    public  int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        if (taskCount < 0) {
            throw new IllegalArgumentException("Количество заданий не может быть отрицательным");
        }
        this.taskCount = taskCount;
    }

     public boolean isNewDataAvailable() {
        return newDataAvailable;
    }

    public void setNewDataAvailable(boolean available) {
        this.newDataAvailable = available;
    }
}