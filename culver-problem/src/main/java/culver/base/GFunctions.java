package culver.base;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-15 15:32
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
class GFunctions {
    static double getSphere(double x[]) {
        double sum = 0;
        for (double x1 : x) sum += (x1 * x1);
        return sum;
    }

    static double getRosenbrock(double x[]) {
        double sum = 0;

        for (int i = 0; i < x.length - 1; i++) {
            double t = 100 * (x[i] * x[i] - x[i + 1]) * (x[i] * x[i] - x[i + 1]) + (1 - x[i]) * (1 - x[i]);
            sum += t;
        }

        return sum;
    }

    static double getAckley(double x[]) {
        double sum1 = 0;
        double sum2 = 0;

        for (double x1 : x) {
            sum1 += ((x1 * x1) / x.length);
            sum2 += (Math.cos(2 * Math.PI * x1) / x.length);
        }

        return -20 * Math.exp(-0.2 * Math.sqrt(sum1)) - Math.exp(sum2) + 20 + Math.E;

    }

    static double getGriewank(double x[]) {
        int k = 1;

        double sum = 0;
        double prod = 1;

        for (int i = 0; i < x.length; i++) {
            sum += (x[i] * x[i]);
            prod *= (k * Math.cos(x[i] / Math.sqrt(i + 1)));
        }

        return k + sum / 4000 - prod;
    }

    static double getRastrigin(double x[]) {

        double result = 0.0;
        double a = 10.0;
        double w = 2 * Math.PI;

        for (double x1 : x) {
            result += x1 * x1 - a * Math.cos(w * x1);
        }
        result += a * x.length;

        return result;
    }

    static double getMean(double x[]) {
        double mean = 0;
        for (double x1 : x) mean += Math.abs(x1);

        mean /= x.length;

        return 9 * mean;
    }
}
