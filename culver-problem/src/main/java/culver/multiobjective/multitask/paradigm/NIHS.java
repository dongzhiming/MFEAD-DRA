package culver.multiobjective.multitask.paradigm;

import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMDTLZ;
import culver.base.MMZDT;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-18 09:26
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class NIHS extends AbstractMultiTaskDoubleProblem {
    public NIHS() {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMDTLZ("NIHS1", 2, 50, -80, 80, 1, "rosenbrock"));
        taskList.add(new MMZDT("NIHS2", 50, -80, 80, 1, "sphere", "linear", "convex"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("NIHS");
    }
}
