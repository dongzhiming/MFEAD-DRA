package culver.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.crossover.impl.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import culver.MFEAD.MFEADDRA;
import culver.multiobjective.multitask.paradigm.*;
import culver.problem.MultiTaskProblem;
import culver.solution.multitaskdoublesolution.MultiTaskDoubleSolution;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static culver.base.Utils.getParetoFront;

/**
 * @Author: Zhi-Ming Dong, dongzm@stumail.neu.edu.com
 * @Date: created in 19-1-19 11:33
 * @Descriptiom: #
 */
public class MFEADRunner extends AbstractAlgorithmRunner {

    /**
     * @param args Command line arguments.
     * @throws SecurityException Invoking command: java
     *                           org.uma.jmetal.runner.multiobjective.MOEADRunner problemName
     *                           [referenceFront]
     */
    public static void main(String[] args) throws IOException {
        final int TIMES = 5;
        List<MultiTaskProblem<MultiTaskDoubleSolution>> multiTaskProblemList = Arrays.asList(new CIHS(), new CIMS(), new CILS(), new PIHS(), new PIMS(), new PILS(), new NIHS(), new NIMS(), new NILS());

        DecimalFormat form = new DecimalFormat("#.####E0");
        long tim[] = new long[multiTaskProblemList.size()];

        for (int index = 0; index < multiTaskProblemList.size(); index++) {
            MultiTaskProblem<MultiTaskDoubleSolution> multiTasksProblem = multiTaskProblemList.get(index);

            System.out.println("===========================" + multiTasksProblem.getName() + "=========================");

            CrossoverOperator crossover = new DifferentialEvolutionCrossover(0.9, 0.5, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);
            MutationOperator mutation = new PolynomialMutation(1.0 / multiTasksProblem.getNumberOfVariables(), 20.0);

            double ave[] = new double[multiTasksProblem.getNumberOfTasks()];

            for (int run = 0; run < TIMES; run++) {

                Algorithm algorithm = new MFEADDRA<MultiTaskDoubleSolution>(
                        multiTasksProblem,
                        105 * multiTasksProblem.getNumberOfTasks(),
                        105 * multiTasksProblem.getNumberOfTasks() * 1000,
                        crossover,
                        mutation,
                        AbstractMOEAD.FunctionType.TCHE,
                        0.8,
                        2,
                        10,
                        0.1);

                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();

                List<MultiTaskDoubleSolution> population = (List<MultiTaskDoubleSolution>) algorithm.getResult();

                long computingTime = algorithmRunner.getComputingTime();
//                JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

                tim[index] += computingTime;

                List<List<DoubleSolution>> solutionList = new ArrayList<>(multiTasksProblem.getNumberOfTasks());
                for (int i = 0; i < multiTasksProblem.getNumberOfTasks(); i++) {
                    solutionList.add(new ArrayList<>());
                }

                for (int i = 0; i < population.size(); i++) {
                    int skillFactor = population.get(i).getSkillFactor();
                    solutionList.get(skillFactor).add((DoubleSolution) population.get(i).getSolution(skillFactor));
                }

                System.out.print(run + "\t");

                for (int i = 0; i < multiTasksProblem.getNumberOfTasks(); i++) {
                    double igd = (new InvertedGenerationalDistance(getParetoFront(multiTasksProblem.getTask(i)))).evaluate(solutionList.get(i));
                    System.out.print(multiTasksProblem.getTask(i).getName() + " = " + form.format(igd) + "\t");
                    ave[i] += igd;
                }
                System.out.println("\tTime --> " + computingTime);
            }

            System.out.println();
            System.out.println("Average Time ==> " + tim[index] / TIMES);
            System.out.println();
            for (int i = 0; i < multiTasksProblem.getNumberOfTasks(); i++) {
                    System.out.println("Average IGD for " + multiTasksProblem.getTask(i).getName() + ": " + form.format(ave[i] / TIMES));
            }

            System.out.println("");
        }
    }
}
