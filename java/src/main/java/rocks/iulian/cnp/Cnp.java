package rocks.iulian.cnp;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

// TODO, CNPs starting with 9 (aka NIF), are for non-citizens.
public class Cnp {

    private Sex sex;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private int county;
    private int ordinal;
    private boolean bornAbroad;

    public Cnp(Sex sex, int birthYear, int birthMonth, int birthDay, int county, int ordinal, boolean bornAbroad) {
        if(birthYear < MIN_BIRTH_YEAR || birthYear > MAX_BIRTH_YEAR) {
            throw new IllegalArgumentException("birthYear is out of range");
        }

        if(bornAbroad && (birthYear < 1900 || birthYear > 1999)) {
            throw new IllegalArgumentException("birthYear is out of range for persons born abroad");
        }

        if(! isValidDate(birthYear, birthMonth, birthDay)) {
            throw new IllegalArgumentException("Birth date is invalid");
        }

        if(! isValidCounty(county)) {
            throw new IllegalArgumentException("County is invalid");
        }

        if(ordinal < 0 || ordinal > 999) {
            throw new IllegalArgumentException("Ordinal is out of range");
        }

        this.sex = sex;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.county = county;
        this.ordinal = ordinal;
        this.bornAbroad = bornAbroad;
    }

    public Sex getSex() {
        return sex;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public int getCounty() {
        return county;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public boolean isBornAbroad() {
        return bornAbroad;
    }


    private static boolean isValidDate(int year, int month, int day) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(String.format("%04d-%02d-%02d", year, month, day));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private static boolean isValidCounty(int county) {
        return VALID_COUNTIES.contains(county);
    }

    private static boolean isNumeric(String s) {
        try {
            new BigInteger(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static Cnp fromString(String cnpStr) {
        // TODO should collect exceptions

        if( cnpStr.length() != 13 ) {
            return null;
            //throw new CNPException( CNPException.V_FORMAT );
        }

        if( !isNumeric(cnpStr) ){
            return null;
            //throw new CNPException( CNPException.V_FORMAT );
        }

        char sexChar = cnpStr.charAt(0);
        Sex sex;
        switch (sexChar) {
            case '1':
            case '3':
            case '5':
            case '7':
                sex = Sex.MALE;
                break;
            case '2':
            case '4':
            case '6':
            case '8':
                sex = Sex.FEMALE;
                break;
            default:
                return null;
                //throw new CNPException( CNPException.V_SEX );
        }

        int century;
        switch (sexChar) {
            case '1':
            case '2':
            case '7':
            case '8':
                century = 1900;
                break;
            case '3':
            case '4':
                century = 1800;
                break;
            case '5':
            case '6':
                century = 2000;
                break;
            default:
                return null;
        }

        boolean bornAbroad = sexChar == '7' || sexChar == '8';

        int birthYear = century + Integer.parseInt(cnpStr.substring(1, 3));
        int birthMonth = Integer.parseInt(cnpStr.substring(3, 5));
        int birthDay = Integer.parseInt(cnpStr.substring(5, 7));

        if(! isValidDate(birthYear, birthMonth, birthDay)) {
            return null;
            // throw new CNPException( CNPException.V_DATA );
        }

        int county = Integer.parseInt(cnpStr.substring(7, 9));
        if(! isValidCounty(county)) {
            return null;
        }

        int ordinal = Integer.parseInt(cnpStr.substring(9, 12));

        return new Cnp(sex, birthYear, birthMonth, birthDay, county, ordinal, bornAbroad);
    }

    private String stringify12() {
        return String.format("%s%02d%02d%02d%02d%03d", sexChar(), birthYear % 100, birthMonth, birthDay, county, ordinal);
    }

    private char sexChar() {
        int century = (birthYear / 100) * 100;
        int maleDigit;
        switch(century) {
            case 1800:
                maleDigit = 3;
                break;
            case 1900:
                if(bornAbroad) {
                    maleDigit = 7;
                } else {
                    maleDigit = 1;
                }
                break;
            case 2000:
                maleDigit = 5;
                break;
            default:
                throw new RuntimeException("Should never be reached!");
        }
        if(sex == Sex.MALE) {
            return Character.forDigit(maleDigit, 10);
        } else {
            return Character.forDigit(maleDigit + 1, 10);
        }
    }

    public String stringify() {
        String str12 = stringify12();
        return str12 + checkDigit(str12);
    }

    @Override
    public String toString() {
        return "CNP: " + stringify();
    }

    private static final int CHECK_DIGIT_FACTORS[] = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};
    public static final int MIN_BIRTH_YEAR = 1800;
    public static final int MAX_BIRTH_YEAR = 2099;
    public static final List<Integer> VALID_COUNTIES = Arrays.asList(new Integer[] {
             1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46,
            51, 52
    });

    private static char checkDigit(String cnp12) {
        int v = 0;
        for(int i = 0; i < 12; i++)
            v += Character.digit(cnp12.charAt(i), 10) * CHECK_DIGIT_FACTORS[i];
        v %= 11;
        if( v == 10 )
            v = 1;
        return Character.forDigit(v, 10);
    }
}
