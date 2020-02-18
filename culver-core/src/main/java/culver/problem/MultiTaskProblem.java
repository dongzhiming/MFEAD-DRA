package culver.problem;

import org.uma.jmetal.problem.Problem;

import java.io.Serializable;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-3-26 上午10:57
 * @Descriptiom: #
 */
public interface MultiTaskProblem<S> extends Serializable {

    int getNumberOfTasks();

    // TODO: 2020/2/7 这个地方泛型的利用有些不妥
    Problem getTask(int index);

    int getNumberOfVariables();

    String getName();

    void evaluate(S solution);

    S createSolution();
}
