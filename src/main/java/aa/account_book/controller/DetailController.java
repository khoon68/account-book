package aa.account_book.controller;

import aa.account_book.domain.Detail;
import aa.account_book.domain.ResponseWrapper;
import aa.account_book.domain.User;
import aa.account_book.dto.DetailForm;
import aa.account_book.service.DetailService;
import aa.account_book.service.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
public class DetailController {
    @Autowired
    DetailService detailService;

    @GetMapping("/detail/today")
    public ResponseEntity<ResponseWrapper<List<Detail>>> showDetailListToday(
            HttpServletRequest req
    ) {
        ResponseWrapper<List<Detail>> resWrapper = new ResponseWrapper<>();

        HttpSession session = req.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);

        try {
            List<Detail> detailListToday = detailService.findDetailListToday(loginUser.getUserId());
            resWrapper.setSuccess("yes");
            resWrapper.setMessage("none");
            resWrapper.setData(detailListToday);
            return new ResponseEntity<>(resWrapper, HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            resWrapper.setSuccess("no");
            resWrapper.setMessage(e.getClass().toString() + "/n" + e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/detail/today")
    public ResponseEntity<ResponseWrapper> addDetail(
            @RequestBody DetailForm form,
            BindingResult bindingResult
            ) {
        ResponseWrapper resWrapper = new ResponseWrapper();

        if (bindingResult.hasErrors()) {
            resWrapper.setSuccess("no");
            resWrapper.setMessage("비어있는 칸이 존재합니다.");
            return new ResponseEntity<>(resWrapper, HttpStatus.BAD_REQUEST);
        }

        Detail detail = new Detail();
        try {
            detail.setUserId(form.getUserId());
            detail.setType(form.getType());
            detail.setDetail(form.getDetail());
            detail.setAmount(form.getAmount());
            detailService.addDetail(detail);
            resWrapper.setSuccess("yes");
            return new ResponseEntity<>(resWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            resWrapper.setSuccess("no");
            resWrapper.setMessage(e.getClass().toString() + "/n" + e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/detail/today/cancel/{index}")
    public ResponseEntity<ResponseWrapper> cancelDetail(
        @PathVariable("index") int index
    ) {
        ResponseWrapper resWrapper = new ResponseWrapper();

        try {
            detailService.cancelDetailByIndex(index);
            resWrapper.setSuccess("yes");
            return new ResponseEntity<>(resWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            resWrapper.setSuccess("no");
            resWrapper.setMessage(e.getClass().toString() + "/n" + e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/month/{date}")
    public ResponseEntity<ResponseWrapper<List<Detail>>> monthlyDetailList(
            @PathVariable("date") String date,
            HttpServletRequest req
    ) {
        HttpSession session = req.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);
        ResponseWrapper<List<Detail>> resWrapper = new ResponseWrapper<>();

        try {
            resWrapper.setData(
                    detailService.findDetailListByMonth(LocalDate.parse(date + "-01"), loginUser.getUserId())
            );
            resWrapper.setSuccess("yes");
            return new ResponseEntity<>(resWrapper, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            resWrapper.setSuccess("no");
            resWrapper.setMessage(e.getClass().toString() + "/n" + e.getMessage());
            return new ResponseEntity<>(resWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

