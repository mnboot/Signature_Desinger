package com.mnboot.signaturedesinger.Crypto;

public class Test {
    public static void main(String[] args) throws Exception {

        String publicKeyBase64 =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCibVgN1K82X7T1Egilx+jk75fk588Dh7YsHEDT0skIQPOFrTFLBzZ37Rku1MBOH2XHtBXA+Q4NNkRiGMQzSsi7zsQYIGnRzxTgKN66CiCjk4VWlVh54ggi3fc44U5YK7UDQFu0ivaeEaZ76PM1lKQn+u2dtpkaBjA6rPsEnld1bwIDAQAB";
        HashBase hb = new HashBase(Hash.SHA256);
        hb.display(publicKeyBase64);
        IO.println(hb.fingerprint(publicKeyBase64));

    }

}
