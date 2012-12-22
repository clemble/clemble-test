package com.stresstest.random.simple;

public class FloatingObject {

    final private float floatValue;
    final private double doubleValue;
    private float anotherFloatValue;
    private double anotherDoubleValue;

    public FloatingObject(float floatValue, double doubleValue) {
        this.floatValue = floatValue;
        this.doubleValue = doubleValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public double getAnotherDoubleValue() {
        return anotherDoubleValue;
    }

    public float getAnotherFloatValue() {
        return anotherFloatValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(doubleValue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Float.floatToIntBits(floatValue);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FloatingObject other = (FloatingObject) obj;
        if (Double.doubleToLongBits(doubleValue) != Double.doubleToLongBits(other.doubleValue))
            return false;
        if (Float.floatToIntBits(floatValue) != Float.floatToIntBits(other.floatValue))
            return false;
        return true;
    }

}
