package com.alg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  CS4050 - Algorithms and Algorithm Analysis
 *
 *  ComboQuicksortInsertionSort.java
 *
 *  Imports a provided file containing consecutive integers of random order into an array to
 *  use the a combination of the quicksort and insertion sort algorithms to improve overall
 *  efficiency. This works because Quicksort performs better on large partitions of data,
 *  while Insertion Sort performs better on smaller sizes of data. and calculate the time
 *  cost of sorting.
 *
 *  By changing the variable "MAX_REGION_SIZE_QS" the algorithm can be adjusted to improve
 *  efficiency according to the size of the provided data.
 **/

public class ComboQuicksortInsertionSort {

    // ADJUST THIS INT BASED ON SIZE OF DATA
    final static int MAX_REGION_SIZE_QS = 11;

    private int[] data;
    private int len;

    // create int array for importing data
    public ComboQuicksortInsertionSort(int max) {
        data = new int[max];
        len = 0;
    }

    // init (helper function)
    public void quickSort() {
        recursiveComboQuicksortInsertionSort(0, len - 1);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     *  recursiveComboQuicksortInsertionSort algorithm
     *  implements median of thee approach to determine
     *  the pivot and uses insertion sort on partitions
     *  less than the specified max region size.
     *
     *  manualSort added to improve small data sets
     *  with partitions containing 3 or less numbers.
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void recursiveComboQuicksortInsertionSort(int left, int right) {
        int size = right - left + 1;
        // if range is larger than MAX_REGION_SIZE use Quicksort, otherwise use InsertionSort
        if (size < MAX_REGION_SIZE_QS)
            insertionSort(left, right);
        else {
            int median = medianOf3(left, right);    // determine partition
            int partition = partitionIterator(left, right, median); // create partition
            recursiveComboQuicksortInsertionSort(left, partition - 1);  // sort left partition
            recursiveComboQuicksortInsertionSort(partition + 1, right); // sort right partition
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  medianOf3 used to determine the location of
     *  the pivot used for recursiveQuicksort.
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public int medianOf3(int left, int right) {
        int center = (left + right) / 2;
        // order left & center
        if (data[left] > data[center])
            swap(left, center);
        // order left & right
        if (data[left] > data[right])
            swap(left, right);
        // order center & right
        if (data[center] > data[right])
            swap(center, right);

        swap(center, right - 1);
        return data[right - 1];
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  partitionIterator controls partition flow
     *  using Hoare's partitioning algorithm
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public int partitionIterator(int left, int right, long pivot) {
        int leftPtr = left; // right of first elem
        int rightPtr = right - 1; // left of pivot
        while (true) {
            while (data[++leftPtr] < pivot) // scan for larger int
                ;
            while (data[--rightPtr] > pivot)  // scan for smaller int
                ;
            if (leftPtr >= rightPtr) // pointers crossed: break
                break;
            else
                swap(leftPtr, rightPtr); // pointers never crossed: swap elements
        }
        swap(leftPtr, right - 1); // restore pivot
        return leftPtr; // return pivot location
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  insertionSort is to improve the algorithm's
     *  efficiency when partitions less than some
     *  specified range/size
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void insertionSort(int left, int right) {
        int in; // new item (replacing old)
        int out; // old item (being removed)
        for (out = left + 1; out <= right; out++) {
            int temp = data[out];
            in = out;
            // when smaller, remember 'in' and perform shift
            while (in > left && data[in - 1] >= temp) {
                data[in] = data[in - 1];
                --in;
            }
            data[in] = temp;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  swap indexes [ints] of two variables/arrays
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void swap(int d1, int d2) {
        int temp = data[d1];
        data[d1] = data[d2];
        data[d2] = temp;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  Array builder for raw data
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void insert(int value) {
        data[len] = value;
        len++;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  Display the current contents of 'data' array
     *  primarily for debugging purposes. [unused]
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void display() {
        System.out.print("Data: ");
        for (int j = 0; j < len; j++)
            System.out.print(data[j] + " ");
        System.out.println("");
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  readArrayFromFile reads a list of integers
     *  from a provided file and adds each of them
     *  to an Array of appropriate size.
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public static int[] ReadArrayFromFile(String file) {
        int [] numArray;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))))
        {
            // use amount of numbers contained in file to determine the length of the Array
            String[] arr = reader.lines().toArray(size->new String[size]);
            numArray = new int [arr.length];
            for (int i = 0; i < arr.length; i++)   // convert Array contents from String to Integer
            {
                numArray[i] = Integer.valueOf(arr[i]);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return numArray;
    }

    public static void main(String[] args) {
        int [] Array;
        Array = ReadArrayFromFile(System.getProperty("user.dir") + "/src/com/alg/Randomized_Numbers_100k.txt"); // filepath

        if (Array!=null) // is it even worth it?
        {
            String name = "timing";
            long first_ns, last_ns, time_ns;    // variables to keep track of calculation time
            float time_ms;

            ComboQuicksortInsertionSort arr = new ComboQuicksortInsertionSort(Array.length); // init array

            int n;
            for (int i=0; i < Array.length; i++) // convert and fill array
            {
                n = Array[i];
                arr.insert(n);
            }

            System.out.println("Running ComboQuicksortInsertionSort.java . . .");
            //arr.display();    // display unsorted numbers (original array)

            /** START TIMER **/
            first_ns = System.nanoTime();

            /** QUICKSORT EXECUTE **/
            arr.quickSort();

            /** STOP TIMER **/
            last_ns = System.nanoTime();

            //arr.display();    // display sorted numbers (final sorted array)
            time_ns = last_ns - first_ns;
            time_ms = ((float)time_ns/1000000);
            System.out.println("\nUsing quicksort on regions of size " + MAX_REGION_SIZE_QS + " or larger" +
                               " and then switching \nto insertion sort for partitions less than size " +
                    MAX_REGION_SIZE_QS + " sorted an array \nof " + Array.length + " unordered " +
                               "sequential numbers and required the following \namount of time: \n");
            System.out.println(time_ns + " nanoseconds");
            System.out.println(time_ms + " milliseconds");
        }

    }

}