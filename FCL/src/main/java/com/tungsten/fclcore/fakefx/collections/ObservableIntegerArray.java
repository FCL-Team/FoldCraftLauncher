package com.tungsten.fclcore.fakefx.collections;

/**
 * {@code ObservableIntegerArray} is an {@code int[]} array that allows listeners
 * to track changes when they occur. In order to track changes, the internal
 * array is encapsulated and there is no direct access available from the outside.
 * Bulk operations are supported but they always do a copy of the data range.
 *
 * @see ArrayChangeListener
 * @since JavaFX 8.0
 */
public interface ObservableIntegerArray extends ObservableArray<ObservableIntegerArray> {

    /**
     * Copies specified portion of array into {@code dest} array. Throws
     * the same exceptions as {@link System#arraycopy(Object,
     * int, Object, int, int) System.arraycopy()} method.
     * @param srcIndex starting position in the observable array
     * @param dest destination array
     * @param destIndex starting position in destination array
     * @param length length of portion to copy
     */
    public void copyTo(int srcIndex, int[] dest, int destIndex, int length);

    /**
     * Copies specified portion of array into {@code dest} observable array.
     * Throws the same exceptions as {@link System#arraycopy(Object,
     * int, Object, int, int) System.arraycopy()} method.
     * @param srcIndex starting position in the observable array
     * @param dest destination observable array
     * @param destIndex starting position in destination observable array
     * @param length length of portion to copy
     */
    public void copyTo(int srcIndex, ObservableIntegerArray dest, int destIndex, int length);

    /**
     * Gets a single value of array. This is generally as fast as direct access
     * to an array and eliminates necessity to make a copy of array.
     * @param index index of element to get
     * @return value at the given index
     * @throws ArrayIndexOutOfBoundsException if {@code index} is outside
     * array bounds
     */
    public int get(int index);

    /**
     * Appends given {@code elements} to the end of this array. Capacity is increased
     * if necessary to match the new size of the data.
     * @param elements elements to append
     */
    public void addAll(int... elements);

    /**
     * Appends content of a given observable array to the end of this array.
     * Capacity is increased if necessary to match the new size of the data.
     * @param src observable array with elements to append
     */
    public void addAll(ObservableIntegerArray src);

    /**
     * Appends a portion of given array to the end of this array.
     * Capacity is increased if necessary to match the new size of the data.
     * @param src source array
     * @param srcIndex starting position in source array
     * @param length length of portion to append
     */
    public void addAll(int[] src, int srcIndex, int length);

    /**
     * Appends a portion of given observable array to the end of this array.
     * Capacity is increased if necessary to match the new size of the data.
     * @param src source observable array
     * @param srcIndex starting position in source array
     * @param length length of portion to append
     */
    public void addAll(ObservableIntegerArray src, int srcIndex, int length);

    /**
     * Replaces this observable array content with given elements.
     * Capacity is increased if necessary to match the new size of the data.
     * @param elements elements to put into array content
     * @throws NullPointerException if {@code src} is null
     */
    public void setAll(int... elements);

    /**
     * Replaces this observable array content with a copy of portion of
     * a given array.
     * Capacity is increased if necessary to match the new size of the data.
     * @param src source array to copy.
     * @param srcIndex starting position in source observable array
     * @param length length of a portion to copy
     * @throws NullPointerException if {@code src} is null
     */
    public void setAll(int[] src, int srcIndex, int length);

    /**
     * Replaces this observable array content with a copy of given observable array.
     * Capacity is increased if necessary to match the new size of the data.
     * @param src source observable array to copy.
     * @throws NullPointerException if {@code src} is null
     */
    public void setAll(ObservableIntegerArray src);

    /**
     * Replaces this observable array content with a portion of a given
     * observable array.
     * Capacity is increased if necessary to match the new size of the data.
     * @param src source observable array to copy.
     * @param srcIndex starting position in source observable array
     * @param length length of a portion to copy
     * @throws NullPointerException if {@code src} is null
     */
    public void setAll(ObservableIntegerArray src, int srcIndex, int length);

    /**
     * Copies a portion of specified array into this observable array. Throws
     * the same exceptions as {@link System#arraycopy(Object,
     * int, Object, int, int) System.arraycopy()} method.
     * @param destIndex the starting destination position in this observable array
     * @param src source array to copy
     * @param srcIndex starting position in source array
     * @param length length of portion to copy
     */
    public void set(int destIndex, int[] src, int srcIndex, int length);

    /**
     * Copies a portion of specified observable array into this observable array.
     * Throws the same exceptions as {@link System#arraycopy(Object,
     * int, Object, int, int) System.arraycopy()} method.
     * @param destIndex the starting destination position in this observable array
     * @param src source observable array to copy
     * @param srcIndex starting position in source array
     * @param length length of portion to copy
     */
    public void set(int destIndex, ObservableIntegerArray src, int srcIndex, int length);

    /**
     * Sets a single value in the array. Avoid using this method if many values
     * are updated, use {@linkplain #set(int, int[], int, int)} update method
     * instead with as minimum number of invocations as possible.
     * @param index index of the value to set
     * @param value new value for the given index
     * @throws ArrayIndexOutOfBoundsException if {@code index} is outside
     * array bounds
     */
    public void set(int index, int value);

    /**
     * Returns an array containing copy of the observable array.
     * If the observable array fits in the specified array, it is copied therein.
     * Otherwise, a new array is allocated with the size of the observable array.
     *
     * @param dest the array into which the observable array to be copied,
     *          if it is big enough; otherwise, a new int array is allocated.
     *          Ignored, if null.
     * @return an int array containing the copy of the observable array
     */
    public int[] toArray(int[] dest);

    /**
     * Returns an array containing copy of specified portion of the observable array.
     * If specified portion of the observable array fits in the specified array,
     * it is copied therein. Otherwise, a new array of given length is allocated.
     *
     * @param srcIndex starting position in the observable array
     * @param dest the array into which specified portion of the observable array
     *          to be copied, if it is big enough;
     *          otherwise, a new int array is allocated.
     *          Ignored, if null.
     * @param length length of portion to copy
     * @return an int array containing the copy of specified portion the observable array
     */
    public int[] toArray(int srcIndex, int[] dest, int length);

}
