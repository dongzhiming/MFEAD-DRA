package culver.multiobjective.multitask.paradigm;

import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMZDT;

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
public class PIHS extends AbstractMultiTaskDoubleProblem {
    public PIHS() throws IOException {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMZDT("PIHS1", 50, -100, 100, 1, "sphere", "linear", "convex"));
        taskList.add(new MMZDT("PIHS2", 50, -100, 100, 1, "rastrigin", "linear", "convex"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("PIHS");

        double[] shiftValues = readShiftValuesFromFile("/momfo/SVData/S_PIHS_2.txt");
        ((MMZDT) getTask(1)).setShiftValues(shiftValues);
    }
}
