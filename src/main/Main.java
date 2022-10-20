package main;

import basic.Params;
import benchmark.Graph;
import benchmark.Problem;

import java.io.File;
import java.util.Random;

import static IO.DataIO.readDataTSP;

public class Main {
    public static void main(String[] args) {
        Problem prob = new Problem();
        for (int seed = 0;seed < Params.REPT;seed++){
            Params.rand = new Random(seed);
            Params.countEvals = 0;

            while (Params.countEvals < Params.MAX_EVALS_PER_TESTCASE){
                //TODO: Xử lý tác vụ
            }
        }
    }
}