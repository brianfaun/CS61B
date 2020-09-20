public class firstclass{
    static int max(int[] a){
        int final_max = 0;
        for (int i = 0; i < a.length; i++){
            if (a[i] > final_max){
                final_max = a[i];
            }
        }
        return final_max;
    }

    static boolean threeSum(int[] a){
        for (int i = 0; i < a.length; i++){
            for (int j = 0; j < a.length; j++){
                for (int k = 0; k < a.length; k++){
                    if ((a[i] + a[j] + a[k]) == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static boolean threeSumDistinct(int[] a){
        for (int i = 0; i < a.length; i++){
            for (int j = i + 1; j < a.length; j++){
                for (int k = j + 1; k < a.length; k++){
                    if ((a[i] + a[j] + a[k]) == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void main(String[] args){
        int[] tester = new int[]{8, 2, -1, -15};
        System.out.println(max(tester));
        System.out.println(threeSum(tester));
        System.out.println(threeSumDistinct(tester));
    }
}
