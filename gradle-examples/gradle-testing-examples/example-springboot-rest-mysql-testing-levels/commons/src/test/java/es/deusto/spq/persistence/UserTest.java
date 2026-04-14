package es.deusto.spq.persistence;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void addMessage_setsBidirectionalLinkAndAddsToSet() {
        User user = new User("bob", "pwd");
        Message message = new Message("hello");
        user.addMessage(message);
        assertThat(user.getMessages()).contains(message);
        assertThat(message.getUser()).isEqualTo(user);
    }

    @Test
    public void removeMessage_unsetsUserAndRemovesFromSet() {
        User user = new User("bob", "pwd");
        Message message = new Message("hello");
        user.addMessage(message);
        user.removeMessage(message);
        assertThat(user.getMessages()).doesNotContain(message);
        assertThat(message.getUser()).isNull();
    }
}
