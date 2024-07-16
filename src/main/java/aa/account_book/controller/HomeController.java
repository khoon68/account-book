package aa.account_book.controller;

import aa.account_book.domain.User;
import aa.account_book.service.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Map;

//@RestController
////public class HomeController {
////    @GetMapping("/")
////    public ResponseEntity<Map<String, String>> loginHome(
////            @SessionAttribute(name=SessionConst.LOGIN_SESSION, required = false) User loginUser,
////            RequestBody req
////    ) {
////
////    }
////}
