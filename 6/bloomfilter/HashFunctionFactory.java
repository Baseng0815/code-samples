package zettel6.bloomfilter;

public interface HashFunctionFactory<E> {

    HashFunction<E>[] generate(int numberOfHashFunctions);
}
