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

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-18 09:26
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class NIMS extends AbstractMultiTaskDoubleProblem {
    public NIMS() throws IOException {
        List<Problem<DoubleSolution>> taskList = new ArrayList<>(2);
        taskList.add(new MMDTLZ("NIMS1", 3, 20, -20, 20, 1, "rosenbrock"));
        taskList.add(new MMZDT("NIMS2", 20, -20, 20, 2, "sphere", "linear", "concave"));

        setTaskList(taskList);
        initNumberOfVariables();

        setName("NIMS");

        double[][] matrix = readMatrixFromFile("/momfo/MData/M_NIMS_2.txt");

        ((MMZDT) getTask(1)).setRotationMatrix(matrix);
    }
}
