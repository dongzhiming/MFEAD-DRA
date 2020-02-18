package culver;

import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static weka.core.Utils.normalize;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 2019/8/22 下午11:31
 * @Descriptiom: #
 */
public class UniformPoint {

    public static double[] getRandomWeightVector(int nobj) {
        JMetalRandom random = JMetalRandom.getInstance();

        double[] weightVector = new double[nobj];

        double weightConstraint = 0;

        boolean bFeasible;
        do {
            double weightsSum = 0;
            for (int i = 0; i < nobj; i++) {
                double R = random.nextDouble();
                if (i < nobj - 1) {
                    weightVector[i] = (1 - weightsSum) * (1 - Math.pow(R, 1 / ((double) nobj - i - 1)));
                } else {
                    weightVector[i] = (1 - weightsSum);
                }
                weightsSum += weightVector[i];
            }

            for (int i = 0; i < nobj; i++) {
                weightVector[i] = weightVector[i] * 1.1 - 0.05;
            }

            normalize(weightVector);

            for (int i = 0; i < nobj; i++) {
                if (weightVector[i] < 0)
                    weightVector[i] = 0;
                if (weightVector[i] > 1)
                    weightVector[i] = 1;
            }


            bFeasible = true;
            for (int i = 0; bFeasible && (i < nobj); i++) {
                bFeasible = weightVector[i] >= weightConstraint;
            }
        } while (!bFeasible);

        return weightVector;
    }

    public static double[][] generateWeightVector(List<Integer> divisions, int nobj) {
        long needed = combination(divisions.get(0) + nobj - 1, nobj - 1) + (divisions.size() > 1 ? combination(divisions.get(1) + nobj - 1, nobj - 1) : 0);

        return generateWeightVector(divisions, (int) needed, nobj);
    }

    public static double[][] generateWeightVector(int needed, int nobj) {
        return generateWeightVector(needed, nobj, 20);
    }

    public static double[][] generateWeightVector(int needed, int nobj, int times) {
        List<Integer> divisions = wise(needed, nobj);
        if (combination(divisions.get(0) + nobj - 1, nobj - 1) + (divisions.size() > 1 ? combination(divisions.get(1) + nobj - 1, nobj - 1) : 0) != needed) {
            divisions = wise(needed * times, nobj);
        }

        return generateWeightVector(divisions, needed, nobj);
    }

    private static double[][] generateWeightVector(List<Integer> divisions, int needed, int nobj) {
        List<ArrayPoint> referencePoints = new Vector<>();

        ArrayPoint refPoint = new ArrayPoint(nobj);
        generateRecursive(referencePoints, refPoint, nobj, divisions.get(0), divisions.get(0), 0);

        if (divisions.size() > 1) {
            List<ArrayPoint> inside = new Vector<>();
            generateRecursive(inside, refPoint, nobj, divisions.get(1), divisions.get(1), 0);

            double center = 1.0 / nobj;
            for (int i = 0; i < inside.size(); i++) {
                for (int j = 0; j < inside.get(i).getDimension(); j++) {
                    inside.get(i).setValue(j, (center + inside.get(i).getValue(j)) / 2);
                }

                referencePoints.add(inside.get(i));
            }
        }

        double[][] weights = new double[referencePoints.size()][nobj];

        for (int i = 0; i < referencePoints.size(); i++) {
            for (int j = 0; j < nobj; j++) {
                weights[i][j] = referencePoints.get(i).getValue(j);
            }
        }

        return Filter.filter(weights, needed);
    }

    private static List<Integer> wise(int needed, int nobj) {
        int p1 = 0, p2 = 0;
        while (combination(p1 + nobj, nobj - 1) <= needed) {
            p1++;
        }

        if (p1 < nobj) {
            while ((combination(p1 + nobj - 1, nobj - 1) +
                    combination(p2 + nobj, nobj - 1)) <= needed) {
                p2++;
            }
        }

        return p2 > 0 ? Arrays.asList(p1, p2) : Arrays.asList(p1);
    }

    private static void generateRecursive(
            List<ArrayPoint> referencePoints,
            ArrayPoint refPoint,
            int numberOfObjectives,
            int left,
            int total,
            int element) {
        if (element == (numberOfObjectives - 1)) {
            refPoint.setValue(element, (double) left / total);
            referencePoints.add(new ArrayPoint(refPoint));
        } else {
            for (int i = 0; i <= left; i += 1) {
                refPoint.setValue(element, (double) i / total);

                generateRecursive(referencePoints, refPoint, numberOfObjectives, left - i, total, element + 1);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<ArrayList<Integer>> combine(int n, int k) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        if (n <= 0 || n < k)
            return result;

        ArrayList<Integer> item = new ArrayList<>();
        dfs(n, k, 1, item, result); // because it need to begin from 1

        return result;
    }

    private static void dfs(int n, int k, int start, ArrayList<Integer> item,
                            ArrayList<ArrayList<Integer>> res) {
        if (item.size() == k) {
            res.add(new ArrayList<>(item));
            return;
        }

        for (int i = start; i <= n; i++) {
            item.add(i);
            dfs(n, k, i + 1, item, res);
            item.remove(item.size() - 1);
        }
    }

    /**
     * @param pre
     * @param pos
     * @return
     */
    private static long factorial(int pre, int pos) {
        if (pre < pos) {
            return factorial(pos, pre);
        }

        return (pre > pos) ? pre * factorial(pre - 1, pos) : pos;
    }

    /**
     * @param n
     * @return
     */
    private static long factorial(int n) {
        return factorial(n, 1);
//        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    /**
     * @param n
     * @param m
     * @return
     */
    private static long arrangement(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) : 0;
    }

    /**
     * @param n
     * @param m
     * @return
     */
    private static long combination(int n, int m) {
        if (n < m) return 0;

        if (m < n - m) {
            return factorial(n - m + 1, n) / factorial(m);
        } else {
            return factorial(m + 1, n) / factorial(n - m);
        }

//        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }
}
