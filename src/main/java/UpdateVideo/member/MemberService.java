package UpdateVideo.member;

import UpdateVideo.member.dto.MemberLoginDto;
import UpdateVideo.member.dto.MemberRegisterDto;
import javassist.Loader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(MemberRegisterDto memberRegisterDto) {
        String password = passwordEncoder.encode(memberRegisterDto.getPassword());
        Member member = new Member(memberRegisterDto.getName(), memberRegisterDto.getEmail(), password);

        memberRepository.save(member);
    }

    public void login(Member member) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserMember(member),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void memberLogin(MemberLoginDto memberLoginDto, Map map) {
        Member member = memberRepository.findByEmail(memberLoginDto.getEmail());
        if(member == null) {
            map.put("success", false);
            return;
        }

        if(passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())) {
            System.out.println("비밀번호 일치");
            map.put("success", true);
            login(member);
        } else {
            System.out.println("비밀번호 불일치");
            map.put("success", false);
            return;
        }
    }
}
