package zettel6.bloomfilter;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class IntegerFactory implements HashFunctionFactory<Integer> {
    @Override
    public HashFunction<Integer>[] generate(int numberOfHashFunctions) {
        HashFunction<Integer>[] functions = new HashFunction[numberOfHashFunctions];
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int finalI = i;
            functions[i] = (e-> 17 * e + finalI * 31 * e);
        }
        return functions;
    }
}
