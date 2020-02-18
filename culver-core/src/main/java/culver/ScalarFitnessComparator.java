package culver;

import culver.solution.MultiTaskSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-1-17 10:43
 * @Descriptiom: #
 */
public class ScalarFitnessComparator<S extends MultiTaskSolution<?, ? extends Solution<?>>> implements Comparator<S>, Serializable {
    public enum Ordering {ASCENDING, DESCENDING}

    private Ordering order;

    public ScalarFitnessComparator(Ordering order) {
        this.order = order;
    }

    public ScalarFitnessComparator() {
        this(Ordering.DESCENDING);
    }

    /**
     * @param solution1
     * @param solution2
     * @Description:
     */
    @Override
    public int compare(S solution1, S solution2) {
        if (solution1 == null) {
            throw new JMetalException("Solution1 is null");
        } else if (solution2 == null) {
            throw new JMetalException("Solution2 is null");
        }

        int result;
        double sfn1 = solution1.getScalarFitness();
        double sfn2 = solution2.getScalarFitness();

        if (order == Ordering.ASCENDING) {
            result = Double.compare(sfn1, sfn2);
        } else {
            result = Double.compare(sfn2, sfn1);
        }

        return result;
    }
}
