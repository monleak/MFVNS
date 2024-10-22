package CTSP.basic;

import CTSP.benchmark.Graph;
import CTSP.util.util;

import java.util.ArrayList;
import java.util.Arrays;

import static CTSP.util.util.randIntArray;
import static CTSP.util.util.shuffleArray;

/**
 * Các thuộc tính thuộc class Individual đều được xét trong không gian chung
 */
public class Individual  implements Comparable<Individual> {
    public static int countID=0;
    public int id;
    public int[] Chromosome;
    public double[] cost;
    public int skillfactor; // Bắt đầu đếm từ 0
    public int rank;

    /**
     * Khởi tạo 1 cá thể mới trong không gian chung
     * @param maxTotalVertices Số chiều của NST
     * @param numberOfTask số lượng các tác vụ
     */
    public Individual(int maxTotalVertices, int numberOfTask){
        this.id = Individual.countID++;
        Chromosome = randIntArray(maxTotalVertices);

        cost = new double[numberOfTask];
        Arrays.fill(cost,Double.MAX_VALUE);

        skillfactor = -1;
        rank = -1;
    }

    @Override
    public int compareTo(Individual o) {
        return Double.valueOf(this.cost[0]).compareTo(o.cost[0]);
    }
}
