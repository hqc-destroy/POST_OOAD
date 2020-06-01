package Entity;

import DAO.ConnectionDB;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static DAO.ConnectionDB.getJDBCConnection;

public class SalesLineItem {
    private int quantity;
    private int subtotal;

    public void subtotal(String UPC, String _quantity, int sale_id){
        try {
            Connection conn = getJDBCConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from product_specification where upc = '" + UPC + "';");
            while (rs.next()) {
                this.quantity = Integer.parseInt(_quantity);
                int price = rs.getInt(2);
                this.subtotal = price*quantity;
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) new ConnectionDB().getJDBCConnection();
        String sql = "insert into saleslineitem(quantity,sale_id,proSpec_id) values(?,?,?)";
        try {
            PreparedStatement pre = (PreparedStatement) conn.prepareStatement(sql);
            pre.setString(1,Integer.toString(quantity));
            pre.setString(2,Integer.toString(sale_id));
            pre.setString(3,UPC);
            int kq = pre.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    public int getSubtotal() {
        return subtotal;
    }
}
