package CTSP.IO;

import CTSP.benchmark.Graph;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import static CTSP.IO.DataIO.readDataCTSP;

public class ReadDataCTSP {
    public static ArrayList<Graph> scanCTSPfile(String path){
        ArrayList<Graph> graphs = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null)
            return graphs;

        for (File f : files) {

            if (f.isDirectory() && f.exists()) {
                try {
                    graphs.addAll(scanCTSPfile(f.getPath()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            else if (!f.isDirectory() && f.exists()) {
                // using file filter
                if (filter.accept(f)) {
//                    System.out.println(f.getPath());
                    graphs.add(readDataCTSP(f.getPath()));
                }
            }
        }
        return graphs;
    }

    // file filter for sort .clt files
    static FileFilter filter = new FileFilter() {
        @Override
        public boolean accept(File file)
        {
            if (file.getName().endsWith(".clt")) {
                return true;
            }
            return false;
        }
    };
}
