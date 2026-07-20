package talktodocuments.talk_to_documents.database.data.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = PasswordHasher.hashPassword(password);
    }

    public void setPassword(String password) {
        this.password = PasswordHasher.hashPassword(password);
    }

    public boolean passwordEquals(String targetPassword) {
        return this.password.equals(PasswordHasher.hashPassword(targetPassword));
    }
}
