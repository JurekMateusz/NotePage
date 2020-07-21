package pl.mjurek.notepage.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {

    private static DataSource dataSource;

    public synchronized static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public synchronized static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                Context initialContext = new InitialContext();
                Context envContext = (Context) initialContext
                        .lookup("java:comp/env");
                DataSource ds = (DataSource) envContext.lookup("jdbc/notepage");
                dataSource = ds;
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        return dataSource;
    }
}