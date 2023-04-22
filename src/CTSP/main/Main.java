package CTSP.main;

import CTSP.basic.Params;
import CTSP.benchmark.Problem;
import CTSP.core.MFVNS;

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
        File subDir = new File(Params.linkOutput+Params.linkOutputCTSP); //Tạo thư mục con chứa kết quả CTSP nếu chưa có
        if (!subDir.exists()) {
            boolean checkSubDirIsExists = subDir.mkdir();
            if(!checkSubDirIsExists){
                System.out.println("Lỗi khi tạo folder Result/CTSP");
            }
        }

        /*
        LARGE INSTANCES
         */
        File largeInstance = new File(Params.linkOutput+Params.linkOutputCTSP+"Large//"); //Tạo thư mục con chứa kết quả Large instance nếu chưa có
        if (!largeInstance.exists()) {
            boolean check = largeInstance.mkdir();
            if(!check){
                System.out.println("Lỗi khi tạo folder Result/CTSP/Large/");
            }
        }

        //Ghi thứ tự các task vào 1 file
        ArrayList<String> orderTask = new ArrayList<>();
        Problem largeProb = new Problem("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large",orderTask);

        File typeDir = new File(Params.linkOutput+Params.linkOutputCTSP+"Large//"+largeProb.instanceName);
        if (!typeDir.exists()) {
            boolean check = typeDir.mkdir();
            if(!check){
                System.out.println("Lỗi khi tạo folder Result/CTSP/Large/"+largeProb.instanceName);
            }
        }

        //in ra file
        String orderTaskFile = typeDir.getPath()+"//"+ largeProb.instanceName+"_ORDER-TASK.txt";
        DataOutputStream outOrderTask = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(orderTaskFile)));
        for(int i=0;i<orderTask.size();i++){
            outOrderTask.writeBytes(orderTask.get(i));
        }
        orderTask.clear();
        outOrderTask.close();

        for (int seed = 0; seed < Params.REPT; seed++){
            ArrayList<String> result = new ArrayList<>();
            System.out.println("=============== " + largeProb.instanceName +" Seed "+ seed +" =================");
            Params.rand = new Random(seed);
            Params.countEvals = 0;
            Params.maxEvals = /*prob.testCase.get(testCase).length * */ Params.MAX_EVALS_PER_TESTCASE;
            MFVNS solver = new MFVNS(largeProb);
            solver.run(result);

            //in ra file
            String fitnessFile = typeDir.getPath()+"//"+ largeProb.instanceName+"_SEED" + (seed + 1) + ".txt";
            DataOutputStream outFit = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fitnessFile)));
            for(int i=0;i<result.size();i++){
                outFit.writeBytes(result.get(i));
            }
            result.clear();
            outFit.close();
        }

    }
}