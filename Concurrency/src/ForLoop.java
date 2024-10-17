public class ForLoop {
    public static void main(String[] args){
        int num = 15;
        int ans = num;
        System.out.print(ans);
        for (int i = num - 1; i > 0; --i){
            System.out.print(" * " + i + " ");
            ans *= i;
        }
        System.out.println("\n"+ans);
    }
}
