package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public final class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
    }
    
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }

    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, int pointsCount) {
        
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не реализует интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор (double, double, int) в классе " + functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не реализует интерфейс TabulatedFunction");
        }

        try {
            
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор (double, double, double[]) в классе " + functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() +  " не реализует интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);

            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор (FunctionPoint[]) в классе " + functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                "Отрезок табулирования [" + leftX + ", " + rightX + "] " +
                "выходит за область определения функции [" + 
                function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
        }

        
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }

        
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        
        for (int i = 0; i < pointsCount; ++i) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

       
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }


    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Требуется не менее 2 точек");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Заданные границы выходят за область определения");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше или равна правой");
        }
        
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        return createTabulatedFunction(leftX, rightX, values);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); ++i) {
            FunctionPoint point = function.getPoint(i);
            dos.writeDouble(point.getX());
            dos.writeDouble(point.getY());
        }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> functionClass, InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in); 
        int pointCount = dis.readInt();
        
        
        double[] xValues = new double[pointCount];
        double[] yValues = new double[pointCount];

        for (int i = 0; i < pointCount; i++) {
            xValues[i] = dis.readDouble();
            yValues[i] = dis.readDouble();
        }
        
        
        return createTabulatedFunction(functionClass, xValues, yValues);
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in); 
        int pointCount = dis.readInt();
        
        
        double[] xValues = new double[pointCount];
        double[] yValues = new double[pointCount];

        for (int i = 0; i < pointCount; i++) {
            xValues[i] = dis.readDouble();
            yValues[i] = dis.readDouble();
        }
        
      
        return createTabulatedFunction(xValues, yValues);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter Writer = new BufferedWriter(out);
        int pointsCount = function.getPointsCount();
        Writer.write(" " + pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = function.getPoint(i);
            Writer.write("\n " + point.getX());
            Writer.write(" " + point.getY());
        }
        Writer.flush();
    }
    
    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> functionClass, Reader in) throws IOException {
        java.io.StreamTokenizer st = new java.io.StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            xValues[i] = x;
            yValues[i] = y;
        }

        return createTabulatedFunction(functionClass, xValues, yValues);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        java.io.StreamTokenizer st = new java.io.StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            xValues[i] = x;
            yValues[i] = y;
        }

        return createTabulatedFunction(xValues, yValues);
    }
    
    private static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Массивы x и y должны быть одинаковой длины");
        }
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше 2");
        }

        
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию x");
            }
        }

        FunctionPoint[] points = new FunctionPoint[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            points[i] = new FunctionPoint(xValues[i], yValues[i]);
        }
        return createTabulatedFunction(functionClass, points);
    }

    
    private static TabulatedFunction createTabulatedFunction(double[] xValues, double[] yValues) {
        
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Массивы x и y должны быть одинаковой длины");
        }
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше 2");
        }

        
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию x");
            }
        }

        
        FunctionPoint[] points = new FunctionPoint[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            points[i] = new FunctionPoint(xValues[i], yValues[i]);
        }
        return createTabulatedFunction(points);
    }
}