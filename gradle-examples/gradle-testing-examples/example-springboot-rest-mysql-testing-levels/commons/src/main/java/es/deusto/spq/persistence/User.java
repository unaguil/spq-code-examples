package es.deusto.spq.persistence;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    private String login;
    private String password;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();
    
    public User() {
        // Default constructor for JPA
    }
    
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public void addMessage(Message message) {
        messages.add(message);
        message.setUser(this);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        message.setUser(null);
    }

    public String getLogin() {
        return this.login;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<Message> getMessages() {
        return this.messages;
    }
     
    @Override
    public String toString() {
        StringBuilder messagesStr = new StringBuilder();
        for (Message message: this.messages) {
            messagesStr.append(message.toString()).append(" - ");
        }
        return "User: login --> " + this.login + ", password -->  " + this.password + ", messages --> [" + messagesStr + "]";
    }
}
