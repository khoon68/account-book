package aa.account_book.controller;

import aa.account_book.domain.User;
import aa.account_book.dto.LoginForm;
import aa.account_book.dto.RegisterForm;
import aa.account_book.service.SessionConst;
import aa.account_book.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register-page";
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterForm form,
            BindingResult bindingResult
    ) {

        Map<String, String> res = new HashMap<>();

        if(bindingResult.hasErrors()) {
            res.put("success", "no");
            res.put("message", "비어있는 칸이 있습니다.");

            bindingResult.getFieldErrors()
                    .forEach(
                            error -> res.put(error.getField(), error.getDefaultMessage())
                    );
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User(
                    form.getUserId(),
                    form.getPassword(),
                    form.getName()
            );
            userService.registerUser(user);
            res.put("success", "yes");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch(Exception e) {
            res.put("success", "no");
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login-page";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest req
    ) {
        Map<String, String> res = new HashMap<>();
        if(bindingResult.hasErrors()) {
            res.put("success", "no");
            res.put("message", "비어있는 칸이 있습니다.");

            bindingResult.getFieldErrors().forEach(error -> res.put(error.getField(), error.getDefaultMessage()));

            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.login(form.getUserId(), form.getPassword());
        if(loginUser.getUserId().isEmpty()) {
            res.put("success", "no");
            res.put("message", "아이디 또는 비밀번호에 해당하는 계정이 없습니다.");
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        }
        HttpSession session = req.getSession();
        session.setAttribute(SessionConst.LOGIN_SESSION, loginUser);
        res.put("success", "ok");

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if(session != null) session.invalidate();

        Map<String, String> res = new HashMap<>();
        res.put("success", "yes");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
