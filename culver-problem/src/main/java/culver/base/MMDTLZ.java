package culver.base;

import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-1-15 15:11
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class MMDTLZ extends MO {

    private int alpha;

    public MMDTLZ(String name, int numberOfObjectives, int numberOfVariables, double lg, double ug, int alpha, String gType) {
        super(name, numberOfObjectives, numberOfVariables);

        this.alpha = alpha;

        //
        int num = numberOfVariables - numberOfObjectives + 1;

        double[] shiftValues = new double[num];
        double[][] rotationMatrix = new double[num][num];

        for (int i = 0; i < num; i++) {
            shiftValues[i] = 0;
        }


        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if (i != j) {
                    rotationMatrix[i][j] = 0;
                } else {
                    rotationMatrix[i][j] = 1;
                }
            }
        }

        setShiftValues(shiftValues);
        setRotationMatrix(rotationMatrix);

        setgType(gType);

        if (numberOfObjectives == 2) {
            sethType("circle");
        } else {
            sethType("sphere");
        }

        //
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < numberOfObjectives - 1; i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        for (int i = numberOfObjectives - 1; i < numberOfVariables; i++) {
            lowerLimit.add(lg);
            upperLimit.add(ug);
        }

        setVariableBounds(lowerLimit, upperLimit);
    }

    @Override
    public void evaluate(DoubleSolution solution) {

        double[] xI = new double[getNumberOfObjectives() - 1];
        double[] xII = new double[getNumberOfVariables() - getNumberOfObjectives() + 1];

        for (int i = 0; i < getNumberOfObjectives() - 1; i++) {
            xI[i] = solution.getVariable(i);
        }

        for (int i = getNumberOfObjectives() - 1; i < getNumberOfVariables(); i++) {
            xII[i - getNumberOfObjectives() + 1] = solution.getVariable(i);
        }

        xII = transformVariables(xII);

        double[] f = new double[getNumberOfObjectives()];

        double g = evalG(xII);

        for (int i = 0; i < getNumberOfObjectives(); i++) {
            f[i] = 1 + g;
        }

//        solution.setGFunValue(1 + g);

        for (int i = 0; i < getNumberOfObjectives(); i++) {
            for (int j = 0; j < getNumberOfObjectives() - (i + 1); j++) {
                f[i] *= Math.cos(Math.pow(xI[j], alpha) * 0.5 * Math.PI);
            }
            if (i != 0) {
                int aux = getNumberOfObjectives() - (i + 1);
                f[i] *= Math.sin(Math.pow(xI[aux], alpha) * 0.5 * Math.PI);
            } // if
        } // for

        //
        for (int i = 0; i < getNumberOfObjectives(); i++) {
            solution.setObjective(i, f[i]);
        }
    }
}
