package zettel5;

import java.util.HashMap;
import java.util.Map;

public class Aufgabe1b {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        System.out.println(hasKSum(arr, 2));
    }

    public static boolean hasKSum(int[] array, int k) { // O(n)
        Map<Object, ?> lookup = new HashMap<>(); // O(1)

        for (int num : array) // O(n)
            lookup.put(num, null); // O(1)

        for (int num : array) { // O(n)
            lookup.remove(num); // O(1)
            if (lookup.containsKey(k - num)) // O(1)
                return true; // O(1)

            lookup.put(num, null); // O(1)
        }

        return false; // O(1)
    }
}
