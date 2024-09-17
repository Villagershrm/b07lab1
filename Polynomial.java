public class Polynomial {
    public double [] coefficients;

    public Polynomial() {
        coefficients = new double[1];
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public double evaluate(double x) {
        double sum = 0;
        for (int i = 0; i < coefficients.length; i++) {
            sum += coefficients[i] * Math.pow(x, i); 
        }
        return sum;
    }

    public Polynomial add(Polynomial other) {
        int length = Math.max(coefficients.length, other.coefficients.length);
        double[] newCoefficients = new double[length];
        for (int i = 0; i < length; i++) {
            if (i >= coefficients.length) {
                newCoefficients[i] = other.coefficients[i];
            } else if (i > other.coefficients.length) {
                newCoefficients[i] = coefficients[i];
            } else {
                newCoefficients[i] = coefficients[i] + other.coefficients[i];
            }
        }
        Polynomial newPolynomial = new Polynomial(newCoefficients);

        return newPolynomial;
    }

    public boolean hasRoot(double root) {
        return evaluate(root) == 0.0;
    }
}
