import java.math.BigInteger;

public static BigInteger improvedFibonacci(int N, BigInteger[] resultArray) {
        if (N == 0) return BigInteger.ZERO;
        if (N == 1) return BigInteger.ONE;

        BigInteger tempResult = resultArray[N - 2].add(resultArray[N - 1]);
        resultArray[N] = tempResult;
        return resultArray[N];
    }

 public static void main(String[] args) {
        BigInteger[] resultArray = new BigInteger[100];
        resultArray[0] = BigInteger.ZERO;
        resultArray[1] = BigInteger.ONE;

        for (int N = 0; N < 100; N++)
            StdOut.println(N + " " + improvedFibonacci(N, resultArray));
    }
