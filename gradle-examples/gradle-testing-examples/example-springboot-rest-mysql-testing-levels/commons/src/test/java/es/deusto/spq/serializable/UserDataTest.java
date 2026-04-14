package es.deusto.spq.serializable;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDataTest {

    @Test
    public void gettersAndSetters_work() {
        UserData ud = new UserData();
        ud.setLogin("joe");
        ud.setPassword("secret");
        assertThat(ud.getLogin()).isEqualTo("joe");
        assertThat(ud.getPassword()).isEqualTo("secret");
    }
}
