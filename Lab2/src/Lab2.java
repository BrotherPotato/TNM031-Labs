import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;


/*
1. Bob chooses secret primes p and q and computes n =pq.
2. Bob chooses e with gcd(e, (p - l)(q - 1)) = 1.
3. Bob computes d with de .1 (mod (p - l)(q - 1)).
4. Bob makes n and e public, and keeps p, q,d secret.
5. Alice encrypts m as c = me (mod n) and sends c to Bob.
6. Bob decrypts by computing m = cd (mod n).
 */

public class Lab2 {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // // to convert an integer b into a BigInteger
        // int b = 170;
        // BigInteger bigB = new BigInteger(String.valueOf(b));

        // // to read a string from keyboard

        // // to convert a string s into a BigInteger
        // String s = "abc";
        // BigInteger c = new BigInteger(s.getBytes());

        // System.out.println(input);

        // to convert a BigInteger back to a string
        // BigInteger a;
        // String s = new String(a.toByteArray());

        int bitlength = 100;

        BigInteger p = BigInteger.probablePrime(bitlength, new Random());
        BigInteger q = BigInteger.probablePrime(bitlength, new Random());

        BigInteger n = p.multiply(q);

        BigInteger gcdNumber = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = gcdNumber.subtract(BigInteger.ONE);

        while(!(e.gcd(gcdNumber).equals(BigInteger.ONE))){
            e.subtract(BigInteger.ONE);
        }

        BigInteger d = e.modInverse(gcdNumber);

        System.out.println("Please enter what you want to encrypt:");

        String messageString = (new BufferedReader(new InputStreamReader(System.in))).readLine();

        BigInteger messageNumber = new BigInteger(messageString.getBytes());

        BigInteger cipherNumber = messageNumber.modPow(e, n);

        System.out.println(cipherNumber);

        BigInteger decodedNumber = cipherNumber.modPow(d, n);

        String decodedMessage = new String(decodedNumber.toByteArray());

        System.out.println(decodedMessage);




    }
}
