package mergesort;

public class MergeSort {

    public static int sortAndCount(int[] array) {
        return sortAndCount(array, 0, array.length - 1);
    }

    private static int sortAndCount(int[] array, int low, int high) {
        int inversions = 0;

        if (low < high) {
            int split = low + (high - low) / 2;
            inversions += sortAndCount(array, low, split);
            inversions += sortAndCount(array, split + 1, high);
            inversions += merge(array, low, split, high);
        }

        return inversions;
    }

    private static int merge(int[] array, int low, int split, int high) {
        int lowElements = split - low + 1;
        int[] lowCopy = new int[lowElements];
        System.arraycopy(array, low, lowCopy, 0, lowElements);

        int highElements = high - (split + 1) + 1;
        int[] highCopy = new int[highElements];
        System.arraycopy(array, split + 1, highCopy, 0, highElements);

        int lowPointer = 0;
        int highPointer = 0;
        int inversions = 0;

        int arrayPointer = low;

        while (lowPointer < lowCopy.length && highPointer < highCopy.length) {
            if (lowCopy[lowPointer] <= highCopy[highPointer])
                array[arrayPointer++] = lowCopy[lowPointer++];
            else {
                array[arrayPointer++] = highCopy[highPointer++];

                // All following elements in lowCopy are inversions w.r.t. the current element in highCopy
                inversions += lowElements - lowPointer;
            }
        }
        while (lowPointer < lowCopy.length)
            array[arrayPointer++] = lowCopy[lowPointer++];
        while (highPointer < highCopy.length)
            array[arrayPointer++] = highCopy[highPointer++];
        return inversions;
    }

    public static void main(String[] args) {
        int result = sortAndCount(new int[]{2, 5, 9, 4, 1, 13});
        System.out.println(result);
    }

}
