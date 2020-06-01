package Entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


import static DAO.ConnectionDB.getJDBCConnection;

public class ProductCatalog extends ProductSpecification{
    private int proCata_id;
    public void spcification(String UPC){
        try {
            Connection conn = getJDBCConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from product_specification where upc = '" + UPC + "';");
            // show data
            while (rs.next()) {
                proSpec_id = rs.getInt(1);
                price = rs.getInt(2);
                description = rs.getString(4);
            }
            // close connection
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getProCata_id() {
        return proCata_id;
    }

    public void setProCata_id(int proCata_id) {
        this.proCata_id = proCata_id;
    }
}

