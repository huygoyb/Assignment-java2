import java.io.SyncFailedException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choose;
        boolean exit = false;
        Scanner in = new Scanner(System.in);
        do {
            showMenu();
            choose = Integer.valueOf(scanner.nextLine());

            switch (choose) {
                case 1:
                    System.out.println("Enter Registration Information");

                    System.out.println("Enter please fullName");
                    String fullName = in.nextLine();
                    System.out.println("Enter please address");
                    String address = in.nextLine();
                    System.out.println("Enter please email");
                    String email = in.nextLine();
                    System.out.println("Enter please account");
                    String account = in.nextLine();
                    System.out.println("Enter please password");
                    String password = in.nextLine();
                    EmployeeModel model = new EmployeeModel();
                    model.setFullName(fullName);
                    model.setAddress(address);
                    model.setEmail(email);
                    model.setAccount(account);
                    model.setPassword(password);

                    boolean checkExist = checkExistAccount(account);
                    if (checkExist) {
                        String yesOrNo = "n";
                        System.out.println("Are you sure ? (Y/[N]");
                        yesOrNo = in.nextLine();
                        if (yesOrNo.toLowerCase().equals("y")) {
                            register(model);
                        }
                        break;
                    } else {
                          System.out.println("Không thể tạo tài khoản do trùng account");
                    }
                    break;
                case 2:
                    System.out.println("Enter account");
                    String accountDN = in.nextLine();
                    System.out.println("Enter password");
                    String passwordDN = in.nextLine();
                    EmployeeModel empl  = login(accountDN, passwordDN);
                    if(empl !=null){
                        System.out.println("Thông tin đăng nhập : " + "Tên " +" "+ empl.getFullName() + " "
                                + "Email " +" " + empl.getEmail() + " " + "Account " +" " + empl.getAccount());
                    }else{
                        System.out.println("Sai thông tin tài khoản ");
                    }
                    break;
                case 3:
                    System.out.println("Exited");
                    exit = true;
                    break;
                default:
                    System.out.println();
            }

        } while (choose != 3);
    }

    public static void showMenu() {
        System.out.println("...........menu.........");
        System.out.println("1.Register");
        System.out.println("2.Login");
        System.out.println("3.Exit");
        System.out.println("........................");
        System.out.print("Please choose: ");
    }

    public static PreparedStatement pre = null;

    public static boolean register(EmployeeModel emp) {
        try {
            Connection connection = ConnectionUtils.getConnection();
            String sql = "insert into employees (fullName, address, email, account, password, createdDate, status) values (?,?,?,?,?,?,?)";
            pre = connection.prepareStatement(sql);
            pre.setString(1, emp.getFullName());
            pre.setString(2, emp.getAddress());
            pre.setString(3, emp.getEmail());
            pre.setString(4, emp.getAccount());
            pre.setString(5, emp.getPassword());
            pre.setDate(6, new Date(new java.util.Date().getTime()));
            pre.setInt(7, 1);
            pre.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public static boolean checkExistAccount(String account) {
        try {
            Connection connection = ConnectionUtils.getConnection();
            String sql = "Select account from employees where account = ?";

            pre = connection.prepareStatement(sql);
            pre.setString(1, account);

            ResultSet resultSet = pre.executeQuery();
            if (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static EmployeeModel login(String account, String password) {
        EmployeeModel emp = new EmployeeModel();
        try {
            Connection connection = ConnectionUtils.getConnection();
            String sql = "Select account, email, fullName, status from employees where account = ?  AND password = ?";

            pre = connection.prepareStatement(sql);
            pre.setString(1, account);
            pre.setString(2, password);

            ResultSet resultSet = pre.executeQuery();
            if (resultSet.next()) {
                emp.setAccount(resultSet.getString(1));
                emp.setEmail(resultSet.getString(2));
                emp.setFullName(resultSet.getString(3));
                emp.setStatus(resultSet.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }
}
