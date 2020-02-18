package culver.multiobjective.multitask.paradigm;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import culver.base.MMDTLZ;
import culver.base.MMZDT;
import culver.problem.multitaskdoubleproblem.impl.AbstractMultiTaskDoubleProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static culver.base.Utils.readMatrixFromFile;
import static culver.base.Utils.readShiftValuesFromFile;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-18 09:26
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class PIMS extends AbstractMultiTaskDoubleProblem {
    public PIMS() throws IOException {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMDTLZ("PIMS1", 2, 50, 0, 1, 1, "sphere"));
        taskList.add(new MMZDT("PIMS2", 50, 0, 1, 1, "rastrigin", "linear", "concave"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("PIMS");

        double[] shiftValues0 = readShiftValuesFromFile("/momfo/SVData/S_PIMS_1.txt");
        double[][] matrix0 = readMatrixFromFile("/momfo/MData/M_PIMS_1.txt");
        double[][] matrix1 = readMatrixFromFile("/momfo/MData/M_PIMS_2.txt");

        ((MMDTLZ) getTask(0)).setShiftValues(shiftValues0);
        ((MMDTLZ) getTask(0)).setRotationMatrix(matrix0);

        ((MMZDT) getTask(1)).setRotationMatrix(matrix1);
    }
}
