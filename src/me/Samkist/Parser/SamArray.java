package me.Samkist.Parser;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

public class SamArray<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable {
    private static final int INITIAL_CAPACITY = 10;
    private int size = 0;
    private Object[] elementData;

    public SamArray() {
        elementData = new Object[INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    public SamArray(Object[] data) {
        elementData = data;
        size = data.length;
    }

    public boolean add(E e) {
        if(size == elementData.length) {
            expandArray();
        }
        elementData[size++] = e;
        return true;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("There is nothing at index " + index + " array size is " + size);
        }
        return (E) elementData[index];
    }
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("There is nothing at index " + index + " array size is " + size);
        }
        Object removedElement = elementData[index];
        if (size - 1 - index >= 0) System.arraycopy(elementData, index + 1, elementData, index, size - 1 - index);
        size--;
        return (E) removedElement;
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

    private void expandArray() {
        int newSize = elementData.length * 2;
        elementData = Arrays.copyOf(elementData, newSize);
    }
}
