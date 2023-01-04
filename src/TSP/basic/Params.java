package TSP.basic;

import java.util.Random;

public class Params {
    public static String linkData = "Data//TSP//";
    public static String linkOutput = "Result//";
    public static String linkOutputTSP = "resultTSP//";
    public static int maxTotalVertices = 0;
    public static int POP_SIZE= 200;
    public static int kmax = 4;
    public static Random rand; //Được khởi tạo theo các seed
    public static double countEvals; //Đếm số lần đánh giá
    public static int recordsNum; //Số lần ghi lại kết quả
    public static final int REPT = 30; //Số lần chạy
    public static int maxEvals;
    public static final int MAX_EVALS_PER_TESTCASE = 500000;
    public static int maxGeneration = 1000;

    public static final double C = 0.02;

}
