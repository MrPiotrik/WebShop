package ua.edu.nung.pz.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.edu.nung.pz.dao.entity.Cart;
import ua.edu.nung.pz.dao.entity.Good;
import ua.edu.nung.pz.view.MainPage;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart/*"})
public class CartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : new Cart();

        StringBuilder context = new StringBuilder("<h2>Cart!</h2>\n");
        if (cart != null && cart.getGoods() != null) {
            context.append("<div class='container'><div class='row'>");
            for (Good good : cart.getGoods()) {
                context.append("<div class='col-12 col-sm-6 col-lg-4 col-xl-3 my-2'>")
                        .append("<div class='card'>")
                        .append("<img src='/img/").append(good.getPhoto().length > 0 ? good.getPhoto()[0] : "placeholder.jpg")
                        .append("' class='card-img-top' alt='").append(good.getName()).append("'>")
                        .append("<div class='card-body'>")
                        .append("<h5 class='card-title'>").append(good.getName()).append("</h5>")
                        .append("<p class='card-text'>Price: ").append(good.getPrice().getFor_client()).append(" UAH</p>")
                        .append("</div></div></div>");
            }
            context.append("</div></div>");
        } else {
            context.append("<div class='alert alert-warning'>Your cart is empty.</div>");
        }

        String builderPage = MainPage.Builder.newInstance()
                .setTitle("Green Shop")
                .setHeader("")
                .setBody(context.toString())
                .setFooter()
                .build()
                .getFullPage();

        out.println(builderPage);
    }
}
