package culver.problem.multitaskdoubleproblem;

import culver.problem.MultiTaskProblem;
import culver.solution.multitaskdoublesolution.MultiTaskDoubleSolution;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-3-26 上午11:27
 * @Descriptiom: #
 */
public interface MultiTaskDoubleProblem extends MultiTaskProblem<MultiTaskDoubleSolution> {
    // TODO: 2020/2/7 下面两个函数似乎可以不要，因为都是0-1
    Double getLowerBound(int index);

    Double getUpperBound(int index);
}
