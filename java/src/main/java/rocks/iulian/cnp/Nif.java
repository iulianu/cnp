package rocks.iulian.cnp;

import java.math.BigInteger;

/**
 * A NIF is kind of a CNP but without inner structure
 * and with the leading digit '9'.
 * The CNP check digit rules apply.
 */
public class Nif extends RomanianPersonalNumber {

    /** Digits without leading '9' and without final check digit */
    private String digits11;

    public Nif(String digits11) {
        this.digits11 = digits11;
    }

    public static boolean isValid(String digits) {
        return fromString(digits) != null;
    }

    public static Nif fromString(String digits) {
        if (digits.length() != 13) {
            return null;
            // throw new IllegalArgumentException("Digits don't represent a NIF");
        }
        if(!isNumeric(digits)) {
            return null;
            //throw new IllegalArgumentException("Digits don't represent a NIF");
        }
        if(digits.charAt(0) != '9') {
            return null;
            //throw new IllegalArgumentException("Digits don't represent a NIF");
        }
        if( checkDigit(digits.substring(0, 12)) != digits.charAt(12) ) {
            return null;
            //throw new IllegalArgumentException("Check digit does not match");
        }
        return new Nif(digits.substring(1, 12));
    }

    @Override
    protected String stringify12() {
        return "9" + this.digits11;
    }

    @Override
    public String toString() {
        return "NIF: " + stringify();
    }

    private static boolean isNumeric(String s) {
        try {
            new BigInteger(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
