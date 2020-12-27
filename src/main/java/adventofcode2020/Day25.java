package adventofcode2020;

import java.math.BigInteger;

/*
 * Feels like cheating, since java has a BigInteger.modPow...
 */
public class Day25 {

    private static final BigInteger FIELD_SIZE = BigInteger.valueOf(20201227);

    static long[] findOneLoopSize(long longPublicKey1, long longPublicKey2) {
        var publicKey1 = BigInteger.valueOf(longPublicKey1);
        var publicKey2 = BigInteger.valueOf(longPublicKey2);
        BigInteger seven = BigInteger.valueOf(7L);
        BigInteger fieldSize = FIELD_SIZE;
        for (int loopSize = 1; loopSize < FIELD_SIZE.longValue(); ++loopSize) {
            BigInteger publicKey = seven.modPow(BigInteger.valueOf(loopSize),
                    fieldSize);
            if (publicKey.equals(publicKey1))
                return new long[] { loopSize, longPublicKey1 };
            else if (publicKey.equals(publicKey2))
                return new long[] { loopSize, longPublicKey2 };
        }

        throw new RuntimeException("Never found loop size");
    }

    static long transform(long subjectNumber, long loopSize) {
        return BigInteger.valueOf(subjectNumber)
                .modPow(BigInteger.valueOf(loopSize), FIELD_SIZE).longValue();
    }

    public static void main(String[] args) {
        long cardPublicKey = 13135480L;
        long doorPublicKey = 8821721L;

        var loopSizeAndPublicKey = findOneLoopSize(cardPublicKey,
                doorPublicKey);

        System.out.print("Day 25 part 1: ");
        if (loopSizeAndPublicKey[1] == cardPublicKey)
            System.out
                    .println(transform(doorPublicKey, loopSizeAndPublicKey[0]));
        else
            System.out
                    .println(transform(cardPublicKey, loopSizeAndPublicKey[0]));
    }

}
