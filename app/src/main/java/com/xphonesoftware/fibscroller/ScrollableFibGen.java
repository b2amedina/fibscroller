package com.xphonesoftware.fibscroller;

import android.util.Log;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * Fibonacci sequence generator which is optimized for scrolling
 *
 * Generating an interval of fibonacci sequences is quick as long
 *  as you have the two values prior to the interval
 *
 * This implementation
 *
 *   1. trades off a small amount of memory to ensure calculating
 *      a range is quick across different devices when scrolling
 *      in both directions and across config changes like rotating
 *      the device
 *   2. assumes that it will always start at 0 and that the start
 *      values required for any given range are in the cache
 *   3. does not use recursion to prevent blowing the stack
 *
 *  In the picture below, X's indicate values which are cached
 *
 *  0 1 . . . . . . . 19 0 . . . . . . . . 19 . . . . . . . . . 19
 *  X X             X  X                 X  X                 X  X
 *
 * Created by davidmedina on 8/1/15.
 */
public class ScrollableFibGen {

    private static final String TAG = "ScrollableFibSequence";
    public static final int RANGE_SIZE = 20;

    private HashMap<Integer, BigInteger> mCache;

    public ScrollableFibGen() {
        mCache = new HashMap<Integer, BigInteger>();
        mCache.put(0, BigInteger.ZERO);
        mCache.put(1, BigInteger.ONE);
    }

    /**
     * This method will generate a range of fibonacci sequences
     * based off of the start value
     *
     * @param n - start
     * @return array of fibonacci sequences
     */
    public BigInteger[] generateRange(int n) {
        BigInteger[] result = new BigInteger[RANGE_SIZE];

        // start = 2 fib numbers before range starts
        int start = (n / RANGE_SIZE) * (RANGE_SIZE - 2);

        int index = 0;
        BigInteger prev1 = mCache.get(start);
        BigInteger prev2 = mCache.get(start + 1);

        int max = RANGE_SIZE;
        if (start == 0) {
            // special case for first range
            result[index++] = prev1;
            result[index++] = prev2;
            max -= 2;
        }

        // generate range of fib sequence iteratively
        //  using the 2 prev seq retrieved from the cache
        for(int i = 0; i < max; i++) {
            BigInteger savePrev1 = prev1;
            prev1 = prev2;
            prev2 = savePrev1.add(prev2);
            result[index++] = prev2;
        }

        // add last 2 entries in the range to the cache
        //  so it can be used for the next range
        index = start + RANGE_SIZE - 2;
        mCache.put(index++, result[RANGE_SIZE - 2]);
        mCache.put(index, result[RANGE_SIZE - 1]);

        return result;
    }

    public static void log(int n, BigInteger[] range) {
        for (int i = 0; i < range.length; i++) {
            Log.d(TAG, "fib(" + (n + i) + ") = " + range[i]);
        }
    }
}
