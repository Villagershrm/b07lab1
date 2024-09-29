import java.io.*;
public class Driver {
    public static void main(String [] args) {
        System.out.println();
        Polynomial p = new Polynomial();
        double[] d1 = {2.2, 5.4, -3};
        int[] i1 = {0, 2, 4};
        double[] d2 = {-1, 4.2, 6.9};
        int[] i2 = {0, 1, 4};
        Polynomial p1 = new Polynomial(d1, i1);
        Polynomial p2 = new Polynomial(d2, i2);
        Polynomial p3 = p1.add(p2);
        Polynomial p4 = p1.multiply(p2);
        System.out.println("P3: " + p3);
        System.out.println("P4: " + p4);
        p4.saveToFile("nomie.txt");
        try {
            File file = new File("nomie.txt");
            Polynomial p5 = new Polynomial(file);
            System.out.println("P5: " + p5);
        } catch (Exception e) {
            System.out.println("Failed to open file");
        }
        System.out.println(p4.evaluate(0));


        System.out.println(p.evaluate(3));
    }
}    