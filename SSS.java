import java.awt.Point;
import java.math.BigInteger;
import java.util.Random;

public class SSS {

    private int k;
    private int n;

    private int prime;

    public SSS(int k, int n,int mod) {
        this.k = k;
        this.n = n;
        this.prime = mod;
    }

    /**
     * This method generates coefficients for polynomial of degree k - 1
     * @return coefficients 
     */
    public int[] generateCoeff() {

        Random num = new Random();
        int[] coeff = new int[k-1];
        for (int i = 0; i < k-1; i++) {
            coeff[i] = num.nextInt(prime);
        }
        return coeff;

    }

    /**
     * This method generates n shares
     * @param s the secret
     * @return n shares
     */
    public int[] shareGen(int s) {
        int[] shares = new int[n];
        int[] coeff = generateCoeff();
        for (int i = 1; i <= n; i++) {
            int sum = s;
            for (int j = 1; j <= coeff.length; j++) {
                sum += coeff[j-1] * Math.pow(i, j);
            }
            shares[i-1] = (sum % prime);
        }
        return shares;
    }

    /**
     * This method performs share reconstruction
     * @param points Array of shares to be used for reconstruction
     * @param mod The prime number selected
     * @return The reconstructed share value
     */
    public int reconstruct(Point[] points,int mod) {
    	
    	// The reconstructed share value
        BigInteger out = new BigInteger("0");
        for (int i = 0; i < points.length; i++) {
            BigInteger temp = new BigInteger(Integer.toString(points[i].y));
            
           out = out.add(temp.multiply(Lagrange(i,points,mod)));
        }
        String t = out.mod(new BigInteger(Integer.toString(mod))).toString();
        return Integer.parseInt(t);
    }

    /**
     * This method performs Lagrange's Interpolation for share construction
     * @param index Index of the current term
     * @param p Current share value
     * @param mod The prime value
     * @return result of Lagrange's Interpolation calculation
     */
    private BigInteger Lagrange(int index, Point[] p,int mod) {
        
    	// Numerator of current term
    	BigInteger prod = new BigInteger("1");
    	
    	// Denominator of current term
        BigInteger div = new BigInteger("1");;
        for (int i = 0; i < p.length; i++) {
            if (index == i)
                continue;
            div = new BigInteger(Integer.toString(p[index].x)).subtract(new BigInteger(Integer.toString(p[i].x)));                                                                                                                
            div = div.modInverse(new BigInteger(Integer.toString(mod)));
            prod = prod.multiply(new BigInteger(Integer.toString(-p[i].x)));
            prod = prod.multiply(div);
        }
        return prod;
    }
    
    /**
     * This method converts shares into (x,y) form; used in Lagrange's Interpolation
     * @param shares Shares generated by shareGen method
     * @return array of point objects containing shares in (x,y) form
     */
    public Point[] intToPoints(int[] shares) {
        Point[] shareCoords = new Point[shares.length];
        for (int i = 0; i < shares.length; i++) {
        	shareCoords[i] = new Point(i+1, shares[i]);
        }
        return shareCoords;
    }


}