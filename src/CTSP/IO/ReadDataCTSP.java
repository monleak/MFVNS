package CTSP.IO;

import java.io.File;
import java.io.FileFilter;

import static CTSP.IO.DataIO.readDataCTSP;

public class ReadDataCTSP {
    //TODO: Viết các hàm quét tất cả file data CTSP. Các file này sẽ được gọi trong benchmark.Problem để tự động khởi tạo list các graph

    public static void scanCTSPfile(String path){
        File file = new File(path);

        File[] files = file.listFiles();
        if (files == null)
            return;

        for (File f : files) {

            if (f.isDirectory() && f.exists()) {
                try {
                    scanCTSPfile(f.getPath());
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
                    readDataCTSP(f.getPath());
                }
            }
        }
    }

    // file filter for sort mp3 files
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
