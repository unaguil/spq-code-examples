package es.deusto.spq.persistence;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    @Test
    public void constructor_setsTextAndTimestamp() {
        Message m = new Message("hi there");
        assertThat(m.getText()).isEqualTo("hi there");
        assertThat(m.getTimestamp()).isGreaterThan(0L);
    }
}
