package functions;

import functions.meta.*;

public final class Functions {
    
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power); 
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

   
        public static double integrate(Function f, double leftX, double rightX, double step) {
        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования за границами области определения");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг не положителен");
        }

        // Вычисление интеграла методом трапеций
        double integral = 0.0;
        double x = leftX;
        double y1 = f.getFunctionValue(x);

        while (x < rightX) {
            double x2 = Math.min(x + step, rightX);
            double y2 = f.getFunctionValue(x2);

            integral += (y1 + y2) * (x2 - x) / 2;
            
            x = x2;
            y1 = y2;
        }
        
        return integral;
    }
}