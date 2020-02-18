package culver.MFEAD;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.impl.IdealPoint;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import culver.problem.MultiTaskProblem;
import culver.solution.MultiTaskSolution;

import java.util.ArrayList;
import java.util.List;

import static culver.UniformPoint.generateWeightVector;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-1-19 15:12
 * @Descriptiom: #
 */
public abstract class AbstractMFEAD<S extends MultiTaskSolution<?, ? extends Solution<?>>> implements Algorithm<List<S>> {
    protected enum NeighborType {NEIGHBOR, POPULATION}

    protected MultiTaskProblem<S> multiTaskProblem;
    protected List<S> population;
    protected int populationSize;

    protected int evaluations;
    protected int maxEvaluations;

    protected JMetalRandom randomGenerator;

    protected CrossoverOperator<S> crossoverOperator;
    protected MutationOperator<S> mutationOperator;
    protected AbstractMOEAD.FunctionType functionType;
    protected double beta;
    protected int nr;
    protected int T;
    protected double rmp;

    protected int subsize;

    protected NeighborType neighborType;

    protected List<IdealPoint> idealPoints;
    protected double[][][] lambda;
    protected int[][][] neighborhood;

    public AbstractMFEAD(MultiTaskProblem<S> multiTaskProblem,
                         int populationSize,
                         int maxEvaluations,
                         CrossoverOperator<S> crossover,
                         MutationOperator<S> mutation,
                         AbstractMOEAD.FunctionType functionType,
                         double neighborhoodSelectionProbability,
                         int maximumNumberOfReplacedSolutions,
                         int neighborSize,
                         double rmp) {
        this.multiTaskProblem = multiTaskProblem;
        this.populationSize = populationSize;
        this.maxEvaluations = maxEvaluations;
        this.crossoverOperator = crossover;
        this.mutationOperator = mutation;
        this.functionType = functionType;
        this.beta = neighborhoodSelectionProbability;
        this.nr = maximumNumberOfReplacedSolutions;
        this.T = neighborSize;
        this.rmp = rmp;

        this.subsize = populationSize / multiTaskProblem.getNumberOfTasks();

        this.randomGenerator = JMetalRandom.getInstance();
    }

    protected void initNeighborhood() {
        neighborhood = new int[multiTaskProblem.getNumberOfTasks()][subsize][T];

        for (int i = 0; i < multiTaskProblem.getNumberOfTasks(); i++) {
            double[] x = new double[subsize];
            int[] idx = new int[subsize];

            for (int j = 0; j < subsize; j++) {
                for (int k = 0; k < subsize; k++) {
                    x[k] = MOEADUtils.distVector(lambda[i][j], lambda[i][k]);
                    idx[k] = subsize * i + k;
                }

                MOEADUtils.minFastSort(x, idx, subsize, T);

                System.arraycopy(idx, 0, neighborhood[i][j], 0, T);
            }
        }
    }

    protected void initWeights() {
        // Step 1
        this.lambda = new double[multiTaskProblem.getNumberOfTasks()][][];
        for (int i = 0; i < multiTaskProblem.getNumberOfTasks(); i++) {
            this.lambda[i] = new double[subsize][multiTaskProblem.getTask(i).getNumberOfObjectives()];
        }

        // Step 2
        for (int i = 0; i < multiTaskProblem.getNumberOfTasks(); i++) {
            lambda[i] = generateWeightVector(subsize, multiTaskProblem.getTask(i).getNumberOfObjectives());
        }
    }

    protected void initIdealPoint() {
        idealPoints = new ArrayList<>(multiTaskProblem.getNumberOfTasks());
        for (int i = 0; i < multiTaskProblem.getNumberOfTasks(); i++) {
            idealPoints.add(new IdealPoint(multiTaskProblem.getTask(i).getNumberOfObjectives()));
        }

        for (int i = 0; i < populationSize; i++) {
            int id = population.get(i).getSkillFactor();
            idealPoints.get(id).update(population.get(i).getSolution(id).getObjectives());
        }
    }

    protected void updateIdealPoint(S child, int id) {
        idealPoints.get(id).update(child.getSolution(id).getObjectives());
    }

    protected void chooseNeighborType() {
        double rnd = randomGenerator.nextDouble();
        if (rnd < this.beta) {
            this.neighborType = NeighborType.NEIGHBOR;
        } else {
            this.neighborType = NeighborType.POPULATION;
        }
    }

    protected double fitnessFunction(int id, S individual, double[] lambda) {
        double maxFun = -1.0e+30;

        for (int n = 0; n < individual.getSolution(id).getNumberOfObjectives(); n++) {
            double diff = Math.abs(individual.getSolution(id).getObjective(n) - idealPoints.get(id).getValue(n));

            double feval;
            if (lambda[n] == 0) {
                feval = diff / 0.000001;
            } else {
                feval = diff / lambda[n];
            }
            if (feval > maxFun) {
                maxFun = feval;
            }
        }

        return maxFun;
    }

    protected void updateNeighborhood(S child, int id, int subproblem) {
        int size;
        int time = 0;

        if (neighborType == NeighborType.NEIGHBOR) {
            size = neighborhood[id][subproblem].length;
        } else {
            size = subsize;
        }
        int[] perm = new int[size];

        MOEADUtils.randomPermutation(perm, size);

        for (int i = 0; i < size; i++) {
            int k;
            if (neighborType == NeighborType.NEIGHBOR) {
                k = neighborhood[id][subproblem][perm[i]];
            } else {
                k = id * subsize + perm[i];
            }
            double f1, f2;

            f1 = fitnessFunction(id, population.get(k), lambda[id][k % subsize]);
            f2 = fitnessFunction(id, child, lambda[id][k % subsize]);

            if (f2 < f1) {
                population.set(k, (S) child.copy());
                time++;
            }

            if (time >= nr) {
                return;
            }
        }
    }

    protected List<S> parentSelection(int skillFactor, int subproblem) {
        List<Integer> matingPool = matingSelection(skillFactor, subproblem, 2);

        List<S> parents = new ArrayList<>(3);

        parents.add(population.get(matingPool.get(0)));
        parents.add(population.get(matingPool.get(1)));
        parents.add(population.get(skillFactor * subsize + subproblem));

        return parents;
    }

    protected List<Integer> matingSelection(int skillFactor, int subproblem, int numberOfSolutionsToSelect) {
        int neighbourSize;
        int selectedSolution;

        List<Integer> listOfSolutions = new ArrayList<>(numberOfSolutionsToSelect);

        neighbourSize = neighborhood[skillFactor][subproblem].length;
        while (listOfSolutions.size() < numberOfSolutionsToSelect) {
            int random;
            if (neighborType == NeighborType.NEIGHBOR) {
                random = randomGenerator.nextInt(0, neighbourSize - 1);
                selectedSolution = neighborhood[skillFactor][subproblem][random];
            } else {
                selectedSolution = subsize * skillFactor + randomGenerator.nextInt(0, subsize - 1);
            }
            boolean flag = true;

            if (skillFactor * subsize + subproblem == selectedSolution) {
                flag = false;
            }

            for (Integer individualId : listOfSolutions) {
                if (individualId == selectedSolution) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                listOfSolutions.add(selectedSolution);
            }
        }

        return listOfSolutions;
    }

    @Override
    public List<S> getResult() {
        return population;
    }
}
