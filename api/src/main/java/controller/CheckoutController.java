package controller;

import DAO.CartDAO;
import DAO.OrderDAO;
import DAO.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
    OrderDAO orderDAO = new OrderDAO();
    UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String userId = req.getParameter("userId");
        String address = req.getParameter("address");
        String products = req.getParameter("productList");
        double shippingFee = Double.parseDouble(req.getParameter("shippingFee"));
        String note = req.getParameter("note");
        double totalPrice = Double.parseDouble(req.getParameter("totalPrice"));
        String consigneeName = req.getParameter("consigneeName");
        String phoneNumber = req.getParameter("phoneNumber");
        String productQuantity = req.getParameter("productQuantity");
        String idOrder = orderDAO.insertOrder(userId, shippingFee, note, totalPrice, address, consigneeName, phoneNumber);
        if (!idOrder.isEmpty()) {
            int execute = orderDAO.insertOrderDetail(idOrder, products, productQuantity);
            CartDAO cartDAO = new CartDAO();
            cartDAO.deleteCartDetail(userId);
        }

        objectMapper.writeValue(resp.getOutputStream(), idOrder);
    }
}
