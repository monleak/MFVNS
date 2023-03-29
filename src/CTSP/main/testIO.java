package CTSP.main;

import CTSP.benchmark.Graph;

import java.io.IOException;

import static CTSP.IO.DataIO.readDataCTSP;
import static CTSP.IO.ReadDataCTSP.scanCTSPfile;

public class testIO {
    public static void main(String[] args){
        scanCTSPfile("Data/b4gcgybvt6-4/Euclidean instances/Large Instances/Type_1_Large");
    }
}
