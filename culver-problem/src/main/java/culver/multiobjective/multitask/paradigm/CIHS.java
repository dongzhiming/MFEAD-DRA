package culver.multiobjective.multitask.paradigm;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMDTLZ;
import culver.base.MMZDT;
import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-14 21:50
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class CIHS extends AbstractMultiTaskDoubleProblem {
    public CIHS() {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMDTLZ("CIHS1", 2, 50, -100, 100, 1, "sphere"));
        taskList.add(new MMZDT("CIHS2", 50, -100, 100, 1, "mean", "linear", "concave"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("CIHS");
    }
}
