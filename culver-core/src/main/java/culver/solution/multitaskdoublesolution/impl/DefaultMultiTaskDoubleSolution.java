package culver.solution.multitaskdoublesolution.impl;

import culver.solution.AbstractMultiTaskSolution;
import culver.solution.multitaskdoublesolution.MultiTaskDoubleSolution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-1-14 21:29
 * @Descriptiom: #
 */
@SuppressWarnings("serial")
public class DefaultMultiTaskDoubleSolution extends AbstractMultiTaskSolution<Double, DoubleSolution>
        implements MultiTaskDoubleSolution {

    public DefaultMultiTaskDoubleSolution(int numberOfVariables, int numberOfTasks) {
        super(numberOfVariables, numberOfTasks);

        for (int i = 0; i < numberOfVariables; i++) {
            variables.set(i, randomGenerator.nextDouble());
        }

        this.skillFactor = -1;
    }

    public DefaultMultiTaskDoubleSolution(DefaultMultiTaskDoubleSolution solution) {
        super(solution.getNumberOfVariables(), solution.solutionList.size());

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            this.variables.set(i, solution.getVariable(i));
        }

        if (solution.attributes != null) {
            this.attributes = new HashMap<>(solution.attributes);
        }

        this.skillFactor = solution.getSkillFactor();

//        for (int i = 0; i < factorialRank.length; i++) {
//            this.factorialRank[i] = solution.getFactorialRank(i);
//        }

//        this.scalarFitness = solution.getScalarFitness();

        for (int i = 0; i < solution.solutionList.size(); i++) {
            if (solution.getSolution(i) != null) {
                this.setSolution(i, (DoubleSolution) solution.getSolution(i).copy());
            }
        }
    }

    @Override
    public Double getLowerBound(int index) {
        return 0.0;
    }

    @Override
    public Double getUpperBound(int index) {
        return 1.0;
    }

//    @Override
//    public List<Solution> getSolutionList() {
//        return solutionList;
//    }

    @Override
    public MultiTaskDoubleSolution copy() {
        return new DefaultMultiTaskDoubleSolution(this);
    }

    @Override
    public void setObjective(int i, double v) {
        throw new JMetalException("No 'setObjective' method for MultiTaskDoubleSolution.");
    }

    @Override
    public double getObjective(int i) {
        throw new JMetalException("No 'getObjective' method for MultiTaskDoubleSolution.");
//        return 0;
    }

    @Override
    public double[] getObjectives() {
        throw new JMetalException("No 'getObjectives' method for MultiTaskDoubleSolution.");
//        return new double[0];
    }

    @Override
    public int getNumberOfObjectives() {
        throw new JMetalException("No 'getNumberOfObjectives' method for MultiTaskDoubleSolution.");
//        return 0;
    }
}
