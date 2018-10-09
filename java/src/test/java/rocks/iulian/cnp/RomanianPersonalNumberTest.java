package rocks.iulian.cnp;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

public class RomanianPersonalNumberTest {

    @Test
    public void shouldCreateCnp() {
        RomanianPersonalNumber number = RomanianPersonalNumber.fromString("1690509049993");
        assertThat(number, instanceOf(Cnp.class));
    }

    @Test
    public void shouldCreateNif() {
        RomanianPersonalNumber number = RomanianPersonalNumber.fromString("9000123456785");
        assertThat(number, instanceOf(Nif.class));
    }

    @Test
    public void shouldBeValid() {
        assertTrue(RomanianPersonalNumber.isValid("1690509049993"));
    }

    @Test
    public void shouldBeInvalid() {
        assertFalse(RomanianPersonalNumber.isValid("1690509049990"));
    }
}
