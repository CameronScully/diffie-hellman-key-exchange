import java.math.BigInteger;
import java.security.SecureRandom;

class PQG{
    BigInteger p;
    BigInteger q;
    BigInteger g;

    public PQG(){
        //generate and print pqg
        SecureRandom random = new SecureRandom();
        
        while(true){
            q = BigInteger.probablePrime(100, random);
            p = q.multiply(BigInteger.valueOf(2));
            p = p.add(BigInteger.valueOf(1));
            if(p.isProbablePrime(1)){  
                break;
            }
        }

        //g=h^2modp
        BigInteger h = new BigInteger(100, random);

        while(h.compareTo(p.subtract(BigInteger.valueOf(2))) == 1 || h.compareTo(BigInteger.valueOf(2)) == -1){
            h = new BigInteger(100, random);
        }

        g = h.modPow(BigInteger.valueOf(2), p);

        //display
        System.out.println("p: "+p);
        System.out.println("q: "+q);
        System.out.println("g: "+g);
    }

    public BigInteger getQ(){
        return q;
    }

    public BigInteger getG(){
        return g;
    }

    public BigInteger getP(){
        return p;
    }
}

class Entity{
    BigInteger sk;
    BigInteger pk;
    BigInteger y;
    PQG pqg;

    public Entity(PQG pqg){
        //generate private key and generate and print public key
        //pick a random number a between 1 and q-1 as alices private key
        SecureRandom random = new SecureRandom();

        //g=h^2modp
        sk = new BigInteger(100, random);

        while(sk.compareTo(pqg.getP().subtract(BigInteger.valueOf(2))) == 1 || sk.compareTo(BigInteger.valueOf(2)) == -1){
            sk = new BigInteger(100, random);
        }

        //calculate public key ya = g^a(modp)
        pk = pqg.getG().modPow(sk, pqg.getP());

        //display
        System.out.println("pk: "+pk);

        pqg = this.pqg;
    }

    public BigInteger getPK(){
        return pk;
    }

    public BigInteger getSK(){
        return sk;
    }
}

class Solution1{
    public static void main(String args[]){
        //generate p and q such that p = 2q + 1. (That is, p is a safe prime.)
        PQG pqg = new PQG();

        //generate alice's private key and public key
        System.out.print("Alice's ");
        Entity alice = new Entity(pqg);

        //generate bob's private key and public key
        System.out.print("Bob's ");
        Entity bob = new Entity(pqg);

        //show how Alice and Bob can calculate the common Diffie-Hellman key.
        System.out.println("Bob will calculate the Diffie-Hellman key as follows: ");
        System.out.println("(YA)^xB(mod p)");
        System.out.println(alice.getPK().toString()+"^"+bob.getSK().toString()+"(mod "+pqg.getP()+")");
        System.out.println("= "+alice.getPK().modPow(bob.getSK(), pqg.getP()).toString());

        System.out.println("Alice will calculate the Diffie-Hellman key as follows: ");
        System.out.println("(YB)^xA(mod p)");
        System.out.println(bob.getPK().toString()+"^"+alice.getSK().toString()+"(mod "+pqg.getP()+")");
        System.out.println("= "+bob.getPK().modPow(alice.getSK(), pqg.getP()).toString());
    }
}