package model.MARK_II.generalAlgorithm;

import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version 3/10/2015
 */
public class SDRStatistics {
    // notes on below variables @ https://github.com/WalnutiQ/WalnutiQ/issues/152
    private int n; // number of neurons
    private int w; // number of activeNeurons and also upper bound of b
    private int theta; // theta = lower bound of b

    public SDRStatistics(int numberOfNeurons, int numberOfActiveNeurons,
                         int minimumNumberOfOverlapNeuronsForMatch) {
        this.n = numberOfNeurons;
        this.w = numberOfActiveNeurons;
        this.theta = minimumNumberOfOverlapNeuronsForMatch;

        if (this.n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        if (this.n < this.w || this.n <= this.theta) {
            throw new IllegalArgumentException("n must be >= than w and n " +
                    "must be >= theta");
        }
    }

    /**
     * This method assumes there is a SDR set M that is a union of SDRs to
     * represent a specific idea. SDR x is usually intersected with SDR set M
     * to find how many "bits" are in common. However if SDR set M is the union
     * of too many SDRs than any SDR x will "match" with M. This method
     * calculates the probability of false positive when SDR set M gets too
     * large for exact matches between SDR x and SDRs within M where theta = w.
     *
     * @param SDRSetMSize Number of SDRs in M. In HTM's SDRSetMSize usually > 1024.
     *
     * @return Probability of false positive for SDR x "matching" with
     *         SDR set M.
     */
    double probabilityOfFalsePositiveForSDRxIntersectSDRSetM(int SDRSetMSize) {
        // s = Percentage of ON bits in all SDRs x and in M.
        double s = (double) this.w / (double) this.n;
        double probabilityAGivenBitIsStill0 = Math.pow(1 - s, SDRSetMSize);

        // In this case w = Number of active "bits" in all SDRs.
        double answer = Math.pow(1 - probabilityAGivenBitIsStill0, this.w);
        return answer;
    }

    /**
     * @return a double value between 0 and 100 of the probability of
     * a false positive.
     */
    String probabilityOfFalsePositive() {
        BigInteger numerator = new BigInteger("0");
        for (int b = this.theta; b <= this.w; b++) {
            numerator = numerator.add(overlapSet(b));
        }

        BigInteger denominator = combination(this.n, this.w);

        double probabilityOfFalsePositive = (numerator.floatValue() /
                denominator.floatValue()) * 100;

        double percentageOfNoise = (Double.valueOf(this.theta) / Double.valueOf(this.w)) * 100;

        DecimalFormat df1 = new DecimalFormat("#.00");
        DecimalFormat df2 = new DecimalFormat("#.000000");
        String statistics = "With " + df1.format(percentageOfNoise) + "% " +
                "noise there is a " + df2.format(probabilityOfFalsePositive)
                + "% of false positive";

        return statistics;
    }

    BigInteger overlapSet(int b) {
        if (b > this.w) {
            throw new IllegalArgumentException("b must be <= than w");
        }
        return combination(this.w, b).multiply(combination(this.n - this.w, this.w - b));
    }

    /**
     * n choose k = n!/(k!(n-k)!)
     *
     * @param n Total number of elements.
     * @param k Number of elements to combine at one time.
     * @return The number of times k elements can be arranged out of n elements
     *         where order of arranging things do NOT make arrangement unique.
     */
    static BigInteger combination(int n, int k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n - k)));
    }

    static BigInteger factorial(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("You cannot take the factorial" +
                    "of the negative integer " + n);
        }
        BigInteger result = BigInteger.valueOf(1);
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
