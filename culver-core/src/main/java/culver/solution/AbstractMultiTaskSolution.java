package culver.solution;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-3-26 下午9:47
 * @Descriptiom: #
 */
public abstract class AbstractMultiTaskSolution<T, S extends Solution<?>> implements MultiTaskSolution<T, S> {
    public enum Member {SKILL_FACTOR, FACTORIAL_RANK, SCALAR_FITNESS}

    protected int skillFactor;
//    protected int[] factorialRank;
//    protected double scalarFitness;

    protected List<T> variables;
    protected List<S> solutionList;

    protected final JMetalRandom randomGenerator;

    protected Map<Object, Object> attributes;

    protected AbstractMultiTaskSolution(int numberOfVariables, int numberOfTasks) {
//        this.factorialRank = new int[numberOfTasks];

        attributes = new HashMap<>();

        this.randomGenerator = JMetalRandom.getInstance();

        variables = new ArrayList<>(numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            variables.add(null);
        }

        solutionList = new ArrayList<>(numberOfTasks);
        // TODO: 2020/2/7 一开始给初始化一下是不是更好
        for (int i = 0; i < numberOfTasks; i++) {
            solutionList.add(null);
        }
    }

    @Override
    public void setSkillFactor(int skillFactor) {
        this.skillFactor = skillFactor;
    }

    @Override
    public void setFactorialRank(int task, int rank) {
        this.solutionList.get(task).setAttribute(Member.FACTORIAL_RANK, rank);
    }

//    @Override
//    public void setScalarFitness(double scalarFitness) {
//        this.scalarFitness = scalarFitness;
//    }

    @Override
    public void setSolution(int index, S solution) {
        this.solutionList.set(index, solution);
    }

    @Override
    public int getSkillFactor() {
        return skillFactor;
    }

    @Override
    public int getFactorialRank(int task) {
        return (int) getSolution(skillFactor).getAttribute(Member.FACTORIAL_RANK);
    }

    @Override
    public double getScalarFitness() {
        return 1.0 / (getFactorialRank(skillFactor) + 1);
    }

    @Override
    public S getSolution(int index) {
        return solutionList.get(index);
    }

    @Override
    public List<T> getVariables() {
        return variables;
    }

    @Override
    public int getNumberOfVariables() {
        return variables.size();
    }

    @Override
    public void setAttribute(Object id, Object value) {
        attributes.put(id, value);
    }

    @Override
    public Object getAttribute(Object id) {
        return attributes.get(id);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    @Override
    public T getVariable(int index) {
        return variables.get(index);
    }

    @Override
    public void setVariable(int index, T variable) {
        variables.set(index, variable);
    }

    @Override
    public double[] getConstraints() {
        return null;
    }

    @Override
    public double getConstraint(int index) {
        return 0;
    }

    @Override
    public void setConstraint(int index, double value) {
        // do nothing
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public boolean hasAttribute(Object id) {
        return attributes.containsKey(id);
    }
}
