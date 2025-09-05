import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/estoque";
    private static final String USER = "root";      // usuário do MySQL
    private static final String PASSWORD = "123456"; // senha do MySQL

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Erro na conexão: " + e);
        }
    }
}
