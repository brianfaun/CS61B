/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3

    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        //TODO: Your code here!
        for (int row = 0; row < arr.length; row++){
            for(int col = 0; col < arr[row].length; col++){
                System.out.println(arr[row][col]);
            }
        }
    }

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int max = 0;
        //TODO: Your code here!
        for (int row = 0; row < arr.length; row++){
            for (int col = 0; col < arr[row].length; col++){
                //arr[0].length may not exist if length = 0
                if (arr[row][col] > max){
                    max = arr[row][col];
                }
            }
        }
        return max;
    }

    /**Return an array where each element is the sum of the
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        //TODO: Your code here!!
        int[] result;
        result = new int[arr.length];
        for (int row = 0; row < arr.length; row++){
            for (int col = 0; col < arr[row].length; col++){
                result[row] += arr[row][col];
                }
            }
        return result;
    }
}
