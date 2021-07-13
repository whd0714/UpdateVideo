package UpdateVideo.member;

import UpdateVideo.member.dto.MemberLoginDto;
import UpdateVideo.member.dto.MemberRegisterDto;
import UpdateVideo.member.validator.MemberValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Result memberAuth(@CurrentUser Member member) {
        System.out.println("/api/auth");
        System.out.println("!!!" + member);
        if(member == null) {
            return new Result();
        }

        MemberAuthDto memberAuthDto = new MemberAuthDto(member);
        Result result = new Result(memberAuthDto);
        System.out.println("!!!" + result);
        return result;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T> {
        private T data;
        private boolean success;

        public Result(T data) {
            this.data = data;
            success = true;
        }
    }

    @Data
    static class MemberAuthDto {
        private Long memberId;
        private String name;
        private String email;


        public MemberAuthDto(Member member) {
            this.memberId = member.getId();
            this.name = member.getName();
            this.email = member.getEmail();
        }

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

    @GetMapping("/api/logout")
    public Map logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        Map map = new HashMap<String, Object>();
        map.put("success", true);
        return map;
    }
}
