package OCSTP.IO;

import OCSTP.benchmark.Graph;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import static OCSTP.IO.DataIO.readDataOCSTP;

public class ReadDataOCSTP {
    public static ArrayList<Graph> scanOCSTPfile(String path, ArrayList<String> orderTask){
        ArrayList<Graph> graphs = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null)
            return graphs;

        for (File f : files) {
            if (f.isDirectory() && f.exists()) {
                try {
                    graphs.addAll(scanOCSTPfile(f.getPath(),orderTask));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (!f.isDirectory() && f.exists()) {
                // using file filter
                if (filter.accept(f)) {
                    graphs.add(readDataOCSTP(f.getPath(),orderTask));
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
            return file.getName().endsWith(".txt");
        }
    };
}
