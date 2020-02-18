package culver.util;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import culver.solution.multitaskdoublesolution.impl.DefaultMultiTaskDoubleSolution;
import culver.Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class defining tasks for the execution of algorithms in parallel.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentAlgorithm<S extends DefaultMultiTaskDoubleSolution, Result> {
    private Algorithm<Result> algorithm;
    private String algorithmTag;
    private String problemTag;
    private String referenceParetoFront;
    private int runId;

    /**
     * Constructor
     */
    public ExperimentAlgorithm(
            Algorithm<Result> algorithm,
            String algorithmTag,
            ExperimentMultiTask multiTask,
            int runId) {
        this.algorithm = algorithm;
        this.algorithmTag = algorithmTag;
        this.problemTag = multiTask.getTag();
        this.referenceParetoFront = multiTask.getReferenceFront();
        this.runId = runId;
    }

    public ExperimentAlgorithm(
            Algorithm<Result> algorithm,
            ExperimentMultiTask problem,
            int runId) {

        this(algorithm, algorithm.getName(), problem, runId);

    }

    public void runAlgorithm(Experiment<?, ?> experimentData) {
        int numOfTasks = 2;
        String outputDirectoryName[] = new String[numOfTasks];
        String funFile[] = new String[numOfTasks];
        String varFile[] = new String[numOfTasks];

        for (int i = 0; i < numOfTasks; i++) {
            outputDirectoryName[i] = experimentData.getExperimentBaseDirectory()
                    + "/data/"
                    + algorithmTag
                    + "/"
                    + problemTag + (i + 1);


            File outputDirectory = new File(outputDirectoryName[i]);
            if (!outputDirectory.exists()) {
                boolean result = new File(outputDirectoryName[i]).mkdirs();
                if (result) {
                    JMetalLogger.logger.info("Creating " + outputDirectoryName[i]);
                } else {
                    JMetalLogger.logger.severe("Creating " + outputDirectoryName[i] + " failed");
                }
            }

            funFile[i] = outputDirectoryName[i] + "/FUN" + runId + ".tsv";
            varFile[i] = outputDirectoryName[i] + "/VAR" + runId + ".tsv";
            JMetalLogger.logger.info(
                    " Running algorithm: " + algorithmTag +
                            ", problem: " + problemTag + (i + 1) +
                            ", run: " + runId +
                            ", funFile: " + funFile[i]);

        }

        algorithm.run();
        Result population = algorithm.getResult();


        List<List<DoubleSolution>> subPolulationList = new ArrayList<>(numOfTasks);
        for (int i = 0; i < numOfTasks; i++) {
            subPolulationList.add(new ArrayList<>());
        }

        for (int i = 0; i < ((List<S>) population).size(); i++) {
            S s = ((List<S>) population).get(i);
            int skillFactor = s.getSkillFactor();
            subPolulationList.get(skillFactor).add((DoubleSolution) s.getSolution(skillFactor));
        }

        for (int i = 0; i < numOfTasks; i++) {
            new SolutionListOutput(subPolulationList.get(i))
                    .setVarFileOutputContext(new DefaultFileOutputContext(varFile[i]))
                    .setFunFileOutputContext(new DefaultFileOutputContext(funFile[i]))
                    .print();
        }
    }

    public Algorithm<Result> getAlgorithm() {
        return algorithm;
    }

    public String getAlgorithmTag() {
        return algorithmTag;
    }

    public String getProblemTag() {
        return problemTag;
    }

    public String getReferenceParetoFront() {
        return referenceParetoFront;
    }

    public int getRunId() {
        return this.runId;
    }
}
