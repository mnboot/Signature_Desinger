package com.mnboot.signaturedesinger.Crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/// HashBase is a class for generating Hash then Base64 because some hash function are limited to hex symbols (0-to-9 + a-to-f)
final public class HashBase {
    final MessageDigest digest;


    public HashBase(Hash algorithm) {
        try {
            digest = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /// convert string in to SHA256 digested then Base64
    /// @param input the string to convert
    /// @return array of byte of the `input` SHA256 then Base64
    public byte[] convertString(String input){
        byte[] hashed = this.digest.digest(input.getBytes());
        return Base64.getEncoder().encode(hashed);
    }

    /// convert array of byte to array of byte in SHA256 digested then Base64
    /// @param input the array of byte to convert
    /// @return array of byte of the `input` SHA256 then Base64
    public byte[] convertToBytes(byte[] input){
        byte[] hashed = this.digest.digest(input);
        return Base64.getEncoder().encode(hashed);
    }

    public void display(String input) throws NoSuchAlgorithmException {
        System.out.println(this.fingerprint(input));
    }

    /// show fingerprint by Hashing then Base64 then lowercase of a string; useful to verify the key has valid signature
    /// <pre>{@code
    /// HashBase hb = new HashBase(Hash.SHA256);
    ///
    /// String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCibVgN1K82X7T1Egilx+jk75fk588Dh7YsHEDT0skIQPOFrTFLBzZ37Rku1MBOH2XHtBXA+Q4NNkRiGMQzSsi7zsQYIGnRzxTgKN66CiCjk4VWlVh54ggi3fc44U5YK7UDQFu0ivaeEaZ76PM1lKQn+u2dtpkaBjA6rPsEnld1bwIDAQAB";
    ///
    /// String fingerprint = hb.HashBaseString(publickey);
    ///
    /// IO.println(fingerprint);
    /// //output: hemjvlywqg9vgkbwuqdbdbyfucmitvfixcrcbedd1xk
    ///
    /// String signature = "he";
    /// IO.println(fingerprint.startsWith(signature)); // true
    /// }
    /// </pre>
    /// @param input string from RSA public(or private) key, not including START or END
    /// @return string of hashed then Base64 then lowercased
    public String fingerprint(String input)  {
        byte[] der = Base64.getDecoder().decode(input);

        byte[] digested = digest.digest(der);

        return Base64.getEncoder().encodeToString(digested).toLowerCase();
    }


    public String fingerprint(byte[] input) {
        byte[] digested = digest.digest(input);
        return Base64.getEncoder().encodeToString(digested).toLowerCase();
    }
    public String getInnerKey(String content) {

        return content
                .replaceFirst("-----BEGIN (PUBLIC|PRIVATE) KEY-----", "")
                .replaceFirst("-----END (PUBLIC|PRIVATE) KEY-----", "")
                .replaceAll("\\s+", "");

    }
    public boolean verify_key(String content, String prefix) {

        String inner = getInnerKey(content);
        IO.println(inner);

        return fingerprint(inner).startsWith(prefix.toLowerCase());
    }
}
