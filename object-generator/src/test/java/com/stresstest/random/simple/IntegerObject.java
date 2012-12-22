package com.stresstest.random.simple;

public class IntegerObject {

    final private long longValue;
    final private int intValue;
    final private short shortValue;
    final private char charValue;
    final private byte byteValue;
    
    public IntegerObject(long longValue, int intValue, short shortValue, char charValue, byte byteValue) {
        this.longValue = longValue;
        this.intValue = intValue;
        this.shortValue = shortValue;
        this.charValue = charValue;
        this.byteValue = byteValue;
    }
    
    public long getLongValue() {
        return longValue;
    }
    public int getIntValue() {
        return intValue;
    }
    public short getShortValue() {
        return shortValue;
    }
    public char getCharValue() {
        return charValue;
    }
    public byte getByteValue() {
        return byteValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + byteValue;
        result = prime * result + charValue;
        result = prime * result + intValue;
        result = prime * result + (int) (longValue ^ (longValue >>> 32));
        result = prime * result + shortValue;
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
        IntegerObject other = (IntegerObject) obj;
        if (byteValue != other.byteValue)
            return false;
        if (charValue != other.charValue)
            return false;
        if (intValue != other.intValue)
            return false;
        if (longValue != other.longValue)
            return false;
        if (shortValue != other.shortValue)
            return false;
        return true;
    }
}
