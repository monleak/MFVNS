package OCSTP.IO;

import OCSTP.benchmark.Graph;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import static OCSTP.IO.DataIO.readDataCTSP;

public class ReadDataCTSP {
    public static ArrayList<Graph> scanCTSPfile(String path, ArrayList<String> orderTask){
        ArrayList<Graph> graphs = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null)
            return graphs;

        for (File f : files) {
            if (f.isDirectory() && f.exists()) {
                try {
                    graphs.addAll(scanCTSPfile(f.getPath(),orderTask));
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
                    typeFile type = null;
                    if(f.getName().endsWith(".clt")){
                        type = typeFile.CLT;
                    }else if(f.getName().endsWith(".htsp")){
                        type = typeFile.HTSP;
                    }
                    graphs.add(readDataCTSP(f.getPath(),orderTask,type));
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
            return file.getName().endsWith(".clt") || file.getName().endsWith(".htsp");
        }
    };
}
