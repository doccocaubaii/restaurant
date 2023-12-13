package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;

import java.util.List;

public class ProductToppingGroupItem {

    private List<ToppingItem> productToppings;
    private List<ToppingItem> groupToppings;

    public List<ToppingItem> getProductToppings() {
        return productToppings;
    }

    public void setProductToppings(List<ToppingItem> productToppings) {
        this.productToppings = productToppings;
    }

    public List<ToppingItem> getGroupToppings() {
        return groupToppings;
    }

    public void setGroupToppings(List<ToppingItem> groupToppings) {
        this.groupToppings = groupToppings;
    }
}
