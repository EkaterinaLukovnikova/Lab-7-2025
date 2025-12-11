import functions.*;
import functions.basic.*;

public class Main {
    public static void main(String[] args){
        TabulatedFunction f = TabulatedFunctions.tabulate(new Tan(), -10, 10, 5);
        for (FunctionPoint p : f) {
            System.out.println(p);
        }

        Function f2 = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f2, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new 
        LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f2, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new 
        ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f2, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunction f3;

        f3 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f3.getClass());
        System.out.println(f3);

        f3 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f3.getClass());
        System.out.println(f3);

        f3 = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {new FunctionPoint(0, 0),new FunctionPoint(10, 10)});
        System.out.println(f3.getClass());
        System.out.println(f3);

        f3 = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f3.getClass());
        System.out.println(f3);
    }
}