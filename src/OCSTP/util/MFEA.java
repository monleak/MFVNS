package OCSTP.util;

import OCSTP.basic.Individual;
import OCSTP.basic.Params;
import OCSTP.benchmark.Graph;
import OCSTP.benchmark.Problem;

import java.util.ArrayList;
import java.util.Arrays;

import static OCSTP.util.utilOCSTP.calCost;

public class MFEA {
    /**
     * Lai ghép 2 cá thể ban đầu để tạo ra 2 con mới
     *
     * @param  parentA
     * @param  parentB
     * @return 2 con mới
     */
//    public static ArrayList<Individual> SBX(Individual parentA, Individual parentB, Problem prob){
//        //TODO: fix lỗi, nhận định ban đầu là do gen của 2 cá thể truyền vào bị lỗi, cần kiểm tra lại các hàm thay đổi gen, decode, encode
//        int maxTotalVertices = prob.maxTotalVertices;
//        //Khởi tạo 2 cá thể con mới
//        Individual o1 = new Individual(maxTotalVertices, prob.graphs.size());
//        Individual o2 = new Individual(maxTotalVertices, prob.graphs.size());
//
//        var ChromosomeA = parentA.Chromosome.clone();
//        var ChromosomeB = parentB.Chromosome.clone();
//
//        int point1,point2;
//        point1 = Params.rand.nextInt(maxTotalVertices);
//        do{
//            point2 = Params.rand.nextInt(maxTotalVertices);
//        }while (point2 == point1);
//
//        if(point1 > point2){
//            int temp = point1;
//            point1 = point2;
//            point2 = temp;
//        }
//        for(int i=point1;i<=point2;i++){
//            o1.Chromosome[i]=parentA.Chromosome[i];
//            for (int j = 0; j< maxTotalVertices; j++){
//                if(ChromosomeB[j]==parentA.Chromosome[i]){
//                    ChromosomeB[j] = -1;
//                    break;
//                }
//            }
//            o2.Chromosome[i]=parentB.Chromosome[i];
//            for (int j = 0; j< maxTotalVertices; j++){
//                if(ChromosomeA[j]==parentB.Chromosome[i]){
//                    ChromosomeA[j] = -1;
//                    break;
//                }
//            }
//        }
//        int count1=0,count2=0;
//        for (int i = 0; i< maxTotalVertices; i++){
//            if(count1 == point1)
//                count1 = point2+1;
//            if(count2 == point1)
//                count2 = point2+1;
//            if(ChromosomeA[i] != -1){
//                if(count1 == 100){
//                    break;
//                }
//                o2.Chromosome[count1++] = ChromosomeA[i];
//            }
//            if(ChromosomeB[i] != -1){
//                o1.Chromosome[count2++] = ChromosomeB[i];
//            }
//        }
//
//        o1.skillfactor = Params.rand.nextBoolean() ? parentA.skillfactor : parentB.skillfactor;
//        o2.skillfactor = Params.rand.nextBoolean() ? parentA.skillfactor : parentB.skillfactor;
//
//        o1.cost[o1.skillfactor] = calCost(o1.Chromosome,prob.graphs.get(o1.skillfactor),prob.maxTotalVertices);
//        o2.cost[o2.skillfactor] = calCost(o2.Chromosome,prob.graphs.get(o2.skillfactor),prob.maxTotalVertices);
//
//        ArrayList<Individual> child = new ArrayList<>();
//        child.add(o1);child.add(o2);
//
//        return child;
//    }

    public static ArrayList<Individual> Crossover(Individual parentA, Individual parentB, Problem prob){
        int maxTotalVertices = prob.maxTotalVertices;
        //Khởi tạo 2 cá thể con mới
        Individual o1 = new Individual(maxTotalVertices, prob.graphs.size());
        Individual o2 = new Individual(maxTotalVertices, prob.graphs.size());

        var ChromosomeA = parentA.Chromosome.clone();
        var ChromosomeB = parentB.Chromosome.clone();

        int point1 = Params.rand.nextInt(maxTotalVertices);

        for (int i = 0; i < point1; i++) {
            o1.Chromosome[i] = ChromosomeA[i];
            o2.Chromosome[i] = ChromosomeB[i];
        }
        for (int i = point1; i < maxTotalVertices; i++) {
            o1.Chromosome[i] = ChromosomeB[i];
            o2.Chromosome[i] = ChromosomeA[i];
        }

        o1.skillfactor = Params.rand.nextBoolean() ? parentA.skillfactor : parentB.skillfactor;
        o2.skillfactor = Params.rand.nextBoolean() ? parentA.skillfactor : parentB.skillfactor;

        o1.cost[o1.skillfactor] = calCost(o1.Chromosome,prob.graphs.get(o1.skillfactor),prob.maxTotalVertices);
        o2.cost[o2.skillfactor] = calCost(o2.Chromosome,prob.graphs.get(o2.skillfactor),prob.maxTotalVertices);

        ArrayList<Individual> child = new ArrayList<>();
        child.add(o1);child.add(o2);

        return child;
    }
}
