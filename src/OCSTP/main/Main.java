package OCSTP.main;

import OCSTP.basic.Params;
import OCSTP.benchmark.Problem;
import OCSTP.core.MFVNS;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
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
        File subDir = new File(Params.linkOutput+Params.linkOutputOCSTP); //Tạo thư mục con chứa kết quả OCSTP nếu chưa có
        if (!subDir.exists()) {
            boolean checkSubDirIsExists = subDir.mkdir();
            if(!checkSubDirIsExists){
                System.out.println("Lỗi khi tạo folder Result/OCSTP");
            }
        }

        //Ghi thứ tự các task vào 1 file
        ArrayList<String> orderTask = new ArrayList<>();
        Problem newProb = new Problem(Params.linkProb,orderTask);

        //in ra file
        String orderTaskFile = subDir.getPath()+"//ORDER-TASK.txt";
        DataOutputStream outOrderTask = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(orderTaskFile)));
        for(int i=0;i<orderTask.size();i++){
            outOrderTask.writeBytes(orderTask.get(i));
        }
        orderTask.clear();
        outOrderTask.close();

        //Hoạt động chính
        for (int seed = 0; seed < Params.REPT; seed++){
            ArrayList<String> result = new ArrayList<>();
            System.out.println("=============== Seed "+ seed +" =================");
            Params.rand = new Random(seed);
            Params.countEvals = 0;
            Params.maxEvals = /*prob.testCase.get(testCase).length * */ Params.MAX_EVALS_PER_TESTCASE;
            MFVNS solver = new MFVNS(newProb);
            solver.run(result);

            //in ra file
            String fitnessFile = subDir.getPath()+"//SEED" + (seed + 1) + ".txt";
            DataOutputStream outFit = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fitnessFile)));
            for(int i=0;i<result.size();i++){
                outFit.writeBytes(result.get(i));
            }
            result.clear();
            outFit.close();
        }

    }
}