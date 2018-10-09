package rocks.iulian.cnp;

/** Either a CNP or a NIF */
public abstract class RomanianPersonalNumber {

    protected abstract String stringify12();

    public String stringify() {
        String str12 = stringify12();
        return str12 + checkDigit(str12);
    }

    public static boolean isValid(String digits13) {
        return fromString(digits13) != null;
    }

    public static RomanianPersonalNumber fromString(String digits13) {
        if(digits13.charAt(0) == '9') {
            return Nif.fromString(digits13);
        } else {
            return Cnp.fromString(digits13);
        }
    }

    private static final int CHECK_DIGIT_FACTORS[] = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};

    protected static char checkDigit(String digits12) {
        int v = 0;
        for(int i = 0; i < 12; i++)
            v += Character.digit(digits12.charAt(i), 10) * CHECK_DIGIT_FACTORS[i];
        v %= 11;
        if( v == 10 )
            v = 1;
        return Character.forDigit(v, 10);
    }
}
