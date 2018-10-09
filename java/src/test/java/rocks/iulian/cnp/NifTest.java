package rocks.iulian.cnp;

import org.junit.Test;

import static org.junit.Assert.*;

public class NifTest {

    @Test
    public void shouldBeValid() {
        assertTrue(Nif.isValid("9000123456785"));
    }

    @Test
    public void shouldBeInvalid() {
        assertFalse(Nif.isValid("9000123456780"));
    }

    @Test
    public void shouldConstructWellFormedNif() {
        Nif nif = Nif.fromString("9000123456785");
        assertEquals("9000123456785", nif.stringify());
    }

    @Test
    public void shouldRejectNifWithWrongCheckDigit() {
        Nif nif = Nif.fromString("9000123456780");
        assertNull(nif);
    }

    @Test
    public void shouldRejectIllFormedNif() {
        Nif nif = Nif.fromString("9j91301049995");
        assertNull(nif);
    }

}
