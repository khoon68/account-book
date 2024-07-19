package aa.account_book.controller;

import aa.account_book.dto.ResponseWrapper;
import aa.account_book.Session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<ResponseWrapper<Boolean>> getUserSessionStatus(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        ResponseWrapper<Boolean> resWrapper = new ResponseWrapper<>();
        if(session == null || session.getAttribute(SessionConst.LOGIN_SESSION) == null) resWrapper.setData(false);
        else resWrapper.setData(true);

        return new ResponseEntity<>(resWrapper, HttpStatus.OK);
    }
}
