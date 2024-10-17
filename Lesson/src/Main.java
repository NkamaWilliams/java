// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.Scanner;

//Methods | Functions
//Arrays -> Single Dimensional and Double Dimentional
//Create
//Change and access their elements
//Iterate through them
//Use them with functions | methods
public class Main {
    public static void main(String[] args) {
        //Homogenous Lists
        //Fixed size
        // 0 to n-1
        System.out.print("I ");
        System.out.print("am");
        System.out.println();

        System.out.println("I ");
        System.out.println("am");
        int[] scores = {1, 7, 8, 20, 9};
        scores[1] = -4;
        int n = scores.length;
        dispArr(scores);

        int x = 5;
        change(x);
        System.out.println(x);

        change(scores);
        dispArr(scores);

        int[] doubled = doubleElem(scores);
        dispArr(doubled);
        dispArr(scores);
    }

    //Signature and Body
    public static int area(int lenght, int breadth){
        int x = 5;
        int rectArea = lenght * breadth;
        return rectArea;
    }

    public static int area(int side){
        return side * side;
    }

    public static void welcome(){
        for (int i = 0; i < 10; i++){
            System.out.println("Welcome, User!");
        }
    }

    public static void evenOdd(int number){
        if (number % 2 == 0){
            System.out.println("Number is even");
        } else{
            System.out.println("Number is odd");
        }
    }

    public static void dispArr(int[] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i] + ",\t");
        }
        System.out.println();
    }

    public static void change(int x){
        x = 27;
        System.out.println(x);
    }

    public static void change(int[] arr){
        arr[0] = 20000;
    }

    public static int[] doubleElem(int[] arr){
        for (int i = 0; i < arr.length; i++){
            arr[i] = arr[i] * 2;
        }
        return arr;
    }
}

//Modularity