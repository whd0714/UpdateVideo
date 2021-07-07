package UpdateVideo.member;

import UpdateVideo.member.dto.MemberLoginDto;
import UpdateVideo.member.dto.MemberRegisterDto;
import UpdateVideo.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/auth")
    public Map memberAuth(@CurrentUser Member member) {
        Map map = new HashMap<String, Object>();
        if(member == null) {
            map.put("success", false);
            return map;
        }

        map.put("success", true);
        map.put("member", member);
        map.put("memberId", member.getId());

        return map;
    }

    @PostMapping("/api/register")
    public Map register(@Valid @RequestBody MemberRegisterDto memberRegisterDto, Errors errors , HttpServletResponse response) {
        Map map = new HashMap<String, Object>();
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

    @PostMapping("/api/login")
    public Map memberLogin(@RequestBody MemberLoginDto memberLoginDto) {
        Map map = new HashMap<String, Object>();
        memberService.memberLogin(memberLoginDto, map);

        return map;
    }
}
