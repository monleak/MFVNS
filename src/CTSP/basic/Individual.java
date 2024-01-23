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
    public int[] Chromosome; //Bắt đầu từ 0
    public double cost;
    public int skillfactor; // Bắt đầu đếm từ 0
    public int rank;

    /**
     * Khởi tạo 1 cá thể mới trong không gian chung
     * @param maxTotalVertices Số chiều của NST
     */
    public Individual(int maxTotalVertices){
        this.id = Individual.countID++;
        Chromosome = randIntArray(maxTotalVertices);
        cost = Double.MAX_VALUE;
        skillfactor = -1;
        rank = -1;
    }

    public Individual(int[] chromosome, int task){
        this.id = Individual.countID++;
        Chromosome = chromosome.clone();
        cost = Double.MAX_VALUE;
        skillfactor = task;
        rank = -1;
    }

    @Override
    public int compareTo(Individual o) {
        return Double.valueOf(this.cost).compareTo(o.cost);
    }
}
