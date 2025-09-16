import com.myapp.service.AuthService;
import com.myapp.service.HabitService;
import com.myapp.util.DatabaseConnection;
import java.sql.Connection;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String HapitName;
        String eamil;
        String password = "";
        System.out.println("1->SingUp");
        System.out.println("2->LoogIn");
        System.out.println("3-> Add Hapit");
        System.out.println("4->View Hapit");
        System.out.println("5->delete Hapit");
        int ans=scanner.nextInt();
        switch (ans) {
            case 1:
                System.out.println("Enter you Email"); 
                eamil=scanner.next();  
                System.out.println("Enter you Password");
                password  =scanner.next();
                try (Connection connection = DatabaseConnection.getConnection()) {
                    AuthService authService = new AuthService(connection);


                    if (authService.signUp(eamil, password)) {
                        System.out.println(" Signup success");
                    } else {
                        System.out.println(" Email already exists");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Enter you Email");
                eamil=scanner.next();
                System.out.println("Enter you Password");
                try (Connection connection = DatabaseConnection.getConnection()) {
                    AuthService authService = new AuthService(connection);
                    if (authService.login(eamil, password)) {
                        System.out.println(" Login success");
                    } else {
                        System.out.println(" Wrong email or password");
                    }}
                catch (Exception e) {
                    e.printStackTrace();
                }
            case 3:
                System.out.println("Enter your Eamil and hapit name");
                 eamil=scanner.next();
                 HapitName=scanner.next();
                try (Connection connection = DatabaseConnection.getConnection()) {
                    HabitService habitService = new HabitService(connection);
                     habitService.addHabit(HapitName, eamil);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 4:
                System.out.println("Enter your Eamil");
                eamil=scanner.next();
                try (Connection connection = DatabaseConnection.getConnection()) {
                    HabitService habitService = new HabitService(connection);
                   habitService.ViewHapits(eamil);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 5:
                System.out.println("Enter you email and hapit name");
                eamil=scanner.next();
                HapitName=scanner.next();

                try (Connection connection = DatabaseConnection.getConnection()) {
                    HabitService habitService = new HabitService(connection);
                    habitService.deleteHabit(eamil,HapitName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                System.out.println("Thans for using our APP");
        }



    }
    }
