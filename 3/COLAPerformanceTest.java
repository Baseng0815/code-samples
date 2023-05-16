package cola;

import java.util.Arrays;
import java.util.Random;

public class COLAPerformanceTest {

    public static void main(String[] args) {
        final int totalInsertOps = 5_000_000;
        final int totalSearchOps = 500_000;

        Random randomCOLA = new Random();

        COLAImpl<Integer> cola = new COLAImpl<>();
        Integer[] array = new Integer[totalInsertOps];

        long time = -System.currentTimeMillis();
        for (int i = 0; i < totalInsertOps; i++) {
            int nextInt = randomCOLA.nextInt();
            cola.insertElement(nextInt);
            array[i] = nextInt;
        }
        time += System.currentTimeMillis();
        System.out.println("WarmUp Time: " + time);


        array = new Integer[totalInsertOps];
        cola = new COLAImpl<>();

        final int seed = 151; //same seed, same numbers
        Random randomArray = new Random(seed);
        randomCOLA = new Random(seed);

        time = -System.currentTimeMillis();
        for (int i = 0; i < totalInsertOps; i++) {
            array[i] = randomArray.nextInt();
        }
        Arrays.sort(array);
        time += System.currentTimeMillis();
        System.out.println("Array Insertion Time: " + time);


        time = -System.currentTimeMillis();
        for (int i = 0; i < totalInsertOps; i++) {
            cola.insertElement(randomCOLA.nextInt());
        }
        time += System.currentTimeMillis();
        System.out.println("COLA Insertion Time: " + time);


        time = -System.currentTimeMillis();
        for (int i = 0; i < totalSearchOps; i++) {
            cola.searchElement(array[randomArray.nextInt(totalInsertOps)]);
        }

        time += System.currentTimeMillis();
        System.out.println("Search : " + time);
    }

}
