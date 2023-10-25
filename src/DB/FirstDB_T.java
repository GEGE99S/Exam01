package DB;

import java.sql.*;
import java.util.Scanner;


class Data  // statement에 들어갈 변수들
{
    private String branchName;
    private String citiName;
    private String phoneNum;
    private int estaDate;
    private int workerNum;
    private int moneyNum;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCitiName() {
        return citiName;
    }

    public void setCitiName(String citiName) {
        this.citiName = citiName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getEstaDate() {
        return estaDate;
    }

    public void setEstaDate(int estaDate) {
        this.estaDate = estaDate;
    }

    public int getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(int workerNum) {
        this.workerNum = workerNum;
    }

    public int getMoneyNum() {
        return moneyNum;
    }

    public void setMoneyNum(int moneyNum) {
        this.moneyNum = moneyNum;
    }
}
class SQLC {//SQL 클래스에서 MySQL로 넘겨주도록  -> 연결//sql문 작성 필요
    private static Connection c; // DB에  쿼리 명령 실행할 객체
    //DriverManager : SQL Driver 연결을 위한 Manager
    //getConnection :DB에 연결시 객체를 제공 Connection
    private static PreparedStatement pstm; // 명령어 담는 객체

    ResultSet rs  = null;
    SQLC() throws SQLException {
        c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234");
    }


    void DataDelete (Data D){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("삭제할 지점명을 입력해주세요 : ");
            String name = sc.nextLine();
            ResultSet rs = pstm.getResultSet();
            // 쿼리문을 세팅하는 작업

            while (rs.next()){
               if(rs.getString("지점명").equals(name)){
                   pstm = c.prepareStatement("DELETE from 대리점 where 지점명 = (?);");
                   pstm.setString(1, name); //지점명
                   pstm.executeUpdate();
                   break;
               }else {
                   System.out.println("삭제할 지점이 없습니다!");
                   break;
               }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void DataInsert(Data d) {
        try {
            // 쿼리문을 세팅하는 작업
            pstm = c.prepareStatement("INSERT INTO 대리점 VALUES (?,?,?,?,?,?);");
            pstm.setString(1, d.getBranchName()); //지점명
            pstm.setString(2, d.getCitiName()); //도시
            pstm.setString(3, d.getPhoneNum()); //전화번호
            pstm.setInt(4, d.getWorkerNum()); // 종업원
            pstm.setInt(5, d.getMoneyNum()); // 자본금
            pstm.setInt(6, d.getEstaDate()); // 지점 개설일
            pstm.executeUpdate();
            //pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 모든 결과문 추출
    void selectAll() throws SQLException {
        String sql = "select * from 대리점;";  // 쿼리문
        pstm = c.prepareStatement(sql); //
        rs = pstm.executeQuery();
        while(rs.next()){
            String bName = rs.getString("지점명");
            String cityName = rs.getString("도시");
            String phoneNum =  rs.getString("전화번호");
            int workerNum = rs.getInt("종업원수");
            int money = rs.getInt("자본금");
            java.sql.Date estaDate = rs.getDate("지점개설일"); // Retrieve the date as a java.sql.Date
            String formattedDate = estaDate.toString();
            System.out.println(bName +" "+cityName+" "+phoneNum+" "+workerNum+" "+money+" "+formattedDate);
            System.out.println();
        }
    }


    void deleteData() throws SQLException {
        Scanner sc = new Scanner(System.in);
        pstm = c.prepareStatement("Delete from 대리점 where 지점명 = (?);"); // 날릴 쿼리 담아놓고
        System.out.println("삭제할 지점명을 입력하세요!");
        pstm.setString(1, sc.nextLine()); //지점명
        rs = pstm.executeQuery(); // 쿼리 날린 결과 값을 받은 객체

        while(rs.next()){ // 쿼리문을 받은 데이터  있다면
            String bName = rs.getString("지점명");
            String cityName = rs.getString("도시");
            String phoneNum =  rs.getString("전화번호");
            int workerNum = rs.getInt("종업원수");
            int money = rs.getInt("자본금");
            java.sql.Date estaDate = rs.getDate("지점개설일"); // Retrieve the date as a java.sql.Date
            String formattedDate = estaDate.toString();
            System.out.println(bName +" "+cityName+" "+phoneNum+" "+workerNum+" "+money+" "+formattedDate);
            System.out.println();
        }
        System.out.println("실행 끝");
    }



}
class InputClass // 정보입력 받고  d 반환
{ //- Input 클래스에서 유저에게 입력받고,
    Data valueReturn() {
        Data d = new Data();
        // 문자열 데이터 갖고있는 컬럼 // 정수갖고있는 컬럼 구분해서 스캐너로 입력
        Scanner scS = new Scanner(System.in);
        Scanner scI = new Scanner(System.in);
        System.out.println("지점명을 입력하세요");
        d.setBranchName(scS.nextLine());
        System.out.println("도시명을 입력하세요 2자리 입력해주세요~");
        d.setCitiName(scS.nextLine());
        System.out.println("전화번호를 입력하세요");
        d.setPhoneNum(scS.nextLine());
        System.out.println("종업원 수를 입력하세요");
        d.setWorkerNum(scI.nextInt());
        System.out.println("자본금을 입력하세요");
        d.setMoneyNum(scI.nextInt());
        System.out.println("개설일 입력하세요");
        d.setEstaDate(scI.nextInt());
        return d;
    }

}





public class FirstDB_T {
    public static void main(String[] args) throws SQLException {

        Scanner sc = new Scanner(System.in);
        InputClass inputClass = new InputClass();
        SQLC sqlc = new SQLC();
        ResultSet rs = null;
        while (true) {
            System.out.print("1. 입력 2.전체출력 3.지점명 검색 4.지점 삭제 5.종료 :");
            int num = sc.nextInt();
            if (num == 1) {
                sqlc.DataInsert(inputClass.valueReturn());
            } else if (num == 2) {
                sqlc.selectAll();
            } else if (num == 3) {

            } else if (num == 4) {
                sqlc.deleteData();
            } else if (num == 5) {

                break;
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

}
