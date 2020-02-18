package culver.multiobjective.multitask.paradigm;

import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMDTLZ;
import culver.base.MMZDT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static culver.base.Utils.readMatrixFromFile;
import static culver.base.Utils.readShiftValuesFromFile;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-18 09:25
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class CIMS extends AbstractMultiTaskDoubleProblem {
    public CIMS() throws IOException {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMZDT("CIMS1", 10, -5, 5, 1, "rosenbrock", "linear", "concave"));
        taskList.add(new MMDTLZ("CIMS2", 2, 10, -5, 5, 1, "mean"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("CIMS");

        double[] shiftValues = readShiftValuesFromFile("/momfo/SVData/S_CIMS_2.txt");
        double[][] matrix = readMatrixFromFile("/momfo/MData/M_CIMS_2.txt");

        ((MMDTLZ) getTask(1)).setShiftValues(shiftValues);
        ((MMDTLZ) getTask(1)).setRotationMatrix(matrix);
    }
}
