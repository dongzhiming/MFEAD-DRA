package culver.multiobjective.multitask.paradigm;

import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMDTLZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static culver.base.Utils.readShiftValuesFromFile;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-18 09:27
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class PILS extends AbstractMultiTaskDoubleProblem {
    public PILS() throws IOException {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMDTLZ("PILS1", 2, 50, -50, 50, 1, "griewank"));
        taskList.add(new MMDTLZ("PILS2", 2, 50, -100, 100, 1, "ackley"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("PILS");

        double[] shiftValues = readShiftValuesFromFile("/momfo/SVData/S_PILS_2.txt");
        ((MMDTLZ) getTask(1)).setShiftValues(shiftValues);
    }
}
