import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        final String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);

        final String dbPath = "jdbc:mysql://localhost:3306/note_page?serverTimezone=UTC&useSSL=false";
        Connection conn = DriverManager.getConnection(dbPath, "root", "rooot");

        Statement statement = conn.createStatement();
        final String sqlQuery = "SELECT * FROM user";
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        String cityName = null;
        String cityPopulation ;
        while(resultSet.next()) {
            cityName = resultSet.getString("user_id");
            cityPopulation = resultSet.getString("user_name");
            System.out.println(cityName + " " + cityPopulation);
        }

        if(conn != null) {
            conn.close();
        }

    }
}