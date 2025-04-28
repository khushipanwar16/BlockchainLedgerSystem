package utils;

// A simple hash simulator for basic blockchain implementation
public class HashSimulator {

    // Simulate hashing a string by summing ASCII values + simple operations
    public static String hash(String input) {
        int hash = 7;
        for (int i = 0; i < input.length(); i++) {
            hash = (hash * 31 + input.charAt(i)) % 1000000007;
        }
        return Integer.toHexString(hash).toUpperCase();
    }
}
