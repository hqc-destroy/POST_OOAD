package Controller;

import Entity.Sale;
import Entity.ProductCatalog;
import Entity.SalesLineItem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import DAO.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BuyItemController {
    @FXML
    TextField upc, quantity, price, desc, total, tendered, balance;

    Sale sale = new Sale();

    public void EnterItem() {
        if (!sale.isComplete()) {
            Statement statement = null;
            try {
                statement = ConnectionDB.getJDBCConnection().createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            String sql = "SELECT * FROM product_specification where upc = '" + upc.getText() + "' limit 1;";
            ResultSet rs = null;
            try {
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                    ProductCatalog proCata = new ProductCatalog();
                    proCata.spcification(upc.getText());
                    sale.makeLineItem(price, desc, proCata.getPrice(), proCata.getDescription());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("NOTIFICATION");
                    alert.setHeaderText("                       Search failed!");
                    alert.setContentText("*WARNING: FBI");
                    alert.show();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else {
            total.setText("");
            balance.setText("");
            tendered.setText("");
            sale.setNewSale();
        }
    }

    public void EndSale() {
        if (!sale.isComplete()) {
            SalesLineItem salesLineItem = new SalesLineItem();
            salesLineItem.subtotal(upc.getText(), quantity.getText(), sale.getSale_id());

            sale.addItemSale(salesLineItem);
            total.setText(Integer.toString(sale.total()));
            upc.setText("");
            quantity.setText("");
            price.setText("");
            desc.setText("");
        } else{
            total.setText("");
            balance.setText("");
            tendered.setText("");
            sale.setNewSale();
        }
    }

    public void EnterTendered() throws Throwable {
        if (!sale.isComplete()) {
            int _tenderd = Integer.parseInt(tendered.getText());
            int _total = Integer.parseInt(total.getText());

            if (_tenderd < _total) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NOTIFICATION");
                alert.setHeaderText("            Khách còn thiếu " + (_total - _tenderd) + "!");
                alert.setContentText("*WARNING: FBI");
                alert.show();
            } else {
                int _balance = sale.makePayment(Integer.parseInt(tendered.getText()));
                balance.setText(Integer.toString(_balance));
                sale.beComeComplete();
            }
        } else {
            total.setText("");
            balance.setText("");
            tendered.setText("");
            sale.setNewSale();
        }
    }
}
