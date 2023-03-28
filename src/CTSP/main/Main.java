package CTSP.main;

import CTSP.basic.Params;
import CTSP.benchmark.Problem;
import CTSP.core.MFVNS;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        Params.recordsNum = 1000;
        Problem prob = new Problem();

    /*
        Tạo các thư mục chứa kết quả
     */
        File dir = new File(Params.linkOutput);
        if (!dir.exists()) {
            boolean checkDirIsExists = dir.mkdir();
            if(!checkDirIsExists){
                System.out.println("Lỗi khi tạo folder Result");
            }
        }
        File subDir = new File(Params.linkOutput+Params.linkOutputCTSP); //Tạo thư mục con chứa kết quả CTSP nếu chưa có
        if (!subDir.exists()) {
            boolean checkSubDirIsExists = subDir.mkdir();
            if(!checkSubDirIsExists){
                System.out.println("Lỗi khi tạo folder Result/CTSP");
            }
        }


        for(int testCase = 0;testCase<prob.testCase.size();testCase++){

            String subFolder = Params.linkOutput+Params.linkOutputCTSP+"TESTCASE"+testCase+"//";
            File dirTestCase = new File(subFolder);
            if (!dirTestCase.exists()) {
                dirTestCase.mkdir();
            }

            for (int seed = 0; seed < Params.REPT; seed++){
                ArrayList<String> result = new ArrayList<>();

                System.out.println("=============== Test case " + (testCase+1) +" Seed "+ seed +" =================");
                Params.rand = new Random(seed);
                Params.countEvals = 0;
                Params.maxEvals = /*prob.testCase.get(testCase).length * */ Params.MAX_EVALS_PER_TESTCASE;
                MFVNS solver = new MFVNS(prob,testCase);
                solver.run(result);

                //in ra file
                String fitnessFile = subFolder + (seed + 1) + ".txt";
                DataOutputStream outFit = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fitnessFile)));
                for(int i=0;i<result.size();i++){
                    outFit.writeBytes(result.get(i));
                }
                result.clear();
                outFit.close();
            }
        }
    }
}