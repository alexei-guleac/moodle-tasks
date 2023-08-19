package com.example.app;

import java.util.EmptyStackException;
import java.util.LinkedList;

/**
 * Basic implementation of a stack using a
 * LinkedList to store data
 *
 * @param <E> type of object stack holds
 * @author Eric Bakan
 */
class Stack<E> {

    private final LinkedList<E> list;

    /**
     * Default constructor, initializes LinkedList
     */
    public Stack() {
        list = new LinkedList<E>();
    }

    /**
     * Returns whether stack is empty or not
     *
     * @return whether stack is empty or not
     */
    public boolean isEmpty() {
        return list.size() == 0;
    }

    /**
     * Pushes an object to the top of the stack
     *
     * @param e object to be pushed
     * @return pushed object
     */
    public E push(E e) {
        list.addFirst(e);
        return list.getFirst();
    }

    /**
     * Pops an object off the top of the stack
     *
     * @return popped object
     */
    public E pop() {
        if (isEmpty())
            throw new EmptyStackException();
        return list.removeFirst();
    }

    /**
     * Returns the object at the top of the stack
     * without removing it
     *
     * @return first object on the stack
     */
    public E peek() {
        if (isEmpty())
            throw new EmptyStackException();
        return list.getFirst();
    }

}
