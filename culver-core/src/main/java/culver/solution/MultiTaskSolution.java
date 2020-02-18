package culver.solution;

import org.uma.jmetal.solution.Solution;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-3-26 下午9:47
 * @Descriptiom: #
 */
public interface MultiTaskSolution<T, S> extends Solution<T> {
    void setSkillFactor(int skillFactor);

    void setFactorialRank(int task, int rank);

//    void setScalarFitness(double scalarFitness);

    int getSkillFactor();

    int getFactorialRank(int task);

    double getScalarFitness();

    S getSolution(int index);

    void setSolution(int index, S solution);
}
