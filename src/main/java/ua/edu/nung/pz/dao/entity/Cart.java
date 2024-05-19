package ua.edu.nung.pz.dao.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cart {
    private User user;
    private List<Good> goods = new ArrayList<>();

    public Cart() {
    }

    public Cart(User user, List<Good> goods) {
        this.user = user;
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public void addGood(Good good) {
        this.goods.add(good);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "user=" + user +
                ", goods=" + goods +
                '}';
    }
}
