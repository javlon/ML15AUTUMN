package pca;

import Jama.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by javlon on 20.12.15.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(args[0]));
        double percent = Double.parseDouble(args[1]);
        if (percent < 0 || percent > 1)
            throw new IllegalArgumentException("args[1], is not from (0,1)");

        List<String[]> list = new ArrayList<>();
        while (sc.hasNextLine()) {
            String[] strings = sc.nextLine().split(" ");
            list.add(strings);
        }
        int l = list.size();
        int n = list.get(0).length;
        double[][] d = new double[l][n];
        for (int i = 0; i < l; ++i) {
            String[] s = list.get(i);
            if (s.length != n)
                throw new IllegalArgumentException("All lines should have the same number of features.");
            for (int j = 0; j < n; ++j)
                d[i][j] = Double.parseDouble(s[j]);
        }
        double[] u = new double[n];
        for (int i = 0; i < l; ++i)
            for (int j = 0; j < n; ++j)
                u[j] += d[i][j];
        for (int i = 0; i < n; ++i)
            u[i] /= l;
        double[][] b = new double[l][n];
        for (int i = 0; i < l; ++i)
            for (int j = 0; j < n; ++j)
                b[i][j] = d[i][j] - u[j];
        Matrix m = new Matrix(b);
        Matrix mc = m.transpose().times(m).times(1.0 / (n - 1));

        double[] eigenvalues = mc.eig().getRealEigenvalues();
        Arrays.sort(eigenvalues);
        double sum = 0;
        for (int i = 0; i < eigenvalues.length; ++i)
            sum += eigenvalues[i];

        double lambda = 0;
        for (int i = eigenvalues.length - 1; i >= 0; --i) {
            lambda += eigenvalues[i];
            if (lambda / sum >= percent) {
                System.out.println("The number of dimensions in the dimensionally reduced subspace: " +
                        +(eigenvalues.length - i) + "   percent: " + lambda / sum);
                return;
            }
        }
        System.out.println("I can't find");
    }
}
