package culver.base;

import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-15 15:12
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class MMZDT extends MO {
    private String f1Type;
    private int k;

    public MMZDT(String name, int numberOfVariables, double lg, double ug, int k, String gType, String f1Type, String hType) {
        super(name, 2, numberOfVariables);

        this.k = k;
        this.f1Type = f1Type;

        setgType(gType);
        sethType(hType);

        double[] shiftValues = new double[numberOfVariables - k];
        for (int i = 0; i < shiftValues.length; i++) {
            shiftValues[i] = 0;
        }

        double[][] rotationMatrix = new double[numberOfVariables - k][numberOfVariables - k];
        for (int i = 0; i < rotationMatrix.length; i++) {
            for (int j = 0; j < rotationMatrix.length; j++) {
                if (i != j) {
                    rotationMatrix[i][j] = 0;
                } else {
                    rotationMatrix[i][j] = 1;
                }
            }
        }

        setShiftValues(shiftValues);
        setRotationMatrix(rotationMatrix);

        //
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < k; i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        for (int i = k; i < numberOfVariables; i++) {
            lowerLimit.add(lg);
            upperLimit.add(ug);
        }

        setVariableBounds(lowerLimit, upperLimit);
    }

    @Override
    public void evaluate(DoubleSolution solution) {
        double[] xI = new double[k];
        double[] xII = new double[getNumberOfVariables() - k];

        for (int i = 0; i < k; i++) {
            xI[i] = solution.getVariable(i);
        }

        for (int i = k; i < getNumberOfVariables(); i++) {
            xII[i - k] = solution.getVariable(i);
        }

        xII = transformVariables(xII);

        double f1 = evalF1(xI);
        double g = evalG(xII) + 1;
        double f2 = g * evalH(f1, g);

        solution.setObjective(0, f1);
        solution.setObjective(1, f2);
    }

    private double evalH(double f1, double g) {
        if (gethType().equalsIgnoreCase("convex"))
            return H_convex(f1, g);
        else if (gethType().equalsIgnoreCase("concave"))
            return H_nonconvex(f1, g);
        else {
            JMetalLogger.logger.info("Error: h function type " + gethType() + " invalid");
            return Double.NaN;
        }
    }

    private double evalF1(double[] xI) {
        if (f1Type.equalsIgnoreCase("linear"))
            return F1_linear(xI);
        else if (f1Type.equalsIgnoreCase("nonlinear"))
            return F1_nonlinear(xI);
        else {
            JMetalLogger.logger.info("Error: f1 function type " + f1Type + " invalid");
            return Double.NaN;
        }
    }

    private double H_convex(double f1, double g) {
        return 1 - Math.pow(f1 / g, 0.5);
    }

    private double H_nonconvex(double f1, double g) {
        return 1 - Math.pow(f1 / g, 2);
    }

    private double F1_linear(double xI[]) {
        double sum = 0;
        for (int i = 0; i < xI.length; i++)
            sum += xI[i];

        return sum / xI.length;
    }

    private double F1_nonlinear(double xI[]) {
        double r = 0;

        for (int i = 0; i < xI.length; i++)
            r += (xI[i] * xI[i]);

        r = Math.sqrt(r);

        return 1 - Math.exp(-4 * r) * Math.pow(Math.sin(5 * Math.PI * r), 4);
    }
}
