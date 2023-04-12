package CTSP.util;

import CTSP.basic.Params;
import CTSP.benchmark.Graph;
import CTSP.benchmark.Problem;

public class util {
    /**
     * Tính khoảng cách giữa 2 điểm
     * @return
     */
    public static double calDis(double xA, double yA, double xB, double yB){
        return Math.sqrt(Math.pow(xA-xB,2)+Math.pow(yA-yB,2));
    }
    /**
     *
     * @param n
     * @return Mảng số nguyên hoán vị tăng dần có độ dài n
     */
    public static int[] randIntArray(int n){
        int[] intArray = new int[n];
        for(int i=0;i<n;i++){
            intArray[i] = i;
        }
        shuffleArray(intArray);
        return intArray;
    }

    /**
     * Hoán vị mảng nhận vào
     * @param ar
     */
    public static void shuffleArray(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = Params.rand.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    /**
     * Chuyển đổi thành mảng các số tăng dần bắt đầu từ start
     * Ví dụ: array = [4,6,2,9,1,8] và start = 2
     * Đầu ra sẽ là   [4,5,3,7,2,6]
     * @param array Mảng cần chuyển đổi
     * @param start Bắt đầu gán nhãn từ start
     * @return
     */
    public static int[] convertOrder(int[] array, int start){
        int[] newArr = new int[array.length];
        int[] cloneArr = array.clone(); //Không thao tác trên mảng ban đầu tránh thay đổi dữ liệu
        int temp = start;
        for(int j=0;j<newArr.length;j++){
            int min = Integer.MAX_VALUE;int idMin = -1;
            for(int i=0;i<cloneArr.length;i++){
                if(min > cloneArr[i] && cloneArr[i] != -1){
                    min = cloneArr[i];
                    idMin = i;
                }
            }
            newArr[idMin] = temp++;
            cloneArr[idMin] = -1;
        }
        return newArr;
    }

    /**
     * Chuyển đổi mảng ban đầu thành mảng thứ tự các phần tử được sắp xếp giảm dần
     * Ví dụ: array = [4,6,2,9,1,8] sẽ được biến đổi thành
     *                [4,6,2,1,3,5]
     * @param array
     * @return
     */
    public static int[] convertOrder2(int[] array){
        int[] newArr = new int[array.length];
        for(int i=0;i<newArr.length;i++){
            int max = 0;int idMax = -1;
            for(int j=0;j<array.length;j++){
                if(max < array[j]){
                    max = array[j];
                    idMax = j;
                    array[j] = -1;
                }
            }
            newArr[i] = idMax;
        }
        return newArr;
    }
    public static int giveId(int x, int y, Problem prob){
        int p1,p2;
        p1 = Math.min(x,y);
        p2 = Math.max(x,y);
        if(p1==0){
            return p1+p2;
        }
        return giveId(p1-1,prob.graphs.size()-1,prob)+p2-p1+1;
    }
}
