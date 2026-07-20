package talktodocuments.talk_to_documents.database.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndPassword(String email, String password);

    long deleteByEmailAndPassword(String email, String password);
}
