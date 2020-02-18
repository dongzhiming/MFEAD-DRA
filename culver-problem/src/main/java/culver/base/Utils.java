package culver.base;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLCell;
import com.jmatio.types.MLDouble;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
 * @Date: created in 19-3-21 下午7:16
 * @Version: v
 * @Descriptiom: #
 * 1#
 * @Modified by:
 */
public class Utils {

    /**
     * @param rotationMatrixPath
     * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
     * @Date: created in 19-3-21 下午7:20
     * @Descriptiom:
     * @Return:
     */
    public static double[][] readMatrixFromFile(String rotationMatrixPath) throws IOException {
        InputStream in = Utils.class.getResourceAsStream(rotationMatrixPath);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);

        String line = br.readLine();
        List<String> data = new ArrayList<String>();

        while (line != null) {
            data.add(line);
            line = br.readLine();
        }
        double[][] matrix = new double[data.size()][];
        for (int i = 0; i < matrix.length; i++) {
            String[] st = data.get(i).trim().split("\\s+");
            matrix[i] = new double[st.length];
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = Double.parseDouble(st[j]);

        }
        br.close();
        return matrix;
    }

    /**
     * @param shiftValuesPath
     * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
     * @Date: created in 19-3-21 下午7:20
     * @Descriptiom:
     * @Return:
     */
    public static double[] readShiftValuesFromFile(String shiftValuesPath) throws IOException {
        InputStream in = Utils.class.getResourceAsStream(shiftValuesPath);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();
        String[] st = line.trim().split("\\s+");

        double[] shift = new double[st.length];

        for (int i = 0; i < shift.length; i++)
            shift[i] = Double.parseDouble(st[i]);

        br.close();
        return shift;
    }

    /**
     * @param shiftValuesMatPath
     * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
     * @Date: created in 19-3-23 上午10:46
     * @Descriptiom:
     * @Return:
     */
    public static double[][] readShiftValuesFromMatFile(String shiftValuesMatPath) throws IOException {
        String path = Utils.class.getClassLoader().getResource(shiftValuesMatPath).getPath();

        MatFileReader read = new MatFileReader(path);

        String key = (String) read.getContent().keySet().toArray()[0];

        MLArray mlArray = read.getMLArray(key);
        MLDouble d = (MLDouble) mlArray;
        double[][] shift = (d.getArray());

        return shift;
    }

    /**
     * @param rotationMatrixMatPath
     * @Author: Zhi-Ming Dong, dzm.neu@gmail.com
     * @Date: created in 19-3-23 上午10:50
     * @Descriptiom:
     * @Return:
     */
    public static double[][][] readMatrixFromMatFile(String rotationMatrixMatPath) throws IOException {
        String path = Utils.class.getClassLoader().getResource(rotationMatrixMatPath).getPath();

        MatFileReader read = new MatFileReader(path);
        String key = (String) read.getContent().keySet().toArray()[0];

        MLCell mlCell = (MLCell) read.getMLArray(key);

//        System.out.println(mlCell.isCell());

        int sizex = mlCell.getSize();
        int sizey = mlCell.get(0).getDimensions()[0];
        int sizez = mlCell.get(0).getDimensions()[1];

        //
        double[][][] matrix = new double[sizex][sizey][sizez];

        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                for (int k = 0; k < sizez; k++) {
                    matrix[i][j][k] = ((MLDouble) mlCell.get(i)).get(j, k);
                }
            }
        }

        return matrix;
    }

    public static Front getParetoFront(Problem problem) throws FileNotFoundException {
        // TODO: 2019/7/10
        String referenceParetoFronts = "/momfo/PF/" + ((MO) problem).gethType() + ".pf";

        String path = Utils.class.getResource(referenceParetoFronts).getPath();

        return new ArrayFront(path);
    }
}
