package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FirstDB {
    private static Connection conn; // 데이터 베이스에 명령 실행
    private static PreparedStatement pstmt; // sq
    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test"
                    ,"root","1234");
                    pstmt = conn.prepareStatement("INSERT INTO 대리점 VALUES(?,?,?,?,?,?);");
            pstmt.setString(1, "강남점"); // 물음표(?)가 여기의 1~6과 매칭된다.
            pstmt.setString(2, "서울");
            pstmt.setString(3, "201-1111");
            pstmt.setInt(4, 5);
            pstmt.setInt(5, 50000000);
            pstmt.setInt(6, 19801001);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
