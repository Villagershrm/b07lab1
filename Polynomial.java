import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Polynomial {
    public double [] coefficients;
    public int [] exponents;

    public Polynomial() {
        coefficients = new double[1];
        exponents = new int[1];
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    // private int countX(String string) {
    //     int count = 0;
    //     for (int i = 0; i < string.length(); i++) {
    //         if (string.charAt(i) == 'x') {
    //             count++;
    //         }
    //     }
    //     return count;
    // }

    public Polynomial(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();

            line = line.replaceAll("-", "+-");

            if (line.charAt(0) == '+') {
                line = line.substring(1); // Remove vestigial +
            }

            String[] numbers = line.split("\\+");

            // for (String number : numbers) {
            //     System.out.println(number);
            // }

            coefficients = new double[numbers.length];
            exponents = new int[numbers.length]; 

            for (int i = 0; i < numbers.length; i++) {
                String number = numbers[i];
                if (!number.contains("x")) {
                    coefficients[i] = Double.parseDouble(number);
                    exponents[i] = 0;
                } else {
                    String[] coex = number.split("x");
                    coefficients[i] = Double.parseDouble(coex[0]);
                    exponents[i] = Integer.parseInt(coex[1]);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to open file (constructor)");
        }
    }

    public double evaluate(double x) {
        double sum = 0;
        for (int i = 0; i < coefficients.length; i++) {
            sum += coefficients[i] * Math.pow(x, exponents[i]); 
        }
        return sum;
    }

    // Counts duplicate entries in exponents and another integer array
    // Assuming every exponent is unique 
    private int countDupes(int[] otherExponents) {
        int dupes = 0;
        for (int i = 0; i < exponents.length; i++) {
            for (int j = 0; j < otherExponents.length; j++) {
                if (exponents[i] == otherExponents[j]) {
                    dupes++;
                }
            }
        }
        return dupes;
    } 

    public int indexOfExponent(int target) {
        for (int i = 0; i < exponents.length; i++) {
            if (exponents[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public Polynomial add(Polynomial other) {
        if (coefficients.length != exponents.length || other.coefficients.length != other.exponents.length) {
            System.out.println("Error: Invalid polynomial");
            return new Polynomial(); // Invalid polynomial(s)
        }
        int length = coefficients.length + other.coefficients.length - countDupes(other.exponents); //Maximum possible number of coefficients
        double[] newCoefficients = new double[length]; 
        int[] newExponents = new int[length];
        int pointer = 0; // Points to the next non-zero element in the parallel arrays
        for (int i = 0; i < other.exponents.length; i++) {
            int index = indexOfExponent(other.exponents[i]);
            if (index == -1) {
                // The exponent only exists in the other polynomial
                newExponents[pointer] = other.exponents[i];
                newCoefficients[pointer] = other.coefficients[i];
                if (newCoefficients[pointer] != 0)
                    pointer++;
            } else {
                // The exponent exists in both polynomials
                newExponents[pointer] = other.exponents[i];
                newCoefficients[pointer] = other.coefficients[i] + coefficients[index];
                if (newCoefficients[pointer] != 0)
                    pointer++;
            }
        }

        for (int i = 0; i < exponents.length; i++) {
            int index = other.indexOfExponent(exponents[i]);
            if (index == -1) {
                // The exponent only exists in this polynomial
                newExponents[pointer] = exponents[i];
                newCoefficients[pointer] = coefficients[i];
                if (newCoefficients[pointer] != 0)
                    pointer++;
            }
            // Already added duplicates in previous loop
        }

        // If the sum is 0, then returns a 0 polynomial
        if (pointer == 0) {
            return new Polynomial();
        }

        // Truncate arrays to exclude the empty elements
        double[] smallCoefficients = new double[pointer];
        int[] smallExponents = new int[pointer];
        for (int i = 0; i < pointer; i++) {
            smallCoefficients[i] = newCoefficients[i];
            smallExponents[i] = newExponents[i];
        }
        Polynomial newPolynomial = new Polynomial(smallCoefficients, smallExponents);

        return newPolynomial;
    }

    public Polynomial copy() {
        return new Polynomial(coefficients.clone(), exponents.clone());
    }

    public Polynomial multiply(Polynomial other) {
        Polynomial sum = new Polynomial();

        //for (int i = 0; i < coefficients.length; i++) {
        for (int j = 0; j < other.coefficients.length; j++) {
            Polynomial summand = this.copy();
            for (int k = 0; k < summand.coefficients.length; k++) {
                summand.coefficients[k] *= other.coefficients[j];
                summand.exponents[k] += other.exponents[j];
            }
            sum = sum.add(summand);
            //System.out.println(summand);
            //System.out.println(sum);
        }
        //}
        //System.out.println("Finished multiplying");

        return sum;
    }

    public boolean hasRoot(double root) {
        return evaluate(root) == 0.0;
    }

    public String toString() {
        String result = "";

        for (int i = 0; i < coefficients.length; i++) {
            double coefficient = coefficients[i];
            if (coefficient >= 0) {
                result += "+";
            }
            result += Double.toString(coefficient);
            if (exponents[i] != 0) {
                result += 'x';
                result += Integer.toString(exponents[i]);
            }
        }

        return result;
    }

    public void saveToFile(String fileName) {
        File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write(this.toString());

            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to load file");
        }
    }
}
