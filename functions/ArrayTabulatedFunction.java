package functions;

import java.io.*;
import java.util.Iterator;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;
    private final double EPSILON_DOUBLE = 1e-10;

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int curIndex = 0;

            @Override
            public boolean hasNext() {
                return curIndex < pointsCount;
            }

            
            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException("Больше нет доступных точек");
                }
                
                return new FunctionPoint(points[curIndex++]);
            }

   
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Операция удаления недоступна");
            }
        };
    }
    
    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }

    public ArrayTabulatedFunction() {} 

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount + 10];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Должно быть не менее 2 точек");
        }
        
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по X");
            }
        }
        
        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 10];
        
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 3) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 3");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Правая граница должна быть больше левой");
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 5];

        double h = (rightX - leftX) / (pointsCount - 1); // h - шаг точек
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * h;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }   

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values == null) {
            throw new IllegalArgumentException("Массив не может быть пустым");
        }

        if (values.length < 2) {
            throw new IllegalArgumentException("Массив должен содержать не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Правая граница должна быть больше левой");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount + 5];

        double h = (rightX - leftX) / (pointsCount - 1); // h - шаг точек
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * h;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    @Override
    public double getFunctionValue(double x) {
        double M = 0;
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        if (Math.abs(x - points[0].getX()) < EPSILON_DOUBLE) { 
            return points[0].getY();
        }

        if (Math.abs(x - points[pointsCount - 1].getX()) < EPSILON_DOUBLE) { 
            return points[pointsCount - 1].getY();
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x_1 = points[i].getX();
            double x_2 = points[i + 1].getX();
            
            if (x > x_1 && x < x_2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                M = y1 + (y2 - y1) * (x - x_1) / (x_2 - x_1);
                return M;
            }

            if (Math.abs(x - points[i].getX()) < EPSILON_DOUBLE) {
                M = points[i].getY();
                return M;
            }
        }
        
        return M;
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }
        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }

        if (point.getX() <= points[index - 1].getX()) {
            return; 
        }
        if (point.getX() >= points[index + 1].getX()) {
            return; 
        }

        points[index] = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }

        if (x <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException();
        }
        if (x >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException();
        }

        points[index].setX(x);
    }

    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы");
        }
        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить - нужно 3 точки");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
        points[pointsCount] = null;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }

        if (insertIndex < pointsCount && Math.abs(point.getX() - points[insertIndex].getX()) < EPSILON_DOUBLE) {
            throw new InappropriateFunctionPointException(); 
        }

        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        for (int i = 0; i < pointsCount; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("(");
            sb.append(points[i].getX());
            sb.append("; ");
            sb.append(points[i].getY());
            sb.append(")");
        }
        
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (!(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction otherFunc = (TabulatedFunction) o;
        
        if (this.pointsCount != otherFunc.getPointsCount()) return false;
        
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction otherArrayFunc = (ArrayTabulatedFunction) o;
            
            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(otherArrayFunc.points[i])) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint thisPoint = this.getPoint(i);
                FunctionPoint otherPoint = otherFunc.getPoint(i);
                
                if (!thisPoint.equals(otherPoint)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        
        hash ^= pointsCount;
        
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayTabulatedFunction cloned = (ArrayTabulatedFunction) super.clone();
        
        cloned.points = new FunctionPoint[this.points.length];
        cloned.pointsCount = this.pointsCount;
        
        for (int i = 0; i < this.pointsCount; i++) {
            if (this.points[i] != null) {
                cloned.points[i] = new FunctionPoint(this.points[i]);
            }
        }
        
        for (int i = cloned.pointsCount; i < cloned.points.length; i++) {
            cloned.points[i] = null;
        }
        
        return cloned;
    }
}