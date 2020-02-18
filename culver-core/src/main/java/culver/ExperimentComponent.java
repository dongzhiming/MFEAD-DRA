package culver;

import java.io.IOException;

/**
 * An experiment is composed of instances of this interface.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface ExperimentComponent {
  void run() throws IOException;
}
