import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;


/*
1. Bob chooses secret primes p and q and computes n = pq.
2. Bob chooses e with gcd(e, (p - l)(q - 1)) = 1.
3. Bob computes d with d*e mod (p - l)(q - 1)) = 1.
4. Bob makes n and e public, and keeps p, q,d secret.
5. Alice encrypts m as c = m^e (mod n) and sends c to Bob.
6. Bob decrypts by computing m = c^d (mod n).
 */

public class Lab2 {
    public static void main(String[] args) throws Exception {
        /*************Generating keys, steps 1-4 lecture 6, slide 53*************/
        int bitlength = 100; // should be 2048 or 4096 for real use

        // 1. Generate large secrete primes p and q and compute n
        BigInteger p = BigInteger.probablePrime(bitlength, new Random());
        BigInteger q = BigInteger.probablePrime(bitlength, new Random());

        BigInteger n = p.multiply(q);   // n = pq

        // 2. Generate e so that GCD(e, (p-1)(q-1)) = 1
        BigInteger gcdNumber = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));   // GCD = (p-1)(q-1)

        BigInteger e = gcdNumber.subtract(BigInteger.ONE);  // e = GCD-1 (only to initiate e)

        // Make sure that e and GCD-number in co-prime (no common factors)
        while(!(e.gcd(gcdNumber).equals(BigInteger.ONE))){  // Subtract with one until finding biggest common divisor (common factor)
            e.subtract(BigInteger.ONE);
        }

        // 3. Compute d so that ed mod((p-1)(q-1)) = 1, move around so that d is alone
        BigInteger d = e.modInverse(gcdNumber);

        // 4. Make n and e public, those are the public keys


        /*************Encrypt message, step 5 lecture 6, slide 53*************/
        
        System.out.print("Please enter what you want to encrypt: ");   // Message to encrypts

        String messageString = (new BufferedReader(new InputStreamReader(System.in))).readLine();

        BigInteger messageNumber = new BigInteger(messageString.getBytes());

        // Encryption
        BigInteger cipherNumber = messageNumber.modPow(e, n);

        // Write out cipher, skip in reality, just send it to other person
        System.out.println("The cipher number: " + cipherNumber);

        
        /*************Decrypt message, step 6 lecture 6, slide 53*************/

        // Decryption
        BigInteger decodedNumber = cipherNumber.modPow(d, n);

        String decodedMessage = new String(decodedNumber.toByteArray());

        // Write out decrypted message        
        System.out.println("The decoded message: " + decodedMessage);
    }
}
