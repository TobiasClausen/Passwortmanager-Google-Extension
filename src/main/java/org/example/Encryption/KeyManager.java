package org.example.Encryption;


import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Enumeration;

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class KeyManager {
    private  String keystoreFile;
    private static final char[] PASSWORD = "password".toCharArray();
    private static final String ALIAS = "alias";
    private KeyStore keyStore;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public KeyManager(String keystoreFile){
        this.keystoreFile = keystoreFile;
    }

    public KeyPair keyGenerator() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            System.out.println("neu keys erstellt");
            return keygen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveKeys(KeyPair keyPair) {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, PASSWORD);

            X509Certificate cert = generateSelfSignedCertificate(keyPair);

            KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new Certificate[]{cert});
            KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(PASSWORD);
            keyStore.setEntry(ALIAS, privateKeyEntry, protParam);

            try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
                keyStore.store(fos, PASSWORD);
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | OperatorCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair loadKeys() {
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fis, PASSWORD);

            if (keyStore.containsAlias(ALIAS)) {
                Key key = keyStore.getKey(ALIAS, PASSWORD);
                if (key instanceof PrivateKey) {
                    Certificate cert = keyStore.getCertificate(ALIAS);
                    PublicKey publicKey = cert.getPublicKey();
                    return new KeyPair(publicKey, (PrivateKey) key);
                } else {
                    throw new RuntimeException("Key is not a private key");
                }
            } else {
                throw new RuntimeException("Alias not found in keystore");
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException e) {
            throw new RuntimeException("Error loading key pair", e);
        }
    }

    public boolean hasfileKeys() {
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(keystoreFile), PASSWORD);
            return keyStore.containsAlias(ALIAS);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            System.out.println("Kein File Vorhanden");
            return false;
        }
    }

    private X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws OperatorCreationException, CertificateException, IOException {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 86400000L);  // Valid for 1 year
        BigInteger serialNumber = BigInteger.valueOf(now);

        X500Name issuer = new X500Name("CN=Test Certificate");
        X500Name subject = issuer;

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(issuer, serialNumber, startDate, endDate, subject, keyPair.getPublic());

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(contentSigner));
    }
}

