package UpdateVideo.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Member findByEmailAndPassword(String email, String password);

    Member findByEmail(String email);
}
