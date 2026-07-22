package talktodocuments.talk_to_documents.database.data.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addUser(String email, String password) {
        userRepository.save(new User(email, password));
    }

    @Transactional
    public boolean deleteUser(String email, String password) {
        User user = new User(email, password);
        return userRepository.deleteByEmailAndPassword(user.getEmail(), user.getPassword()) > 0;
    }

    public boolean validateUserPassword(String email, String password) {
        User user = new User(email, password);
        return userRepository.existsByEmailAndPassword(user.getEmail(), user.getPassword());
    }
}
