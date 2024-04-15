package OCSTP.basic;

import OCSTP.benchmark.Graph;
import OCSTP.util.util;

import java.util.ArrayList;
import java.util.Arrays;

import static OCSTP.util.util.randDoubleArray;
import static OCSTP.util.util.shuffleArray;

/**
 * Các thuộc tính thuộc class Individual đều được xét trong không gian chung
 */
public class Individual  implements Comparable<Individual> {
    public static int countID=0;
    public int id;
    public double[] Chromosome; //Chua do uu tien cua canh
    public double[] cost;
    public int skillfactor; // Bắt đầu đếm từ 0
    public int rank;
    public int countNegative;

    /**
     * Khởi tạo 1 cá thể mới trong không gian chung
     * @param maxTotalVertices Số chiều của NST
     * @param numberOfTask số lượng các tác vụ
     */
    public Individual(int maxTotalVertices, int numberOfTask){
        this.id = Individual.countID++;
        Chromosome = randDoubleArray(maxTotalVertices*(maxTotalVertices-1)/2);
        cost = new double[numberOfTask];
        Arrays.fill(cost,Double.MAX_VALUE);

        skillfactor = -1;
        rank = -1;
    }

    public Individual(){
        this.id = Individual.countID++;
    }

    public Individual clone(){
        Individual clone = new Individual();
        clone.Chromosome = this.Chromosome.clone();
        clone.cost = this.cost.clone();
        clone.skillfactor = this.skillfactor;
        clone.rank = this.rank;
        return clone;
    }

    public double getFitness(){
        return cost[skillfactor];
    }

    @Override
    public int compareTo(Individual o) {
        return Double.valueOf(this.cost[0]).compareTo(o.cost[0]);
    }
}
