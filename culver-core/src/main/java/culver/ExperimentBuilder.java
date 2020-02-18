package culver;

import culver.util.ExperimentAlgorithm;
import culver.util.ExperimentMultiTask;
import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import culver.solution.multitaskdoublesolution.impl.DefaultMultiTaskDoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for class {@link Experiment}
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentBuilder<S extends DefaultMultiTaskDoubleSolution, Result> {
  private final String experimentName ;
  private List<ExperimentAlgorithm<S, Result>> algorithmList;
  private List<ExperimentMultiTask<S>> multiTaskList;
  private String referenceFrontDirectory;
  private String experimentBaseDirectory;
  private String outputParetoFrontFileName;
  private String outputParetoSetFileName;
  private int independentRuns;

  private List<GenericIndicator<S>> indicatorList ;

  private int numberOfCores ;

  public ExperimentBuilder(String experimentName) {
    this.experimentName = experimentName ;
    this.independentRuns = 1 ;
    this.numberOfCores = 1 ;
    this.referenceFrontDirectory = null ;
  }

  public ExperimentBuilder<S, Result> setAlgorithmList(List<ExperimentAlgorithm<S, Result>> algorithmList) {
    this.algorithmList = new ArrayList<>(algorithmList) ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setMultiTaskList(List<ExperimentMultiTask<S>> multiTaskList) {
    this.multiTaskList = multiTaskList;

    return this;
  }

  public ExperimentBuilder<S, Result> setExperimentBaseDirectory(String experimentBaseDirectory) {
    this.experimentBaseDirectory = experimentBaseDirectory+"/"+experimentName ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setReferenceFrontDirectory(String referenceFrontDirectory) {
    this.referenceFrontDirectory = referenceFrontDirectory ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setIndicatorList(
          List<GenericIndicator<S>> indicatorList ) {
    this.indicatorList = indicatorList ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setOutputParetoFrontFileName(String outputParetoFrontFileName) {
    this.outputParetoFrontFileName = outputParetoFrontFileName ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setOutputParetoSetFileName(String outputParetoSetFileName) {
    this.outputParetoSetFileName = outputParetoSetFileName ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setIndependentRuns(int independentRuns) {
    this.independentRuns = independentRuns ;

    return this ;
  }

  public ExperimentBuilder<S, Result> setNumberOfCores(int numberOfCores) {
    this.numberOfCores = numberOfCores;

    return this ;
  }

  public Experiment<S, Result> build() {
    return new Experiment<S, Result>(this);
  }

  /* Getters */
  public String getExperimentName() {
    return experimentName;
  }

  public List<ExperimentAlgorithm<S, Result>> getAlgorithmList() {
    return algorithmList;
  }

  public List<ExperimentMultiTask<S>> getMultiTaskList() {
    return multiTaskList;
  }

  public String getExperimentBaseDirectory() {
    return experimentBaseDirectory;
  }

  public String getOutputParetoFrontFileName() {
    return outputParetoFrontFileName;
  }

  public String getOutputParetoSetFileName() {
    return outputParetoSetFileName;
  }

  public int getIndependentRuns() {
    return independentRuns;
  }

  public int getNumberOfCores() {
    return numberOfCores;
  }

  public String getReferenceFrontDirectory() {
    return referenceFrontDirectory;
  }

  public List<GenericIndicator<S>> getIndicatorList() {
    return indicatorList;
  }
}
