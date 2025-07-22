package org.group3.project_swp391_bookingmovieticket.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.User;

import java.io.IOException;

@WebFilter(urlPatterns = "/admin/*")
public class AdminPageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Bộ lọc được khởi tạo nếu cần
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Lấy session người dùng
        HttpSession session = request.getSession(false);

        // Kiểm tra xem người dùng đã đăng nhập và có quyền admin hay không
        if (session == null || session.getAttribute("userLogin") == null || !isAdmin(session)) {
            // Nếu không phải admin, chuyển hướng đến trang login hoặc trang thông báo lỗi
            response.sendRedirect("/home");  // Hoặc có thể là trang thông báo lỗi
            return;
        }

        // Nếu người dùng là admin, tiếp tục xử lý yêu cầu
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Bộ lọc được hủy nếu cần
    }

    // Kiểm tra nếu người dùng là admin
    private boolean isAdmin(HttpSession session) {
        // Giả sử "userLogin" là đối tượng chứa thông tin người dùng, và có phương thức getRole()
        User user = (User) session.getAttribute("userLogin");
        String role = user.getRole().getName();

        return "ADMIN".equalsIgnoreCase(role);  // Kiểm tra nếu vai trò là "ADMIN"
    }
}
