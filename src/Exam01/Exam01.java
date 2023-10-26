package Exam01;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


class PhoneInfoBook {

    private String name;
    private String phoneNumber;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

class Sqlc {
    PhoneInfoBook book;
    Connection c;
    ResultSet rs;
    PreparedStatement pstm;

    Scanner scS = new Scanner(System.in);
    Scanner scI = new Scanner(System.in);

    Sqlc(PhoneInfoBook book) { //생성자
        this.book = book; // 외부 생성된 전화번호 부 동기화
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234");
        } catch (SQLException e) {
            System.out.println(" 객체 생성 중 오류 발생! (JDBC,로컬주소,DB 로그인) 정보를 확인해주세요!");
            e.printStackTrace();

        }
    }

    void insertEntity(PhoneInfoBook book) {
        String insert = "insert into phone values (?,?,?);";
        try {
            System.out.println("저장할 이름 입력해주세요");
            this.book.setName(scI.nextLine());
            System.out.println("폰번호 입력");
            this.book.setPhoneNumber(scS.nextLine());
            System.out.println("주소 입력");
            this.book.setAddress(scS.nextLine());
            pstm = c.prepareStatement(insert); // 입력받은 값 전송!
            pstm.setString(1, this.book.getName()); //이름
            pstm.setString(2, this.book.getPhoneNumber()); //폰번
            pstm.setString(3, this.book.getAddress()); //주소
            int i = pstm.executeUpdate(); //
            if (i == 1) {
                System.out.println(this.book.getName() + "의 연락처가 업데이트 되었습니다!");
            } else {
                System.out.println("업데이트 되지 않았습니다.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("중복된 이름이 이미 존재합니다. 이름뒤에 구분자를 더 넣어 시도해주세요.");
        } catch (SQLException e) {
            System.out.print("에러 내역 : ");
            e.printStackTrace();
        }
    }


    void printAllEntity() {
        String insert;
        insert = "select name, RPAD(substr(phoneNumber,1,4),8,'*' ) as phoneNumber, address from phone;";
        try {
            pstm = c.prepareStatement(insert);

            rs = pstm.executeQuery();
            boolean found = false;
            System.out.println("============================");
            while (rs.next()) {
                found = true;
                String name = rs.getString("name");
                String phoneN = rs.getString("phoneNumber");
                String add = rs.getString("address");
                System.out.println("이름 : " + name + ", 전화번호 : " + phoneN + ", 주소 : " + add);
            }
            if (!found) {
                System.out.println("표시할 정보가 더이상 없습니다!");
            }
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteRow() {
        //delete from phone where name ='?' ; */
        String insert = "delete from phone where name =?;";
        try {
            System.out.print("삭제할 이름을 입력해주세요! : ");
            String deleteName = scS.nextLine();

            pstm = c.prepareStatement(insert); // 입력받은 값 전송!
            pstm.setString(1, deleteName); //이름
            boolean ox = pstm.execute(); //
            if (!ox) {
                System.out.println(deleteName + "의 정보가 정상적으로 삭제되었습니다!");
            } else {
                System.out.println("삭제할 " + deleteName + "정보가 없습니다! ");
            }
            System.out.println("======== 나머지 연락처 ========");
            printAllEntity();
        } catch (SQLException e) {
            System.out.print("에러 내역 : ");
            e.printStackTrace();
        }
    }

    void searchName() {
        //select * from phone where name = '?';
        String insert;
        insert = "select name, RPAD(substr(phoneNumber,1,4),8,'*' ) as phoneNumber, address from phone where name = ?;";

        try {
            System.out.print("검색할 이름을 입력해주세요! : ");
            String search = scS.nextLine();
            pstm = c.prepareStatement(insert); // 입력받은 값 전송!
            pstm.setString(1, search); //이름
            rs = pstm.executeQuery();


            boolean found = false;
            System.out.println("============================");
            while (rs.next()) {
                found = true;
                String name = rs.getString("name");
                String phoneN = rs.getString("phoneNumber");
                String add = rs.getString("address");
                System.out.println("이름 : " + name + ", 전화번호 : " + phoneN + ", 주소 : " + add);
            }
            if (!found) {
                System.out.println("찾으시는 이름 : " + search + "가 없습니다!");
            }
            System.out.println("============================");
        } catch (SQLException e) {
            System.out.print("에러 내역 : ");
            e.printStackTrace();
        }
    }
}

public class Exam01 {
    public static void main(String[] args) {
        int menu;
        PhoneInfoBook book = new PhoneInfoBook();
        Sqlc app = new Sqlc(book);
        while (true) {
            System.out.println("1.입력 2.검색 3.삭제 4.출력 5.종료");
            try {
                Scanner sc = new Scanner(System.in);
                menu = sc.nextInt();
                if (menu == 1) {
                    app.insertEntity(app.book);
                } else if (menu == 2) {
                    app.searchName();
                } else if (menu == 3) {
                    app.deleteRow();
                } else if (menu == 4) {
                    app.printAllEntity();
                } else if (menu == 5) {
                    System.out.println("프로그램이 종료됩니다!");
                    break;
                } else {
                    System.out.println("잘못된 메뉴선택입니다. 다시 입력해주세요!");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력해주세요!");
            }
        }
    }
}
