package utils;

public class HashSimulator {

    public static String hash(String input) {
        int hash = 7;
        for (int i = 0; i < input.length(); i++) {
            hash = (hash * 31 + input.charAt(i)) % 1000000007;
        }
        return Integer.toHexString(hash).toUpperCase();
    }
}
