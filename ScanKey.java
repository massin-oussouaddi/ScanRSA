package fr.group11.rsa;

import java.math.BigInteger;

public class ScanKey {

    private BigInteger n;
    private BigInteger pow;
    public ScanKey(BigInteger n, BigInteger pow){
        this.n = n;
        this.pow = pow;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getPow() {
        return pow;
    }
}

