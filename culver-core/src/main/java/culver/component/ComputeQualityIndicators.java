package culver.component;

/**
 * This class computes the {@link QualityIndicator}s of an experiment. Once the algorithms of an
 * experiment have been executed through running an instance of class {@link ExecuteAlgorithms},
 * the list of indicators in obtained from the {@link ExperimentComponent #getIndicatorsList()} method.
 * Then, for every combination algorithm + problem, the indicators are applied to all the FUN files and
 * the resulting values are store in a file called as {@link QualityIndicator #getName()}, which is located
 * in the same directory of the FUN files.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
//public class ComputeQualityIndicators<S extends culver.solution.multitaskdoublesolution.impl.DefaultMultiTaskDoubleSolution, Result> implements ExperimentComponent {
//
//    private final Experiment<S, Result> experiment;
//
//    public ComputeQualityIndicators(Experiment<S, Result> experiment) {
//        this.experiment = experiment;
//    }
//
//    @Override
//    public void run() throws IOException {
//        /**
//         * @Description: Turner added
//         */
//        for (GenericIndicator<S> indicator : experiment.getIndicatorList()) {
//            for (int i = 0; i < experiment.getAlgorithmList().size() / experiment.getIndependentRuns(); i++) {
//                ExperimentAlgorithm<?, Result> algorithm = experiment.getAlgorithmList().get(i);
//                String algorithmDirectory = experiment.getExperimentBaseDirectory() + "/data/" + algorithm.getAlgorithmTag();
//                String problemDirectory = algorithmDirectory + "/" + algorithm.getProblemTag();
//                String qualityIndicatorFile = problemDirectory + "/" + indicator.getName();
//                resetFile(qualityIndicatorFile);
//            }
//        }
//
//        for (GenericIndicator<S> indicator : experiment.getIndicatorList()) {
//            JMetalLogger.logger.info("Computing indicator: " + indicator.getName());
//
//            for (ExperimentAlgorithm<?, Result> algorithm : experiment.getAlgorithmList()) {
//                String algorithmDirectory;
//                algorithmDirectory = experiment.getExperimentBaseDirectory() + "/data/" + algorithm.getAlgorithmTag();
//                String problemDirectory = algorithmDirectory + "/" + algorithm.getProblemTag();
//
//                String referenceFrontDirectory = experiment.getReferenceFrontDirectory();
//
//
//                String referenceFrontName = referenceFrontDirectory + "/" + algorithm.getReferenceParetoFront();
//
//                JMetalLogger.logger.info("RF: " + referenceFrontName);
//                ;
//                Front referenceFront = new ArrayFront(referenceFrontName);
//
//                FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
//                Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
//
//                String qualityIndicatorFile = problemDirectory + "/" + indicator.getName();
//
//
//                indicator.setReferenceParetoFront(normalizedReferenceFront);
//                String frontFileName = problemDirectory + "/" +
//                        experiment.getOutputParetoFrontFileName() + algorithm.getRunId() + ".tsv";
//
//                Front front = new ArrayFront(frontFileName);
//                Front normalizedFront = frontNormalizer.normalize(front);
//                List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
//                Ranking<PointSolution> ranking = new DominanceRanking<>(new DominanceComparator<>());
//                ranking.computeRanking(normalizedPopulation);
//
//                Double indicatorValue = (Double) indicator.evaluate((List<S>) ranking.computeRanking(normalizedPopulation).getSubfront(0));
//                JMetalLogger.logger.info(indicator.getName() + ": " + indicatorValue);
//
//                writeQualityIndicatorValueToFile(indicatorValue, qualityIndicatorFile);
//            }
//        }
//        findBestIndicatorFronts(experiment);
//    }
//
//    private void writeQualityIndicatorValueToFile(Double indicatorValue, String qualityIndicatorFile) {
//        FileWriter os;
//        try {
//            os = new FileWriter(qualityIndicatorFile, true);
//            os.write("" + indicatorValue + "\n");
//            os.close();
//        } catch (IOException ex) {
//            throw new JMetalException("Error writing indicator file" + ex);
//        }
//    }
//
//    /**
//     * Deletes a file or directory if it does exist
//     *
//     * @param file
//     */
//    private void resetFile(String file) {
//        File f = new File(file);
//        if (f.exists()) {
//            JMetalLogger.logger.info("File " + file + " exist.");
//
//            if (f.isDirectory()) {
//                JMetalLogger.logger.info("File " + file + " is a directory. Deleting directory.");
//                if (f.delete()) {
//                    JMetalLogger.logger.info("Directory successfully deleted.");
//                } else {
//                    JMetalLogger.logger.info("Error deleting directory.");
//                }
//            } else {
//                JMetalLogger.logger.info("File " + file + " is a file. Deleting file.");
//                if (f.delete()) {
//                    JMetalLogger.logger.info("File succesfully deleted.");
//                } else {
//                    JMetalLogger.logger.info("Error deleting file.");
//                }
//            }
//        } else {
//            JMetalLogger.logger.info("File " + file + " does NOT exist.");
//        }
//    }
//
//    public void findBestIndicatorFronts(Experiment<?, Result> experiment) throws IOException {
//        for (GenericIndicator<?> indicator : experiment.getIndicatorList()) {
//            for (ExperimentAlgorithm<?, Result> algorithm : experiment.getAlgorithmList()) {
//                String algorithmDirectory;
//                algorithmDirectory = experiment.getExperimentBaseDirectory() + "/data/" +
//                        algorithm.getAlgorithmTag();
//
//                for (ExperimentMultiTask<?> problem : experiment.getMultiTaskList()) {
//                    String indicatorFileName =
//                            algorithmDirectory + "/" + problem.getTag() + "/" + indicator.getName();
//                    Path indicatorFile = Paths.get(indicatorFileName);
//                    if (indicatorFile == null) {
//                        throw new JMetalException("Indicator file " + indicator.getName() + " doesn't exist");
//                    }
//
//                    List<String> fileArray;
//                    fileArray = Files.readAllLines(indicatorFile, StandardCharsets.UTF_8);
//
//                    List<Pair<Double, Integer>> list = new ArrayList<>();
//
//
//                    for (int i = 0; i < fileArray.size(); i++) {
//                        Pair<Double, Integer> pair = new ImmutablePair<>(Double.parseDouble(fileArray.get(i)), i);
//                        list.add(pair);
//                    }
//
//                    Collections.sort(list, new Comparator<Pair<Double, Integer>>() {
//                        @Override
//                        public int compare(Pair<Double, Integer> pair1, Pair<Double, Integer> pair2) {
//                            if (Math.abs(pair1.getLeft()) > Math.abs(pair2.getLeft())) {
//                                return 1;
//                            } else if (Math.abs(pair1.getLeft()) < Math.abs(pair2.getLeft())) {
//                                return -1;
//                            } else {
//                                return 0;
//                            }
//                        }
//                    });
//                    String bestFunFileName;
//                    String bestVarFileName;
//                    String medianFunFileName;
//                    String medianVarFileName;
//
//                    String outputDirectory = algorithmDirectory + "/" + problem.getTag();
//
//                    bestFunFileName = outputDirectory + "/BEST_" + indicator.getName() + "_FUN.tsv";
//                    bestVarFileName = outputDirectory + "/BEST_" + indicator.getName() + "_VAR.tsv";
//                    medianFunFileName = outputDirectory + "/MEDIAN_" + indicator.getName() + "_FUN.tsv";
//                    medianVarFileName = outputDirectory + "/MEDIAN_" + indicator.getName() + "_VAR.tsv";
//                    if (indicator.isTheLowerTheIndicatorValueTheBetter()) {
//                        String bestFunFile = outputDirectory + "/" +
//                                experiment.getOutputParetoFrontFileName() + list.get(0).getRight() + ".tsv";
//                        String bestVarFile = outputDirectory + "/" +
//                                experiment.getOutputParetoSetFileName() + list.get(0).getRight() + ".tsv";
//
//                        Files.copy(Paths.get(bestFunFile), Paths.get(bestFunFileName), REPLACE_EXISTING);
//                        Files.copy(Paths.get(bestVarFile), Paths.get(bestVarFileName), REPLACE_EXISTING);
//                    } else {
//                        String bestFunFile = outputDirectory + "/" +
//                                experiment.getOutputParetoFrontFileName() + list.get(list.size() - 1).getRight() + ".tsv";
//                        String bestVarFile = outputDirectory + "/" +
//                                experiment.getOutputParetoSetFileName() + list.get(list.size() - 1).getRight() + ".tsv";
//
//                        Files.copy(Paths.get(bestFunFile), Paths.get(bestFunFileName), REPLACE_EXISTING);
//                        Files.copy(Paths.get(bestVarFile), Paths.get(bestVarFileName), REPLACE_EXISTING);
//                    }
//
//                    int medianIndex = list.size() / 2;
//                    String medianFunFile = outputDirectory + "/" +
//                            experiment.getOutputParetoFrontFileName() + list.get(medianIndex).getRight() + ".tsv";
//                    String medianVarFile = outputDirectory + "/" +
//                            experiment.getOutputParetoSetFileName() + list.get(medianIndex).getRight() + ".tsv";
//
//                    Files.copy(Paths.get(medianFunFile), Paths.get(medianFunFileName), REPLACE_EXISTING);
//                    Files.copy(Paths.get(medianVarFile), Paths.get(medianVarFileName), REPLACE_EXISTING);
//                }
//            }
//        }
//    }
//}

