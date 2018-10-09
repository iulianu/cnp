package rocks.iulian.cnp;

import org.junit.Test;

import static org.junit.Assert.*;
import static rocks.iulian.cnp.Sex.*;

public class CnpTest {

    @Test
    public void shouldBeValid() {
        assertTrue(Cnp.isValid("1690509049993"));
    }

    @Test
    public void shouldBeInvalid() {
        assertFalse(Cnp.isValid("1690509049990"));
    }

    @Test
    public void shouldParseWellKnownCnp() {
        Cnp cnp = Cnp.fromString("1690509049993");
        assertEquals(MALE, cnp.getSex());
        assertEquals(1969, cnp.getBirthYear());
        assertEquals(5, cnp.getBirthMonth());
        assertEquals(9, cnp.getBirthDay());
        assertEquals(4, cnp.getCounty());
        assertEquals(999, cnp.getOrdinal());
        assertEquals(false, cnp.isBornAbroad());
    }

    @Test
    public void shouldRejectCnpWithWrongCheckDigit() {
        Cnp cnp = Cnp.fromString("1690509049990");
        assertNull(cnp);
    }

    @Test
    public void shouldRejectCnpWithInvalidDate() {
        Cnp cnp = Cnp.fromString("1691301049995");
        assertNull(cnp);
    }

    @Test
    public void shouldRejectIllFormedCnp() {
        Cnp cnp = Cnp.fromString("1j91301049995");
        assertNull(cnp);
    }

    @Test
    public void shouldAcceptCnpWithNonstandardCounty() {
        Cnp cnp = Cnp.fromString("1690509990011");
        assertEquals(MALE, cnp.getSex());
        assertEquals(1969, cnp.getBirthYear());
        assertEquals(5, cnp.getBirthMonth());
        assertEquals(9, cnp.getBirthDay());
        assertEquals(99, cnp.getCounty());
        assertEquals(1, cnp.getOrdinal());
        assertEquals(false, cnp.isBornAbroad());
    }

    @Test
    public void shouldCreateCnpFromComponents() {
        Cnp cnp = new Cnp(MALE, 1969, 5, 9,4, 999, false);
        assertEquals("1690509049993", cnp.stringify());
    }
}
