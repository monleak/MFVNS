package CTSP.basic;

import CTSP.benchmark.Problem;
import CTSP.util.util;

import java.util.Arrays;

import static CTSP.util.util.randIntArray;
import static CTSP.util.util.shuffleArray;

public class Individual  implements Comparable<Individual> {
    public static int countID=0;
    public int id;
    public int[] Chromosome;
    public double[] cost;
    public int skillfactor; // Bắt đầu đếm từ 0
    public int rank;

    /**
     * Khởi tạo 1 cá thể mới trong không gian chung
     * @param maxTotalVertices
     * @param numberOfTask
     */
    public Individual(int maxTotalVertices, int numberOfTask){
        this.id = Individual.countID++;
        Chromosome = randIntArray(maxTotalVertices);

        cost = new double[numberOfTask];
        Arrays.fill(cost,Double.MAX_VALUE);

        skillfactor = -1;
        rank = -1;
    }
    public Individual(int[] Chromosome, double cost,int skillfactor){
        this.id = Individual.countID++;
        this.Chromosome = Chromosome;
        rank = -1;

        this.cost = new double[8];
        Arrays.fill(this.cost,Double.MAX_VALUE);
        this.cost[skillfactor] = cost;
        this.skillfactor = skillfactor;
    }

    public void calCost(Problem prob, int idGraph){
        cost[idGraph] = 0;
        int[] decodeChromosome = util.decodeChromosome(Chromosome,prob.graphs.get(idGraph).totalVertices);

        for(int i=0;i<decodeChromosome.length-1;i++){
            cost[idGraph] += prob.graphs.get(idGraph).distance[decodeChromosome[i]][decodeChromosome[i+1]];
        }
        cost[idGraph] += prob.graphs.get(idGraph).distance[decodeChromosome[decodeChromosome.length-1]][decodeChromosome[0]];
        Params.countEvals++;
    }

    @Override
    public int compareTo(Individual o) {
        return Double.valueOf(this.cost[0]).compareTo(o.cost[0]);
    }
}
