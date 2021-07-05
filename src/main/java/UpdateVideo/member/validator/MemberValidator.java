package UpdateVideo.member.validator;

import UpdateVideo.member.MemberRepository;
import UpdateVideo.member.dto.MemberRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return MemberRegisterDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MemberRegisterDto memberRegisterDto = (MemberRegisterDto) o;
        if (memberRepository.existsByEmail(memberRegisterDto.getEmail())) {
            errors.rejectValue("email", "error.email", null, "이미 사용중인 이메일입니다.");
        }
    }
}
