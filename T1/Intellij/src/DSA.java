package dsapack;

import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class DSA {
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
        keyGenerator.initialize(1024);

        return keyGenerator.genKeyPair();
    }

    public static byte[] sign(DSAPrivateKey privateKey, byte[] message)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signAlgorithm = Signature.getInstance("DSA");

        signAlgorithm.initSign(privateKey);
        signAlgorithm.update(message);

        return signAlgorithm.sign();
    }

    public static boolean verify(DSAPublicKey publicKey, byte[] message, byte[] signature)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifyAlgorithm = Signature.getInstance("DSA");

        verifyAlgorithm.initVerify(publicKey);
        verifyAlgorithm.update(message);

        return verifyAlgorithm.verify(signature);
    }

    public static String privateKey2Str(DSAPrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = Base64.getEncoder().encodeToString(packed);

        Arrays.fill(packed, (byte) 0);
        return key64;
    }

    public static String publicKey2Str(DSAPublicKey pub) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec spec = fact.getKeySpec(pub, X509EncodedKeySpec.class);
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    public static DSAPublicKey Str2publicKey (String pubKey_str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(pubKey_str);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        return (DSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}