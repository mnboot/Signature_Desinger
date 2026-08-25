package com.mnboot.signaturedesinger.Crypto;

/// Hashing algorithms that are
/// supported by [Java Security Standard Algorithm Names Specification](https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#messagedigest-algorithms) <br>
/// Note that it is not 100% complete, as there are many missing algorithms, so feel free to override it
/// @see HashBase
/// @see java.security.MessageDigest#getInstance(String)
public enum Hash {
    SHA256,
    SHA512,
    MD5;


    @Override
    public String toString() {
        return switch (this){
            case SHA256 -> "SHA-256";
            case SHA512 -> "SHA-512";
            case MD5 -> "MD5";
        };
    }
}
