package com.mnboot.signaturedesinger.Crypto;

public interface PublicKeyEncryption {
    String getStringPublicKeyPem();
    String getStringPrivateKeyPem();
    void generateNewKeyPair();


}
