package Entity;

import DAO.ConnectionDB;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Sale {
    private int sale_id;
    private String time;
    private boolean isComplete;
    private ArrayList<SalesLineItem> listItemPirce = new ArrayList<>();

    public Sale() {
        this.time = java.time.LocalDate.now().toString();
        this.isComplete = false;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.time = timestamp.toString();

        Statement statement = null;
        try {
            statement = ConnectionDB.getJDBCConnection().createStatement();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        Connection conn = (Connection) new ConnectionDB().getJDBCConnection();
        String sql = "insert into sale(time) values(?)";
        try {
            PreparedStatement pre = (PreparedStatement) conn.prepareStatement(sql);
            pre.setString(1, getTime());
            int kq = pre.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        //lay ra id
        String sql1 = "SELECT * FROM sale  ORDER BY sale_id DESC limit 1;";
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql1);
            if(rs.next()){
                setSale_id(rs.getInt(1));
                System.out.println(getSale_id());
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void makeLineItem(TextField price,TextField desc, int _price, String _desc){
        price.setText(Integer.toString(_price));
        desc.setText(_desc);
    }

    public int total(){
        int total = 0;
        for (int i = 0; i < listItemPirce.size(); i++){
            total = total+listItemPirce.get(i).getSubtotal();
        }
        return total;
    }
    public int makePayment(int tendered){
        Payment payment = new Payment();
        payment.setAmount(tendered-total());

        com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) new ConnectionDB().getJDBCConnection();
        String sql = "insert into payment(amount,sale_id) values(?,?)";
        try {
            PreparedStatement pre = (PreparedStatement) conn.prepareStatement(sql);
            pre.setInt(1,payment.getAmount());
            pre.setInt(2,getSale_id());
            int kq = pre.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return payment.getAmount();
    }

    public void setNewSale(){
        isComplete = false;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.time = timestamp.toString();

        Connection conn = (Connection) new ConnectionDB().getJDBCConnection();
        String sql = "insert into sale(time) values(?)";
        try {
            PreparedStatement pre = (PreparedStatement) conn.prepareStatement(sql);
            pre.setString(1, getTime());
            int kq = pre.executeUpdate();
            setSale_id(getSale_id()+1);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void beComeComplete(){
        this.isComplete = true;
        listItemPirce.removeAll(listItemPirce);
    }

    public String getTime() {
        return time;
    }

    public void addItemSale(SalesLineItem salesLineItem){
        this.listItemPirce.add(salesLineItem);
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public boolean isComplete() {

        return isComplete;
    }
}
