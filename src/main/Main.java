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

    }
}