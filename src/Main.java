




import java.sql.*;
import java.util.Scanner;



public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "805876";


    public static void main(String[] args) throws ClassNotFoundException, SQLException {


//        loading the drivers
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("drever loaded");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

//        here we are creating connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("connection established");
            while (true) {
                System.out.println();
                System.out.println("HOTEL RESERVATION SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Exit");
                System.out.println("Choose an Option: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, sc);
                        break;
                    case 2:
                        viewReservations(connection, sc);
                        break;
                    case 3:
                        getRoomNumber(connection, sc);
                        break;
                    case 4:
                        updateReservation(connection, sc);
                        break;
                    case 5:
                        deleteReservation(connection, sc);
                        break;
                    case 6:
                        System.out.println("Have a Good Day");
                        return;
                    default:
                        System.out.println("Invalid choice, Try Again");

                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //to avoid repetations of code we pass connection and sc as an argument
    private static void reserveRoom(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter guest name");
            sc.nextLine();
            String name = sc.nextLine();
//            here we use two methods because if we use .nextLine and .nextInt method together then a space or new line character is replaced with the value of String to avoid this we use two methods.
            System.out.println("Enter Room Number");
            int roomnumber = sc.nextInt();
            System.out.println("Enter Contact Number");
            String cn = sc.next();
            String query = "INSERT INTO reservation (guest_name,room_number,contact_number) VALUES('" + name + "' , " + roomnumber + ", '" + cn + "');";

            try (Statement st = connection.createStatement()) {
                int affectedRows = st.executeUpdate(query);
//                executeUpdate method update the table row and return an integer value which is equal to number of updated rows.

                if (affectedRows > 0) {
                    System.out.println(" Reservation Successfull.");
                } else {
                    System.out.println("Reservation Failed.");
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    private static void viewReservations(Connection connection, Scanner sc) {
        String query = "SELECT * FROM reservation ";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            System.out.println("Current Reservation:");
            //  here next() method returns boolean true or false is any value present in the rs then it return true else return false.
            System.out.println("+--------------------+--------------------+--------------------+--------------------+--------------------+");
            while (rs.next()) {
                int rsid = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int roomnumber = rs.getInt("room_number");
                String cn = rs.getString("contact_number");
                String date = rs.getTimestamp("reservation_date").toString();
                System.out.println("Reservation ID: " + rsid);
                System.out.println("Guest Name: " + name);
                System.out.println("Room Number: " + roomnumber);
                System.out.println("Contact Number: " + cn);
                System.out.println("Reservation Date:" + date);
                System.out.println("+--------------------+--------------------+--------------------+--------------------+--------------------+");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private static void getRoomNumber(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter Reservation ID: ");
            int rsid = sc.nextInt();
            System.out.println("Enter Guest Name: ");
            String name = sc.next();

            String query = "select room_number from reservation where reservation_id=" + rsid + " and guest_name='" + name + "';";
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    int rn = rs.getInt("room_number");
                    System.out.println("Room Number of Guest " + name + " is: " + rn);
                } else {
                    System.out.println("Reservation not found for the given Reservation ID and Guest Name");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void updateReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter Reservation ID to Update: ");
            int rsid = sc.nextInt();
            sc.nextLine();  //it is used to use the remaning new line character of nextInt() method.
            if (!reservationExists(connection, rsid)) {
                System.out.println("Reservation Not Found for the given ID.");
                return;
            }
            System.out.println("Enter the new Guest name: ");
            String name = sc.nextLine();
            System.out.println("Enter Room number: ");
            int rm = sc.nextInt();
            sc.nextLine();  //it is used to use the remaning new line character of nextInt() method.
            System.out.println("Enter the contact Number: ");
            String cn = sc.next();


            String query = "UPDATE reservation SET guest_name='" + name + "',room_number=" + rm + ",contact_number='" + cn + "' where reservation_id=" + rsid;

            try {
                Statement st = connection.createStatement();
                int rowsAffected = st.executeUpdate(query);
                if (rowsAffected > 0) {
                    System.out.println("Reservation Update Successfully");
                } else {
                    System.out.println("Reservation Update failed");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static boolean reservationExists(Connection connection, int id) {
        try {
            String query = "SELECT reservation_id FROM reservation WHERE reservation_id=" + id + ";";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs.next();  //here next() method returns true or false if there exist any value it return true else returns false.
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    private static void deleteReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter Reservation ID to Delete: ");
            int rsid = sc.nextInt();
            sc.nextLine();  //it is used to use the remaning new line character of nextInt() method.
            if (!reservationExists(connection, rsid)) {
                System.out.println("Reservation Not Found for the given ID.");
                return;
            }
            String query = "DELETE FROM reservation WHERE reservation_id=" + rsid + ";";
            Statement st = connection.createStatement();
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Reservation Deleted Successfully");
            } else {
                System.out.println("Reservation Deletion failed");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}