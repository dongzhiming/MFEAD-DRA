package culver.base;

import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.util.JMetalLogger;

public abstract class MO extends AbstractDoubleProblem {
    private double[] shiftValues;
    private double[][] rotationMatrix;
    private String gType;
    private String hType;

    public MO(String name, int numberOfObjectives, int numberOfVariables) {
        setName(name);
        setNumberOfConstraints(0);
        setNumberOfObjectives(numberOfObjectives);
        setNumberOfVariables(numberOfVariables);
    }

    protected double evalG(double[] xII) {
        if (gType.equalsIgnoreCase("sphere"))
            return GFunctions.getSphere(xII);
        else if (gType.equalsIgnoreCase("rosenbrock"))
            return GFunctions.getRosenbrock(xII);
        else if (gType.equalsIgnoreCase("ackley"))
            return GFunctions.getAckley(xII);
        else if (gType.equalsIgnoreCase("griewank"))
            return GFunctions.getGriewank(xII);
        else if (gType.equalsIgnoreCase("rastrigin"))
            return GFunctions.getRastrigin(xII);
        else if (gType.equalsIgnoreCase("mean"))
            return GFunctions.getMean(xII);
        else {
            JMetalLogger.logger.info("Error: g function type " + gType + " invalid");
            return Double.NaN;
        }
    }

    protected double[] transformVariables(double x[]) {
        shiftVariables(x);
        return rotateVariables(x);
    }

    private double[] rotateVariables(double[] x) {
        int len = x.length;
        double res[] = new double[len];

        for (int i = 0; i < len; i++) {
            double[] y = rotationMatrix[i];

            double sum = 0;
            for (int j = 0; j < len; j++) {
                sum += x[j] * y[j];
            }
            res[i] = sum;
        }

        return res;
    }

    private void shiftVariables(double[] x) {
        for (int i = 0; i < x.length; i++) {
            x[i] -= shiftValues[i];
        }
    }

    public double[] getShiftValues() {
        return shiftValues;
    }

    public double[][] getRotationMatrix() {
        return rotationMatrix;
    }

    public String getgType() {
        return gType;
    }

    public String gethType() {
        return hType;
    }

    public void setShiftValues(double[] shiftValues) {
        this.shiftValues = shiftValues;
    }

    public void setRotationMatrix(double[][] rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }

    public void sethType(String hType) {
        this.hType = hType;
    }
}
