package functions;

public class FunctionPoint {
    private double x;
    private double y;

    
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionPoint otherPoint = (FunctionPoint) o;
        return Math.abs(this.x - otherPoint.x) < 1e-9 && Math.abs(this.y - otherPoint.y) < 1e-9;
    }

    
    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        int xHigh = (int)(xBits >>> 32);
        int xLow = (int)(xBits & 0xFFFFFFFFL);
        int yHigh = (int)(yBits >>> 32);
        int yLow = (int)(yBits & 0xFFFFFFFFL);
        
        return xHigh ^ xLow ^ yHigh ^ yLow;
    }

    
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new FunctionPoint(this);
        }
    }
}