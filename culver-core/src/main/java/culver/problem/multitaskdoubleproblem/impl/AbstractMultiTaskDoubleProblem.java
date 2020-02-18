package culver.problem.multitaskdoubleproblem.impl;

import culver.problem.AbstractMultiTaskProblem;
import culver.problem.multitaskdoubleproblem.MultiTaskDoubleProblem;
import culver.solution.multitaskdoublesolution.MultiTaskDoubleSolution;
import culver.solution.multitaskdoublesolution.impl.DefaultMultiTaskDoubleSolution;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-3-26 上午9:20
 * @Descriptiom: #
 */
public abstract class AbstractMultiTaskDoubleProblem
        extends AbstractMultiTaskProblem<DoubleSolution, MultiTaskDoubleSolution>
        implements MultiTaskDoubleProblem {

    @Override
    public void evaluate(MultiTaskDoubleSolution multiTaskSolution) {
        // TODO: 2020/2/7 直接通过SkillFactor来获得不行吗？
        DoubleSolution solution = transform(multiTaskSolution);

        int skillFactor = multiTaskSolution.getSkillFactor();
        getTaskList().get(skillFactor).evaluate(solution);

        resetting(multiTaskSolution);

        multiTaskSolution.setSolution(skillFactor, solution);
    }

    @Override
    public MultiTaskDoubleSolution createSolution() {
        return new DefaultMultiTaskDoubleSolution(getNumberOfVariables(), getNumberOfTasks());
    }

    private DoubleSolution transform(MultiTaskDoubleSolution multiTaskSolution) {
        int skillFactor = multiTaskSolution.getSkillFactor();
        DoubleProblem problem = (DoubleProblem) getTask(skillFactor);
        DoubleSolution solution = new DefaultDoubleSolution(problem.getBounds(),
                problem.getNumberOfObjectives(), problem.getNumberOfConstraints());

        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            solution.setVariable(i, multiTaskSolution.getVariable(i) *
                    (problem.getUpperBound(i) - problem.getLowerBound(i)) + problem.getLowerBound(i));
        }

        return solution;
    }

    @Override
    public Double getLowerBound(int index) {
        return 0.0;
    }

    @Override
    public Double getUpperBound(int index) {
        return 1.0;
    }
}
