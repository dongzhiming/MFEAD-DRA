package culver.multiobjective.multitask.paradigm;

import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMDTLZ;
import culver.base.MMZDT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static culver.base.Utils.readShiftValuesFromFile;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-18 09:26
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class NILS extends AbstractMultiTaskDoubleProblem {
    public NILS() throws IOException {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMDTLZ("NILS1", 3, 25, -50, 50, 1, "griewank"));
        taskList.add(new MMZDT("NILS2", 50, -100, 100, 2, "ackley", "linear", "concave"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("NILS");

        double[] shiftValues = readShiftValuesFromFile("/momfo/SVData/S_NILS_1.txt");

        ((MMDTLZ) getTask(0)).setShiftValues(shiftValues);
    }
}
