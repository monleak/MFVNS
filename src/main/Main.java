package main;

import basic.Individual;
import basic.Params;
import basic.TSP_Population;
import benchmark.Graph;
import benchmark.Problem;
import core.MFVNS;

import java.io.File;
import java.util.Random;

import static IO.DataIO.readDataTSP;
import static core.MFVNS.do_2_Opt;
import static util.util.codeChromosome;
import static util.util.decodeChromosome;

public class Main {
    public static void main(String[] args) {
        Params.recordsNum = 1000;
        Problem prob = new Problem();
        for(int testCase = 0;testCase<prob.testCase.size();testCase++){

            for (int seed = 0;seed < Params.REPT;seed++){
                System.out.println("=============== Test case " + (testCase+1) +" Seed "+ seed +" =================");
                Params.rand = new Random(seed);
                Params.countEvals = 0;
                Params.maxEvals = prob.testCase.get(testCase).length * Params.MAX_EVALS_PER_TESTCASE;
                //TODO: Kiểm tra các phần tính countEval
                MFVNS solver = new MFVNS(prob,testCase);
                solver.run();
            }
        }

//        //test code
//        int[] x = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
//        int[] y = decodeChromosome(x,10);
//        y = do_2_Opt(y,3,9);
//        int[] z = codeChromosome(y,x);
    }
}