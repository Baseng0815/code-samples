package zettel7;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class HeapPerformanceTest {

    private static void testStacks(int seed, int size) {
        Random random2 = new Random(seed);
        SimpleStack<Integer> simpleStack = new HeapStack<>(size);
        long startInsertSimple = System.nanoTime();
        for (int i = 0; i < size; i++) {
            simpleStack.push(random2.nextInt());
        }
        long endInsertSimple = System.nanoTime();
        System.out.println("HeapStack Insert: "
                + TimeUnit.MILLISECONDS.convert(endInsertSimple - startInsertSimple, TimeUnit.NANOSECONDS));


        long startQuerySimple = System.nanoTime();
        for (int i = 0; i < size; i++) {
            simpleStack.pop();
        }
        long endQuerySimple = System.nanoTime();
        System.out.println("HeapStack Query: "
                + TimeUnit.MILLISECONDS.convert(endQuerySimple - startQuerySimple, TimeUnit.NANOSECONDS));

        Random random = new Random(seed);
        Stack<Integer> stack = new Stack<>();
        long startInsert = System.nanoTime();
        for (int i = 0; i < size; i++) {
            stack.push(random.nextInt());
        }
        long endInsert = System.nanoTime();
        System.out.println("Stack Insert: "
                + TimeUnit.MILLISECONDS.convert(endInsert - startInsert, TimeUnit.NANOSECONDS));

        long startQuery = System.nanoTime();
        for (int i = 0; i < size; i++) {
            stack.pop();
        }
        long endQuery = System.nanoTime();
        System.out.println("Stack Query: "
                + TimeUnit.MILLISECONDS.convert(endQuery - startQuery, TimeUnit.NANOSECONDS));
    }

    private static void testQueues(int seed, int size) {
        Random random2 = new Random(seed);
        SimpleFIFOQueue<Integer> simpleQueue = new HeapQueue<>(size);
        long startInsertSimple = System.nanoTime();
        for (int i = 0; i < size; i++) {
            simpleQueue.add(random2.nextInt());
        }
        long endInsertSimple = System.nanoTime();
        System.out.println("HeapQueue Insert: "
                + TimeUnit.MILLISECONDS.convert(endInsertSimple - startInsertSimple, TimeUnit.NANOSECONDS));

        long startQuerySimple = System.nanoTime();
        for (int i = 0; i < size; i++) {
            simpleQueue.poll();
        }
        long endQuerySimple = System.nanoTime();
        System.out.println("HeapQueue Query: "
                + TimeUnit.MILLISECONDS.convert(endQuerySimple - startQuerySimple, TimeUnit.NANOSECONDS));

        Random random = new Random(seed);
        ArrayDeque<Integer> queue = new ArrayDeque<>(size);
        long startInsert = System.nanoTime();
        for (int i = 0; i < size; i++) {
            queue.add(random.nextInt());
        }
        long endInsert = System.nanoTime();
        System.out.println("Queue Insert: "
                + TimeUnit.MILLISECONDS.convert(endInsert - startInsert, TimeUnit.NANOSECONDS));


        long startQuery = System.nanoTime();
        for (int i = 0; i < size; i++) {
            queue.poll();
        }
        long endQuery = System.nanoTime();
        System.out.println("Queue Query: "
                + TimeUnit.MILLISECONDS.convert(endQuery - startQuery, TimeUnit.NANOSECONDS));
    }

    public static void main(String[] args) {
        //Warmup
        testStacks(14, 10_000_000);
        //Experiment
        testStacks(13, 10_000_000);
        System.out.println("----");
        //Warmup
        testQueues(14, 10_000_000);
        //Experiment
        testQueues(13, 10_000_000);
    }

}
