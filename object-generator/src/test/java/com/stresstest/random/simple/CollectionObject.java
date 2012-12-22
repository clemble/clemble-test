package com.stresstest.random.simple;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CollectionObject {

    final private Collection<String> stringCollection;
    private Collection<String> anotherStringCollection;
    final private List<Integer> integerCollection;
    private List<Integer> anotherIntegerCollection;
    final private Set<Float> floatCollection;
    private Set<Float> anotherFloatCollection;
    final private Queue<Double> doubleCollection;
    private Queue<Double> anotherDoubleCollection;
    final private Deque<Long> longCollection;
    private Deque<Long> anotherLongCollection;
    final private Map<Long, String> mapCollection;
    private Map<Long, String> anotherMapCollection;
    private static Collection<String> staticStrings;

    public CollectionObject(final Collection<String> stringCollection, final List<Integer> integerCollection, final Set<Float> floatCollection,
            final Queue<Double> doubleCollection, final Deque<Long> longCollection, final Map<Long, String> mapCollection) {
        this.stringCollection = stringCollection;
        this.integerCollection = integerCollection;
        this.floatCollection = floatCollection;
        this.doubleCollection = doubleCollection;
        this.longCollection = longCollection;
        this.mapCollection = mapCollection;
    }

    public Deque<Long> getLongCollection() {
        return longCollection;
    }

    public void addLongCollection(Long longValue) {
        throw new IllegalAccessError();
    }

    public void addToAnotherLongCollection(Long longValue) {
        anotherLongCollection.add(longValue);
    }

    public Queue<Double> getDoubleCollection() {
        return doubleCollection;
    }

    public void addDoubleCollection(Double doubleValue) {
        throw new IllegalAccessError();
    }

    public void addToAnotherDoubleCollection(Double doubleValue) {
        anotherDoubleCollection.add(doubleValue);
    }

    public Set<Float> getFloatCollection() {
        return floatCollection;
    }

    public void addFloatCollection(Float floatValue) {
        floatCollection.add(floatValue);
    }

    public void addToAnotherFloatCollection(Float floatValue) {
        anotherFloatCollection.add(floatValue);
    }

    public List<Integer> getIntegerCollection() {
        return integerCollection;
    }

    public void addIntegerCollection(Integer intValue) {
        integerCollection.add(intValue);
    }

    public void addToAnotherIntegerCollection(Integer intValue) {
        anotherIntegerCollection.add(intValue);
    }

    public void addToAnotherIntegerCollection(int index, int element) {
        anotherIntegerCollection.add(index, element);
    }

    public void setMeFree() {
        // Do nothing
    }

    public Collection<String> getStringCollection() {
        return stringCollection;
    }

    public void addStringCollection(String stringValue) {
        stringCollection.add(stringValue);
    }

    public void addToAnotherStringCollection(String stringValue) {
        anotherStringCollection.add(stringValue);
    }

    public Map<Long, String> getMapCollection() {
        return mapCollection;
    }

    public void putSomethingThere(Long key, String value) {
        mapCollection.put(key, value);
    }

    public void putInAnotherMap(Long key, String value) {
        anotherMapCollection.put(key, value);
    }

    public Collection<String> getAnotherStringCollection() {
        return anotherStringCollection;
    }

    public void setAnotherStringCollection(Collection<String> anotherStringCollection) {
        throw new IllegalAccessError();
    }

    public List<Integer> getAnotherIntegerCollection() {
        return anotherIntegerCollection;
    }

    public void setAnotherIntegerCollection(List<Integer> anotherIntegerCollection) {
        this.anotherIntegerCollection = anotherIntegerCollection;
    }

    public Set<Float> getAnotherFloatCollection() {
        return anotherFloatCollection;
    }

    public void setAnotherFloatCollection(Set<Float> anotherFloatCollection) {
        this.anotherFloatCollection = anotherFloatCollection;
    }

    public Queue<Double> getAnotherDoubleCollection() {
        return anotherDoubleCollection;
    }

    public void setAnotherDoubleCollection(Queue<Double> anotherDoubleCollection) {
        this.anotherDoubleCollection = anotherDoubleCollection;
    }

    public Deque<Long> getAnotherLongCollection() {
        return anotherLongCollection;
    }

    public void setAnotherLongCollection(Deque<Long> anotherLongCollection) {
        this.anotherLongCollection = anotherLongCollection;
    }

    public Map<Long, String> getAnotherMapCollection() {
        return anotherMapCollection;
    }

    public void setAnotherMapCollection(Map<Long, String> anotherMapCollection) {
        this.anotherMapCollection = anotherMapCollection;
    }

    public static Collection<String> getStaticStrings() {
        return staticStrings;
    }

    public static void setStaticStrings(Collection<String> staticStrings) {
        CollectionObject.staticStrings = staticStrings;
    }

    public static void addToStaticStrings(String newString) {
        if (newString == null)
            return;
        CollectionObject.staticStrings.add(newString);
    }
}
