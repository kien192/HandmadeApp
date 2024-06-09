package controller;

import DAO.CartDAO;
import DAO.OrderDAO;
import DAO.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.CartItem;
import model.Order;
import model.Product;
import model.Receiver;

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
        String idOrder = orderDAO.insertOrder(userId, shippingFee, note, totalPrice, address, consigneeName);
        if (!idOrder.isEmpty()) {
            int execute = orderDAO.insertOrderDetail(idOrder, products);
        }

        objectMapper.writeValue(resp.getOutputStream(), idOrder);
    }
}
