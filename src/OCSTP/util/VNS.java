package OCSTP.util;

import OCSTP.basic.Individual;
import OCSTP.basic.Params;
import OCSTP.benchmark.Graph;

import java.util.ArrayList;
import java.util.Arrays;

import static OCSTP.util.util.*;

public class VNS {
    /**
     * Sử dụng double bridge để biến đổi gen ban đầu
     *
     * @param  Chromosome Gen ban đầu
     * @return Gen sau khi biến đổi
     */
    public static int[] Shaking(int[] Chromosome){
        int[] x = Chromosome.clone();
        if(Chromosome.length < 8){
            int p1,p2;
            p1 = Params.rand.nextInt(Chromosome.length);
            do{
                p2 = Params.rand.nextInt(Chromosome.length);
            }while (p2==p1);

            int temp = x[p1];
            x[p1] = x[p2];
            x[p2] = temp;
        } else {
            //Using double bridge
            int[] allowSelect = new int[x.length];
            for (int i=0;i<x.length;i++){
                allowSelect[i] = i;
            }
            int[] point = new int[4];
            for (int i=0;i<point.length;i++){
                int select;
                do{
                    select = Params.rand.nextInt(allowSelect.length);
                }while (allowSelect[select] == -1);

                point[i] = allowSelect[select];
                allowSelect[select] = -1;
                //select + 1
                if(select == x.length-1)
                    allowSelect[0] = -1;
                else
                    allowSelect[select+1] = -1;

                //select - 1
                if(select == 0)
                    allowSelect[x.length - 1] =-1;
                else
                    allowSelect[select - 1] = -1;
            }
            Arrays.sort(point);

            int[] tempX = new int[x.length];
            int count = 0;
            tempX[count++] = x[point[0]];
            for (int i = point[2]+1;i<=point[3];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for (int i = point[1]+1;i<=point[2];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for (int i = point[0]+1;i<=point[1];i++){
                tempX[count++] = x[i];
                x[i] = -1;
            }
            for(int i=point[3]+1;i<x.length;i++){
                tempX[count++] = x[i];
            }
            for(int i=0;i<point[0];i++){
                tempX[count++] = x[i];
            }

            x = tempX;
        }
        return x;
    }

    /**
     * Di chuyển cá thể hiện tại tới lời giải tốt nhất trong tập các lời giải lân cận
     *
     * @param indiv Cá thể ban đầu
     * @param graph Đồ thị cần trùng với skillfactor của cá thể
     * @return boolean Có cải thiện sau khi search hay không (T/F)
     */
    public static boolean localSearch(Individual indiv, Graph graph, int maxTotalVertices){
        boolean positive = false;
        double startLength = indiv.cost[indiv.skillfactor];
        Individual newIndiv = runDSCG(indiv,200,graph, maxTotalVertices);
        newIndiv.cost[newIndiv.skillfactor] = utilOCSTP.calCost(newIndiv,graph);

        if(startLength > newIndiv.cost[newIndiv.skillfactor]){
            indiv = newIndiv;
            positive = true;
        }
        return positive;
    }

    private static final double INIT_STEP_SIZE = 0.02;
    private static final double EPSILON = 1E-8;
    private static final int EVALS_PER_LINE_SEARCH = 10;
    private static Individual runDSCG(Individual start_point, int fes, Graph graph, int maxTotalVertices) { //fes = 2000
        double s = INIT_STEP_SIZE;
        int evals_per_linesearch = EVALS_PER_LINE_SEARCH;
        int dim = start_point.Chromosome.length;

        Individual result = start_point.clone();

        Individual[] x = new Individual[dim + 2];
        for (int i = 0; i < x.length; i++) {
            x[i] = new Individual(maxTotalVertices,start_point.cost.length);
            x[i].skillfactor = start_point.skillfactor;
        }

        x[0].Chromosome = start_point.Chromosome.clone();
        x[0].cost = start_point.cost.clone();

        int direct = 1, evals = 0;
        double[][] v = new double[dim + 1][dim];
        double[][] a = new double[dim][dim];
        for (int i = 0; i <= dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == j) {
                    v[i][j] = 1;
                } else {
                    v[i][j] = 0;
                }
            }
        }

        while (true) {
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    a[i][j] = 0;
                }
            }

            // line search
            while (evals < fes - evals_per_linesearch) {
                evals += lineSearch(x[direct - 1], x[direct], EVALS_PER_LINE_SEARCH, graph, s, v[direct - 1],maxTotalVertices);

                // x_fit[direct - 1] >= x_fit[direct]
                for (int i = 1; i <= direct; i++) {
                    for (int j = 0; j < dim; j++) {
                        a[i - 1][j] += x[direct].Chromosome[j] - x[direct - 1].Chromosome[j];
                    }
                }

                // update best
                if (result.getFitness() > x[direct].getFitness()) {
                    result.cost[result.skillfactor] = x[direct].getFitness();
                    result.Chromosome = x[direct].Chromosome.clone();
                }

                if (direct < dim) {
                    direct++;
                } else {
                    break;
                }
            }

            if (evals >= fes || direct < dim) {
                break;
            }

            // Eventually one more line search
            double z[] = new double[dim];
            double norm_z = 0;
            for (int i = 0; i < dim; i++) {
                z[i] = x[dim].Chromosome[i] - x[0].Chromosome[i];
                norm_z += z[i] * z[i];
            }
            norm_z = Math.sqrt(norm_z);

            if (norm_z == 0) {
                x[dim + 1].Chromosome = x[dim].Chromosome.clone();
                x[dim + 1].cost = x[dim].cost.clone();
                // Termination criterion
                s *= 0.1;
                if (s <= EPSILON) {
                    // end the search
                    break;
                } else {
                    // next loop
                    direct = 1;
                    x[0].Chromosome = x[dim + 1].Chromosome.clone();
                    x[0].cost = x[dim + 1].cost.clone();
                }
            } else {
                for (int i = 0; i < dim; i++) {
                    v[dim][i] = z[i] / norm_z;
                }
                direct = dim + 1;

                int rest_eval = fes - evals;
                int overall_ls_eval;
                if (rest_eval < evals_per_linesearch) {
                    overall_ls_eval = rest_eval;
                } else {
                    overall_ls_eval = evals_per_linesearch;
                }

                evals += lineSearch(x[direct - 1], x[direct], overall_ls_eval, graph, s, v[direct - 1],maxTotalVertices);

                // update best
                if (result.getFitness() > x[direct].getFitness()) {
                    result.cost[result.skillfactor] = x[direct].getFitness();
                    result.Chromosome = x[direct].Chromosome.clone();
                }

                // Check appropriateness of step length
                norm_z = 0;
                for (int i = 0; i < dim; i++) {
                    double tmp = x[direct].Chromosome[i] - x[0].Chromosome[i];
                    norm_z += tmp * tmp;
                }
                norm_z = Math.sqrt(norm_z);

                if (norm_z < s) {
                    // Termination criterion
                    s *= 0.1;
                    if (s <= EPSILON) {
                        // end the search
                        break;
                    } else {
                        // next loop
                        direct = 1;

                        x[0].Chromosome = x[dim + 1].Chromosome.clone();
                        x[0].cost = x[dim + 1].cost.clone();
                    }
                } else {
                    // Orthogonalization
                    // v = GramSchmidtOrthogonalization(v, a);

                    direct = 2;
                    x[0].Chromosome = x[dim].Chromosome.clone();
                    x[1].Chromosome = x[dim + 1].Chromosome.clone();

                    x[0].cost = x[dim].cost.clone();
                    x[1].cost = x[dim + 1].cost.clone();
                }
            }
        }

        if (result.getFitness() > x[dim + 1].getFitness()) {
            result = x[dim + 1];
        }

        return result;
    }

    private static int lineSearch(Individual start_point, Individual result, int fes, Graph graph, double step_size, double[] v, int maxTotalVertices) {
        int evals = 0;
        int dim = start_point.Chromosome.length;
        double s = step_size;
        boolean change;
        boolean interpolation_flag = false;
        Individual x0 = new Individual(maxTotalVertices,start_point.cost.length);
        Individual x = new Individual(maxTotalVertices,start_point.cost.length);
        x.skillfactor = start_point.skillfactor;
        x0.skillfactor = start_point.skillfactor;

        for (int i = 0; i < dim; i++) {
            x0.Chromosome[i] = start_point.Chromosome[i];
            x.Chromosome[i] = start_point.Chromosome[i] + s * v[i];
        }
        x0.cost = start_point.cost.clone();
        x.cost[start_point.skillfactor] = utilOCSTP.calCost(x,graph);
        evals++;

        // For Lagrangian quadratic interpolation
        double F[] = new double[3];
        Individual interpolation_points[] = new Individual[3];
        for (int i = 0; i < 3; i++) {
            interpolation_points[i] = new Individual(maxTotalVertices,start_point.cost.length);
        }

        // x1, x2 of for the Lagrangian quadratic interpolation
        interpolation_points[0].Chromosome = x0.Chromosome.clone();
        interpolation_points[1].Chromosome = x.Chromosome.clone();
        F[0] = x0.cost[x0.skillfactor];
        F[1] = x.cost[x.skillfactor];

        // Step backward
        if (x.getFitness() > x0.getFitness()) {
            for (int i = 0; i < dim; i++) {
                x.Chromosome[i] = x.Chromosome[i] - 2 * s * v[i];
            }
            s = -s;

            x.cost[x.skillfactor] = utilOCSTP.calCost(x,graph);
            evals++;

            if (x.getFitness() <= x0.getFitness()) {
                change = true;

                // update x1 and x2 for the Lagrangian quadratic interpolation
                interpolation_points[0].Chromosome = x0.Chromosome.clone();
                interpolation_points[1].Chromosome = x.Chromosome.clone();
                F[0] = x0.getFitness();
                F[1] = x.getFitness();
            } else {
                change = false;
                interpolation_flag = true;

                // x1, x2, x3 for the Lagrangian quadratic interpolation
                interpolation_points[2].Chromosome = interpolation_points[1].Chromosome.clone();
                interpolation_points[1].Chromosome = interpolation_points[0].Chromosome.clone();
                interpolation_points[0].Chromosome = x.Chromosome.clone();

                F[2] = F[1];
                F[1] = F[0];
                F[0] = x.getFitness();
            }
        } else {
            // activate further steps
            change = true;
        }

        // Further steps
        while (change) {
            s *= 2;

            for (int i = 0; i < dim; i++) {
                x0.Chromosome[i] = x.Chromosome[i];
                x.Chromosome[i] = x0.Chromosome[i] + s * v[i];
            }

            x0.cost = x.cost.clone();
            x.cost[x.skillfactor] = utilOCSTP.calCost(x,graph);
            evals++;

            if (x.getFitness() < x0.getFitness()) {
                // update x1 and x2 for the Lagrangian quadratic interpolation
                interpolation_points[0].Chromosome = x0.Chromosome.clone();
                interpolation_points[1].Chromosome = x.Chromosome.clone();
                F[0] = x0.getFitness();
                F[1] = x.getFitness();
            } else {
                change = false;
                interpolation_flag = true;

                // x3 = x
                interpolation_points[2].Chromosome = x.Chromosome.clone();
                F[2] = x.getFitness();

                // generate x = x0 + 0.5s
                s *= 0.5;
                for (int i = 0; i < dim; i++) {
                    x.Chromosome[i] = x0.Chromosome[i] + s * v[i];
                }
                x.cost[x.skillfactor] = utilOCSTP.calCost(x,graph);
                evals++;

                // reject the one which is furthest from the point that has the smallest value
                // of the objective function
                if (x.getFitness() > F[1]) {
                    // x2 is smallest
                    // reject x3, new x3 = x
                    interpolation_points[2].Chromosome = x.Chromosome.clone();
                    F[2] = x.getFitness();
                } else {
                    // x is smallest
                    // reject current x1, new x1 = x2, new x2 = x
                    for (int i = 0; i < dim; i++) {
                        interpolation_points[0].Chromosome[i] = interpolation_points[1].Chromosome[i];
                        interpolation_points[1].Chromosome[i] = x.Chromosome[i];
                    }
                    F[0] = F[1];
                    F[1] = x.getFitness();
                }
            }

            if (evals >= fes - 2) {
                change = false;
            }
        }

        // Lagrangian quadratic interpolation
        if (interpolation_flag && (F[0] - 2 * F[1] + F[2] != 0)) {
            // x = x2 + Lagrangian quadratic interpolation
            for (int i = 0; i < dim; i++) {
                x.Chromosome[i] = interpolation_points[1].Chromosome[i] + s * (F[0] - F[2]) / (2.0 * (F[0] - 2 * F[1] + F[2]));
            }
            x.cost[x.skillfactor] = utilOCSTP.calCost(x,graph);
            evals++;

            if (x.getFitness() < F[1]) {
                // best found = Lagrangian quadratic interpolation point
                result.Chromosome = x.Chromosome.clone();
                result.cost = x.cost.clone();
                result.skillfactor = x.skillfactor;
            } else {
                // best found = x2
                result.Chromosome = interpolation_points[1].Chromosome.clone();
                result.cost[result.skillfactor] = F[1];
            }
        } else {
            // best found = x2
            result.Chromosome = interpolation_points[1].Chromosome.clone();
            result.cost[result.skillfactor] = F[1];
        }

        return evals;
    }
}
