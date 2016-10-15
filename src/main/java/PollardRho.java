import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollardRho {
    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE = new BigInteger("1");
    private final static BigInteger TWO = new BigInteger("2");
    private final static SecureRandom random = new SecureRandom();

    public static BigInteger rho(BigInteger N) {
        BigInteger divisor;
        BigInteger c = new BigInteger(N.bitLength(), random);
        BigInteger x = new BigInteger(N.bitLength(), random);
        BigInteger xx = x;

        // check divisibility by 2
        if (N.mod(TWO).compareTo(ZERO) == 0) return TWO;

        do {
            x = x.multiply(x).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            divisor = x.subtract(xx).gcd(N);
        } while ((divisor.compareTo(ONE)) == 0);

        return divisor;
    }

    public static List<BigInteger> factor(BigInteger N) {
        if (N.compareTo(ONE) == 0)
            return Arrays.asList(new BigInteger[]{ONE});
        else if (N.compareTo(ZERO) == 0) {
            return Arrays.asList(new BigInteger[]{ZERO});
        } else if (N.compareTo(ONE) == -1)
            return factor(N.multiply(BigInteger.valueOf(-1)));
        else {
            List<BigInteger> out = new ArrayList<>();
            factor(N, out);
            return out;
        }
    }

    public static void factor(BigInteger N, List<BigInteger> list) {
        if (N.compareTo(ONE) == 0) return;
        if (N.isProbablePrime(20)) {
            list.add(N);
            System.out.println(N);
            return;
        }
        BigInteger divisor = rho(N);
        factor(divisor, list);
        factor(N.divide(divisor), list);
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        BigInteger N = new BigInteger("618970019642690137449562111");
        System.out.printf("took %dms\n", System.currentTimeMillis() - start);
        List l = new ArrayList<>();
        System.out.println(factor(N));
    }
}