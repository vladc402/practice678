package practice5;

public class ex1 {
    public static void main(String[] args) {

        double a = 2.389;
        double b = 3.1;
        double c = 17;

        double x = Math.sqrt(Math.pow(b, 3) / (b - a)) - Math.log(a);
        double y = a * Math.pow(Math.sin(c), 3) + b * Math.pow(Math.cos(c), 3);

        System.out.println("x = " + x);
        System.out.println("y = " + y);
    }
}
