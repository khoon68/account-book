package aa.account_book.controller;



import aa.account_book.Session.SessionManager;
import aa.account_book.domain.User;
import aa.account_book.dto.LoginForm;
import aa.account_book.dto.RegisterForm;
import aa.account_book.dto.ResponseWrapper;
import aa.account_book.Session.SessionConst;
import aa.account_book.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final SessionManager sessionManager;

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> register(
            @Valid @RequestBody RegisterForm form,
            BindingResult bindingResult
    ) {

        ResponseWrapper resWrapper = new ResponseWrapper();

        if(bindingResult.hasErrors()) {
            resWrapper.setMessage("비어있는 칸이 있습니다.");
            return new ResponseEntity<>(resWrapper, HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User(
                    form.getUserId(),
                    form.getPassword(),
                    form.getName(),
                    0
            );
            userService.registerUser(user);
            return new ResponseEntity<>(resWrapper, HttpStatus.CREATED);
        } catch(Exception e) {
            resWrapper.setMessage(e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(
            @Valid @RequestBody LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest req
    ) {
        ResponseWrapper resWrapper = new ResponseWrapper();

        if(bindingResult.hasErrors()) {
            resWrapper.setMessage("비어있는 칸이 있습니다.");

            return new ResponseEntity<>(resWrapper, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.login(form.getUserId(), form.getPassword());
        if(loginUser == null) {
            resWrapper.setMessage("일치하는 계정 정보를 찾을 수 없습니다.");
            return new ResponseEntity<>(resWrapper, HttpStatus.CONFLICT);
        }

        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(30 * 60);
        session.setAttribute(SessionConst.LOGIN_SESSION, loginUser);
        sessionManager.addSession(loginUser.getUserId(), session);

        return new ResponseEntity<>(resWrapper, HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseWrapper> logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if(session != null) {
            User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);
            sessionManager.removeSession(loginUser.getUserId());
            session.invalidate();
        }

        ResponseWrapper resWrapper = new ResponseWrapper();
        return new ResponseEntity<>(resWrapper, HttpStatus.OK);
    }

    @GetMapping("/login/state")
    public ResponseEntity<ResponseWrapper<Boolean>> showAllUserLoginState(HttpServletRequest req) {
        ResponseWrapper<Boolean> resWrapper = new ResponseWrapper<>();
        List<String> loginUserIdList = sessionManager.getLoginUserIdList();

        HttpSession session = req.getSession(false);

        if(session != null && session.getAttribute(SessionConst.LOGIN_SESSION) != null) {
            User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);
            if(loginUserIdList.contains(loginUser.getUserId())) resWrapper.setData(true);
            else resWrapper.setData(false);
        } else resWrapper.setData(false);

        return new ResponseEntity<>(resWrapper, HttpStatus.OK);
   }


}
