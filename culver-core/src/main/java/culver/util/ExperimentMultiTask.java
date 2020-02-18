package culver.util;

import culver.problem.AbstractMultiTaskProblem;
import culver.solution.multitaskdoublesolution.impl.DefaultMultiTaskDoubleSolution;

/**
 * Class used to add a tag field to a multiTask.
 *
 * @author Antonio J. Nebro <ajnebro@uma.es>
 */
public class ExperimentMultiTask<S extends DefaultMultiTaskDoubleSolution> {
  private AbstractMultiTaskProblem multiTask;
  private String tag ;
  private String referenceFront;

  public ExperimentMultiTask(AbstractMultiTaskProblem multiTask, String tag) {
    this.multiTask = multiTask;
    this.tag = tag;
    this.referenceFront = this.multiTask.getName() + ".pf";

  }

  public ExperimentMultiTask(AbstractMultiTaskProblem multiTask) {
    this(multiTask,multiTask.getName());
  }

  public ExperimentMultiTask<S> changeReferenceFrontTo(String referenceFront) {
    this.referenceFront = referenceFront;
    return this;
  }

  public AbstractMultiTaskProblem getMultiTask() {
    return multiTask;
  }

  public String getTag() {
    return tag ;
  }

  public String getReferenceFront() {return referenceFront;}
}
