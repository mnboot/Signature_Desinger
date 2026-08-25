package com.mnboot.signaturedesinger.Crypto;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSA implements PublicKeyEncryption {
    private static final Logger log = LogManager.getLogger(RSA.class);

    public final String TRANSFORMATION = "RSA";
    private final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(TRANSFORMATION);
    @Getter @Setter private boolean keyPairGenerated = false;
    @Getter @Setter private KeyPair keyPair;
    @Getter @Setter private PublicKey publicKey;
    @Getter @Setter private PrivateKey privateKey;

    public RSA() throws NoSuchAlgorithmException {
        try {
            // Generate a key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);

            // Keypair
            setKeyPair(keyPairGenerator.generateKeyPair());

            setPublicKey(keyPair.getPublic());
            setPrivateKey(keyPair.getPrivate());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        /*
        IO.println("-----BEGIN PRIVATE KEY-----");
        IO.println(Base64.getMimeEncoder().encodeToString(getPrivateKey().getEncoded()));
        IO.println("-----END PRIVATE KEY-----");*/
    }


    //TODO: remove this method from RSA class to another class
    public void bruteForce(String prefix) {
        prefix = prefix.toLowerCase();

        if(!prefix.matches("^[a-z0-9+/]*")){
            log.error("Invalid prefix: {}", prefix);
            return;
        }

        long attempts = 0;
        HashBase hashBase = new HashBase(Hash.SHA256);
        try {
            log.info("Attempting to find prefix for '{}'", prefix);
            while (true) {
                keyPair = keyPairGenerator.generateKeyPair();
                attempts++;


                byte[] hashbased = hashBase.convertToBytes(keyPair.getPublic().getEncoded());

                boolean equals = new String(hashbased, StandardCharsets.UTF_8).toLowerCase()
                        .startsWith(prefix);

                if (equals) {
                    setKeyPair(keyPair);
                    setPublicKey(keyPair.getPublic());
                    setPrivateKey(keyPair.getPrivate());
                    setKeyPairGenerated(true);

                    log.info("Found public key after {} attempts.", attempts);
                    log.info(new String(hashbased, StandardCharsets.UTF_8).toLowerCase());
                    log.info("RSA:");
                    log.info("-----BEGIN RSA PUBLIC KEY-----");
                    log.info(new String(Base64.getMimeEncoder().encode(getPublicKey().getEncoded()), StandardCharsets.UTF_8));
                    IO.println(new String(Base64.getMimeEncoder().encode(getPublicKey().getEncoded()), StandardCharsets.UTF_8));
                    log.info("-----END RSA PUBLIC KEY-----");
                    return;
                }

                if (attempts % 100 == 0) {
                    log.debug("Attempts: {}", attempts);
                    log.debug(new String(hashbased, StandardCharsets.UTF_8).toLowerCase());
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /// checking whether the HashBase `public` key has the right signature
    /// @param prefix prefix of signature
    /// @return `true` if prefix starts with signature, `false` otherwise
    public boolean verifyPrefix(String prefix) {
        HashBase hb = new HashBase(Hash.SHA256);
        String fingerprint = hb.fingerprint(publicKey.getEncoded());
        return fingerprint.startsWith(prefix);
    }

    @Override
    public String getStringPublicKeyPem() {
        return """
                -----BEGIN PUBLIC KEY-----
                %s
                -----END PUBLIC KEY-----
                """.formatted(Base64.getMimeEncoder().encodeToString(getPublicKey().getEncoded()));
    }

    @Override
    public String getStringPrivateKeyPem() {
        return ("""
                -----BEGIN PRIVATE KEY-----
                %s
                -----END PRIVATE KEY-----
                """).formatted(Base64.getMimeEncoder().encodeToString(getPrivateKey().getEncoded()));
    }

    /// Generate new public and private keys
    @Override
    public void generateNewKeyPair() {
        setKeyPair(keyPairGenerator.generateKeyPair());
        setPublicKey(keyPair.getPublic());
        setPrivateKey(keyPair.getPrivate());
    }
}


