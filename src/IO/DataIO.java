package IO;

import benchmark.Graph;
import benchmark.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;

public class DataIO {
    public static Graph readDataTSP(String linkFile) {
        Graph graph = null;
        //Khởi tạo cấu trúc dữ liệu và lưu nó trong mảng 2 chiều
        BufferedReader readBuffer = null;
        try{
            readBuffer = new BufferedReader(new FileReader(linkFile));
            String line;
            do{
                //Đọc từng dòng của file đến phần bắt đầu tọa độ
                line = readBuffer.readLine();
                if(line.equals("NODE_COORD_SECTION")) break; //Thoát khỏi vòng lặp nếu gặp NODE_COORD_SECTION
                if(line.contains("DIMENSION")){
                    //Đọc giá trị của tổng số thành phố
                    String[] result = line.split(" : ");
                    graph = new Graph(Integer.parseInt(result[1]));
                }
            }while (true);
            do{
                //Đọc từng dòng của file và lưu dữ liệu tọa độ vào mảng 2 chiều
                line = readBuffer.readLine();
                if(line.equals("EOF")) break; //Thoát khỏi vòng lặp nếu gặp EOF
                String[] result = line.split(" ");
                Vertex vertex = new Vertex(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]));
                assert graph != null;
                graph.addVertex(vertex);
            }while (true);
            for(int i=0;i<graph.totalVertices;i++){
                for (int j=0;j<graph.totalVertices;j++){
                    if(graph.distance[j][i] != 0) {
                        graph.distance[i][j] = graph.distance[j][i];
                        continue;
                    }
                    double dis = calDis(graph.vertexList.get(i).x,graph.vertexList.get(i).y,graph.vertexList.get(j).x,graph.vertexList.get(j).y);
                    graph.distance[i][j] = dis;
                }
            }
        }catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }
        return graph;
    }
    public static double calDis(int x1,int y1,int x2,int y2){
        //hàm tính khoảng cách
        double n;
        n = Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
        return n;
    }
}
