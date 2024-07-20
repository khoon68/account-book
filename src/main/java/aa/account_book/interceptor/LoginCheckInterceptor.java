package aa.account_book.interceptor;

import aa.account_book.Session.SessionManager;
import aa.account_book.domain.User;
import aa.account_book.dto.ResponseWrapper;
import aa.account_book.Session.SessionConst;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(
            HttpServletRequest req,
            HttpServletResponse res,
            Object handler
    ) throws IOException {

        if("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        HttpSession session = req.getSession(false);
        boolean isLogin = session != null && session.getAttribute(SessionConst.LOGIN_SESSION) != null;

        if(!isLogin) {

            if(session != null) {
                User loginUser = (User) session.getAttribute(SessionConst.LOGIN_SESSION);
                sessionManager.removeSession(loginUser.getUserId());
            }

            ResponseWrapper<Object> resWrapper = new ResponseWrapper<>();
            resWrapper.setMessage("현재 로그인 상태가 아니거나 세션이 만료되었습니다.");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(resWrapper);

            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(jsonResponse);

            return false;
        }

        session.setMaxInactiveInterval(60 * 30);

        return true;
    }
}
