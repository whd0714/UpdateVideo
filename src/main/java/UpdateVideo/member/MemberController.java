package UpdateVideo.member;

import UpdateVideo.member.dto.MemberRegisterDto;
import UpdateVideo.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @InitBinder("memberRegisterDto")
    public void registerBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberValidator);
    }

    @PostMapping("/api/register")
    public Map register(@Valid @RequestBody MemberRegisterDto memberRegisterDto, Errors errors , HttpServletResponse response) {
        System.out.println("!!!" + memberRegisterDto);
        Map map = new HashMap();
        if(errors.hasErrors()) {

            map.put("errorMessage", errors.getAllErrors().get(0).getDefaultMessage());
            map.put("success", false);
            response.setCharacterEncoding("UTF-8");
            return map;
        }

        memberService.register(memberRegisterDto);
        map.put("success", true);

        return map;
    }
}
