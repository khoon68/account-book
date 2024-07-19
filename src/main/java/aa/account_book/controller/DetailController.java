package aa.account_book.controller;

import aa.account_book.domain.Detail;
import aa.account_book.dto.EditDetailForm;
import aa.account_book.dto.ResponseWrapper;
import aa.account_book.domain.User;
import aa.account_book.dto.AddDetailForm;
import aa.account_book.service.DetailService;
import aa.account_book.Session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class DetailController {
    @Autowired
    DetailService detailService;

    @GetMapping("/detail/today")
    public ResponseEntity<ResponseWrapper<List<Detail>>> showDetailListToday(HttpServletRequest req) {

        ResponseWrapper<List<Detail>> resWrapper = new ResponseWrapper<>();

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute(SessionConst.LOGIN_SESSION);

        try {
            List<Detail> detailListToday = detailService.findDetailListToday(user.getUserId());
            resWrapper.setMessage("none");
            resWrapper.setData(detailListToday);
            return new ResponseEntity<>(resWrapper, HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            resWrapper.setMessage(e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/detail/today/")
    public ResponseEntity<ResponseWrapper> addDetail(@RequestBody AddDetailForm form, HttpServletRequest req) {
        ResponseWrapper resWrapper = new ResponseWrapper();

        HttpSession session = req.getSession(false);
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);

        Detail detail = new Detail(loginUser.getUserId(), LocalDate.now(), LocalTime.now(), form.getType(), form.getDetail(), form.getAmount());
        try {
            detailService.addDetail(detail);
            return new ResponseEntity<>(resWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            resWrapper.setMessage(e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/detail/today/edit")
    public ResponseEntity<ResponseWrapper> editDetail(
        @RequestBody EditDetailForm form
    ) {
        ResponseWrapper resWrapper = new ResponseWrapper();
        Detail detail = new Detail(form.getIndex(), form.getUserId(), form.getDate(), form.getTime(),form.getType(), form.getDetail(), form.getAmount());

        try {
            detailService.editDetail(detail);
            return new ResponseEntity<>(resWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            resWrapper.setMessage(e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/month/{yearMonth}")
    public ResponseEntity<ResponseWrapper<List<Detail>>> monthlyDetailList(
            @PathVariable("yearMonth") String yearMonth,
            HttpServletRequest req
    ) {
        HttpSession session = req.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);
        ResponseWrapper<List<Detail>> resWrapper = new ResponseWrapper<>();

        try {
            resWrapper.setData(
                    detailService.findDetailListByMonth(LocalDate.parse(yearMonth + "-01"), loginUser.getUserId())
            );
            return new ResponseEntity<>(resWrapper, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            resWrapper.setMessage(e.getClass().toString() + "/n" + e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

