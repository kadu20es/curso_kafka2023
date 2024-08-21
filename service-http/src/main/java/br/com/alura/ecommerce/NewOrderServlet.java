package br.com.alura.ecommerce;

import br.com.alura.ecommerce.database.LocalDatabase;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.destroy();
        orderDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            var userEmail = req.getParameter("email"); // captura os parâmetros na URL
            var orderAmount = new BigDecimal(req.getParameter("amount")); // captura os parâmetros na URL
            //var orderId = UUID.randomUUID().toString();
            var orderId = req.getParameter("uuid");
            var order = new Order(orderId, orderAmount, userEmail);

            // verifica se o id é único (nova compra)
            try (var database = new OrdersDatabase()){

                if (database.saveNew(order)){
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER",
                            userEmail,
                            new CorrelationId(NewOrderServlet.class.getSimpleName()),
                            order);

                    System.out.println("New order sent successfuly");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println("New order sent successfuly \n Your order is " + orderId);
                } else {
                    System.out.println("Old order received");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println("Old order received " + orderId);
                }
            }

        } catch (ExecutionException | InterruptedException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
