import java.util.HashMap;
import java.util.Scanner;

public class FloatingPoint {

    char[] symbols;
    double[] probability;
    
    FloatingPoint(char[] symbols, double[] probability) {
        this.symbols = symbols;
        this.probability = probability;
    }

    public int getIndex(char[] input, char c) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public double Compress(String str) {
        double lower = 0.0;
        double higher = 1.0;
        double range = 1.0;
        char[] input = str.toCharArray();

        double lowrange[] = new double[probability.length];
        double highrange[] = new double[probability.length];

        for (int i = 0; i < lowrange.length; i++) {
            lowrange[i] = 0;
            highrange[i] = probability[i];
            highrange[i] = lowrange[i] + probability[i];

            for (int j = 0; j < i; j++) {
                lowrange[i] = lowrange[i] + probability[j];
            }
        }

        int IndexOfCurrentSymbol;
        for (int i = 0; i < input.length; i++) {
            IndexOfCurrentSymbol = this.getIndex(symbols, input[i]);
            lower = lower + range * lowrange[IndexOfCurrentSymbol];
            higher = lower + range * highrange[IndexOfCurrentSymbol];
            range = higher - lower;
        }
        return (higher + lower) / 2;
    }
    
    public static HashMap<Character, Double> LowRange = new HashMap<Character, Double>();
    public static HashMap<Character, Double> HighRange = new HashMap<Character, Double>();
    public static String decompress(double CompressionResult, int n) { // n->number of symbol in the string
        double Lower = 0.0;
        double Upper = 1.0;
        double Code = CompressionResult;
        String str = "";
        for (int i = 0; i < n; ++i) {
            for (char c : LowRange.keySet()) {
                if (Code > LowRange.get(c) && Code < HighRange.get(c)) { //check compression result in range
                    str += c;//store charactes
                    Lower = Lower + (Upper - Lower) * LowRange.get(c);
                    Upper = Lower + (Upper - Lower) * HighRange.get(c);
                    Code = (CompressionResult - Lower) / (Upper - Lower);
                    break;
                }
            }
        }
        return str;
    }

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        char[] symbol = {'A', 'B', 'C'};
        double[] probability = {0.8, 0.02, 0.18};
        FloatingPoint obj = new FloatingPoint(symbol, probability);

        //for compress
        System.out.print("Enter String to compress it: ");
        String str = input.next();
        double result = obj.Compress(str);
        System.out.print("Compression result: ");
        System.out.println(result);
        
        //for decompress        
        for (int i = 0; i < symbol.length; i++) {
            System.out.print("Enter Low Range of " + symbol[i] + ": ");
            double low = input.nextDouble();
            System.out.print("Enter High Range of " + symbol[i] + ": ");
            double high = input.nextDouble();
            LowRange.put(symbol[i], low);
            HighRange.put(symbol[i], high);
        }
        System.out.print("Decompression result: ");
        System.out.print(decompress(result, str.length()) + "\n");
    }
}

    /*
    A
    (0.0, 0.8)

    B
    (0.8, 0.82);

    C
    (0.82, 1.0);
     */