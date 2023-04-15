package CTSP.util;

import CTSP.basic.Individual;
import CTSP.basic.Params;

import java.util.ArrayList;

public class MFEA {
    /**
     * Lai ghép 2 cá thể ban đầu để tạo ra 2 con mới
     * Lưu ý 2 cá thể con này chưa được tính cost
     *
     * @param  parentA
     * @param  parentB
     * @return 2 con mới
     */
    public static ArrayList<Individual> SBX(Individual parentA, Individual parentB){
        int maxTotalVertices = parentA.Chromosome.length;
        //Khởi tạo 2 cá thể con mới
        Individual o1 = new Individual(maxTotalVertices, parentA.cost.length);
        Individual o2 = new Individual(maxTotalVertices, parentA.cost.length);

        int[] ChromosomeA = parentA.Chromosome.clone();
        int[] ChromosomeB = parentB.Chromosome.clone();

        int point1,point2;
        point1 = Params.rand.nextInt(maxTotalVertices);
        do{
            point2 = Params.rand.nextInt(maxTotalVertices);
        }while (point2 == point1);

        if(point1 > point2){
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        for(int i=point1;i<=point2;i++){
            o1.Chromosome[i]=parentA.Chromosome[i];
            for (int j = 0; j< maxTotalVertices; j++){
                if(ChromosomeB[j]==parentA.Chromosome[i]){
                    ChromosomeB[j] = -1;
                    break;
                }
            }
            o2.Chromosome[i]=parentB.Chromosome[i];
            for (int j = 0; j< maxTotalVertices; j++){
                if(ChromosomeA[j]==parentB.Chromosome[i]){
                    ChromosomeA[j] = -1;
                    break;
                }
            }
        }
        int count1=0,count2=0;
        for (int i = 0; i< maxTotalVertices; i++){
            if(count1 == point1)
                count1 = point2+1;
            if(count2 == point1)
                count2 = point2+1;
            if(ChromosomeA[i] != -1){
                o2.Chromosome[count1++] = ChromosomeA[i];
            }
            if(ChromosomeB[i] != -1){
                o1.Chromosome[count2++] = ChromosomeB[i];
            }
        }

        o1.skillfactor = Params.rand.nextBoolean() ? parentA.skillfactor : parentB.skillfactor;
        o2.skillfactor = Params.rand.nextBoolean() ? parentA.skillfactor : parentB.skillfactor;

        ArrayList<Individual> child = new ArrayList<>();
        child.add(o1);child.add(o2);

        return child;
    }
}
