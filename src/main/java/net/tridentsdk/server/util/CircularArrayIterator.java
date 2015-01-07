package net.tridentsdk.server.util;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator for {@link net.tridentsdk.server.util.ConcurrentCircularArray}
 * <p/>
 * <p>Removals are done on a best effort basis, and are not guaranteed to reflect the
 * same value at removal as they do when this object is created, use with caution.</p>
 * <p/>
 * <p>Should only be used by one thread at a time</p>
 */
@NotThreadSafe
public class CircularArrayIterator<E> implements Iterator<E> {

    private final ConcurrentCircularArray<E> parent;

    private final Object[] contents;
    private final int elements;

    // The current index to read from
    private int current;

    // The number of items read
    private int read;


    /**
     * Creates a new Iterator based on the ConcurrentCircularArray
     * <p/>
     * <p>Expects a read lock to be in place when constructing</p>
     *
     * @param parent
     */
    protected CircularArrayIterator(ConcurrentCircularArray<E> parent) {
        this.parent = parent;

        contents = new Object[parent.getMaxSize()];
        int notNulls = 0;
        for (int i = 0; i < parent.getMaxSize(); i++) {
            contents[i] = parent.backing.get(i);
            if (contents[i] != null) {
                notNulls++;
            }
        }

        elements = notNulls;
    }

    @Override
    public boolean hasNext() {
        return read != elements;
    }

    /**
     * @return null if an error, otherwise the next value
     * @throws NoSuchElementException
     */
    @Override
    public E next() throws NoSuchElementException {
        if (read >= elements) {
            throw new NoSuchElementException("Iterator has run out of items");
        }
        current++;
        if (current >= parent.getMaxSize()) {
            current = 0;
        }
        read++;
        // skips null values, effectively a while (value != null) loop with a limit to prevent infinite loops
        for (int i = 0; i < parent.getMaxSize(); i++) {
            if (contents[current] != null) {
                return (E) contents[current];
            }
            current++;
            if (current >= parent.getMaxSize()) {
                current = 0;
            }
        }

        return null;
    }

    @Override
    public void remove() {
        parent.remove(current, null);
    }

    /**
     * Guarantees that this value has not been changed since the iterator was created before removing the value
     *
     * @returns whether or not this removal was successful
     */
    public boolean strictRemove() {
        return parent.remove(current, (E) contents[current]);
    }
}