package CTSP.basic;

import java.util.Random;

public class Params {
    public static int type = 2; // 1-Large  2-Small
    public static String linkProb = "Data/relaxed_Data/Cluster_small";
    public static String linkData = "Data//";
    public static String linkOutput = "Result//";
    public static String linkOutputCTSP = "CTSP//";
    public static String linkLarge = "Large//";
    public static String linkSmall = "Small//";
    public static int POP_SIZE= 200;
    public static int kmax = 4;
    public static Random rand; //Được khởi tạo theo các seed
    public static double countEvals; //Đếm số lần đánh giá
    public static int recordsNum; //Số lần ghi lại kết quả
    public static final int REPT = 30; //Số lần chạy
    public static int maxEvals;
    public static final int MAX_EVALS_PER_TESTCASE = 500000;
    public static int maxGeneration = 1000;
    public static double bigNumber = 10000000;

    public static final double C = 0.02;

}
