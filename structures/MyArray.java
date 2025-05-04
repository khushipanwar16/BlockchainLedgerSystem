package structures;

public class MyArray<T> {
    private Object[] data;
    private int size;
    private static final int INITIAL_CAPACITY = 4; 

    public MyArray() {
        this(INITIAL_CAPACITY);
    }

    public MyArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }
    
    public void add(T element) {
        if (size == data.length) {
            resizeManually();
        }
        data[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        validateIndex(index);
        return (T) data[index];
    }

    public int size() {
        return size;
    }

    public MyArray<T> getRange(int start, int end) {
        validateIndex(start);
        validateIndex(end-1); 
        
        MyArray<T> slice = new MyArray<>(end - start);
        for (int i = start; i < end; i++) {
            slice.add(this.get(i));
        }
        return slice;
    }

    private void resizeManually() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for size %d", index, size)
            );
        }
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null; 
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        
        String result = "[";
        for (int i = 0; i < size; i++) {
            result += data[i];
            if (i < size - 1) result += ", ";
        }
        return result + "]";
    }
}