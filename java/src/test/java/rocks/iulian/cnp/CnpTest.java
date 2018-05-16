package rocks.iulian.cnp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static rocks.iulian.cnp.Sex.*;

public class CnpTest {

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
    public void shouldCreateCnpFromComponents() {
        Cnp cnp = new Cnp(MALE, 1969, 5, 9,4, 999, false);
        assertEquals("1690509049993", cnp.stringify());
    }
}
