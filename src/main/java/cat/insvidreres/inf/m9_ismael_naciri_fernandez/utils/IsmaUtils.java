package cat.insvidreres.inf.m9_ismael_naciri_fernandez.utils;

import java.math.BigInteger;

public class IsmaUtils {

    public static String bytesToHex(byte[] data) {
        BigInteger bigInteger = new BigInteger(1, data);
        String hex = bigInteger.toString(16);

        int paddingLength = (data.length * 2) - hex.length();
        if (paddingLength > 0) {
            return "0".repeat(paddingLength) + hex;
        }

        return hex;
    }
}
