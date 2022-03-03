package cn.nicole.test;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;
import java.util.Random;

/**
 * Created by Donna on 2018/2/4.
 */

public class GeneratePk10 {

    public static String genCSR(String subject, byte[] pkdata)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            SignatureException, OperatorCreationException {
        String signalg = "SHA256WITHECDSA";
        int alglength = 256;
        String keyAlg =  "EC";
        String hexString ="";

        BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
        Security.addProvider(bouncyCastleProvider);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyAlg);
        keyGen.initialize(alglength);
        KeyPair kp = keyGen.generateKeyPair();

        byte[] heradByte = new byte[] { 48, 89, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 3, 66, 0, 4 };
        byte[] data = byteMerger(heradByte, pkdata);
        PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(
                new X500Name(subject), SubjectPublicKeyInfo.getInstance(data));
        JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder(signalg);
        ContentSigner contentSigner = jcaContentSignerBuilder.build(kp.getPrivate());
        PKCS10CertificationRequest Request = builder.build(contentSigner);
        try {
            byte[] encoded2 = Request.getEncoded();
            hexString = new String(Base64.encode(encoded2));
            System.out.println(hexString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


}
