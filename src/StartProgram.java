/*
* Course: COMP3005
* Project: Final Project Version 2
* Author: Kinjal Kamboj
* Date Created: March 25, 2024
*/

import java.util.*;
import java.sql.*;
import java.sql.Date;

public class StartProgram {

    // Global variables
    static Connection connection;
    static String currentUserName; // track the current member logged in
    static int classCount, sessionCount = 0; //count num of sessions and classes member is registered for

    public static void main(String[] args) {

        // PostgreSQL Database URL, username and password
        String url = "jdbc:postgresql://localhost:5432/COMP3005_Health_Fitness_Club_DB";
        String username = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database!");
            }

            menu(); // start looping to ask user for input
            connection.close();
        }

        catch (SQLException e) {
            System.out.println("Connection failed: SQLException");
        } catch (ClassNotFoundException e) {
            System.out.println("Connection failed: ClassNotFoundException");
        }
    }

    // Menu function to select if someone is a user, trainer, or admin staff
    public static void menu(){
        try {
            while (true){
                System.out.println("\nWelcome to Health and Fitness Club Management System! \nPlease enter a number to select your current role and 0 to exit the program.");
                System.out.println("\n(1) Current Member \n(2) Joining Member \n(3) Trainer \n(4) Administrative Staff");
                               
                Scanner scanner = new Scanner(System.in);
                int userInput = scanner.nextInt(); //store user input
                String first_name, password;
          
                if (userInput == 0){
                    System.out.println("Thank you for considering Health and Fitness Management!");
                    break;
                }

                if (userInput == 1){
                    System.out.print("Enter your first name: ");
                    first_name = scanner.next();

                    System.out.print("Enter your password: ");
                    password = scanner.next();

                    boolean val = checkLogin("Member", first_name, password);

                    if (val) {
                        currentUserName = first_name;
                        System.out.println("Redirecting to Member Portal...");
                        memberPortal();
                    }
                            
                    else{
                        System.out.println("Wrong username or password.");
                    }            
                }
                    
                else if (userInput == 2){
                    System.out.println("Redirecting to Member Registration...");
                    memberRegistration();
                }

                else if (userInput == 3){
                    System.out.print("Enter your first name: ");
                    first_name = scanner.next();

                    System.out.print("Enter your password: ");
                    password = scanner.next();

                    boolean val = checkLogin("Trainer", first_name, password);

                    if (val) {
                        currentUserName = first_name;
                        System.out.println("Redirecting to Trainer Portal...");
                        trainerPortal();
                    }
                    else{
                        System.out.println("Wrong username or password.");
                    }
                }

                else if (userInput == 4){
                    System.out.print("Enter your first name: ");
                    first_name = scanner.next();

                    System.out.print("Enter your password: ");
                    password = scanner.next();

                    boolean val = checkLogin("Admin", first_name, password);

                    if (val) {
                        currentUserName = first_name;
                        System.out.println("Redirecting to Admin Portal...");
                        adminPortal();
                    }
                    else{
                        System.out.println("Wrong username or password.");
                    }    
                }
            }
        }
        
        catch(IllegalArgumentException | InputMismatchException e){
            System.out.println("Please enter a valid number.");
        }
    }

    //check if the username and password is correct for the role type the user wishes to log in as
    public static boolean checkLogin(String type, String name, String pw) {
        try {
            String query;
            if (type.equals("Member")) {
                query = "SELECT * FROM Members WHERE first_name = ? AND passwd = ?";
            }

            else if (type.equals("Trainer")) {
                query = "SELECT * FROM Trainers WHERE first_name = ? AND passwd = ?";
            }

            else {
                query = "SELECT * FROM AdminStaff WHERE first_name = ? AND passwd = ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, pw);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return true;
            }

            else {
                return false;
            }
        }

        catch (SQLException e) {
            System.out.println("");
        }
        return false;
    }


    //MEMBER FUNCTIONS

    // Have menu displayed for member to select
    // options are for viewing, modifying their profile, and for registering for classes/sessions
    public static void memberPortal() {
        Scanner scan = new Scanner(System.in);

        try {
            while (true){
                System.out.println("\n-----MEMBER PROFILE MANAGEMENT-----");
                System.out.println(
                "Choose what you would like to do (0 to logout). \n(1) Display profile info \n(2) Update profile \n(3) Register for a Fitness Class/Personal Session \n(4) Cancel registeration for a Fitness Class/Personal Session" 
                + "\n(5) File an Equipment Report \n(6) Make Payment");
                int userInput = scan.nextInt();
                
                if (userInput == 0){
                    System.out.println("Logged out.");
                    break;
                }

                else if (userInput == 1){
                    dashBoardDisplay(currentUserName);
                }

                else if (userInput == 2){
                    updateProfile();
                }

                else if (userInput == 3){
                    registerClassSession();
                }

                else if (userInput == 4){
                    cancelClassSession();
                }

                else if (userInput == 5){
                    fileEquipmentReport();
                }

                else if (userInput == 6){
                    makePayment();
                }
            }
        }

        catch(NumberFormatException| InputMismatchException e){
            System.out.println("Please enter a valid number.");
        }
    }

    // Register a new member to the system
    public static void memberRegistration() {
        try {
            Scanner scan = new Scanner(System.in);
            // create query
            String query = "INSERT INTO Members (first_name, last_name, passwd, exercise_routine, height, current_weight, weight_goal, achieve_by_date, systolic, diastolic) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
            PreparedStatement statement = connection.prepareStatement(query); // prepare query to be edited with user
                                                                              // inputted values

            while (true) {
                System.out.println("\n-----MEMBER REGISTRATION-----");
                System.out.println("Please enter input for the the following fields...");

                System.out.println("First Name: ");
                String first_name = scan.next();
                statement.setString(1, first_name);

                System.out.println("Last Name: ");
                String last_name = scan.next();
                statement.setString(2, last_name);

                System.out.println("Password: ");
                String passwd = scan.next();
                statement.setString(3, passwd);

                scan.nextLine();

                System.out.println("Exercise Routine: ");
                String routine = scan.nextLine();
                statement.setString(4, routine);

                System.out.println("Height (m): ");
                double height = scan.nextDouble();
                statement.setDouble(5, height);

                System.out.println("Current weight (kg): ");
                double current_weight = scan.nextDouble();
                statement.setDouble(6, current_weight);
                
                System.out.println("Weight to achieve (kg): ");
                double weight_goal = scan.nextDouble();
                statement.setDouble(7, weight_goal);

                System.out.println("Date to achieve weight goal by (YYYY-MM-DD): ");
                String weight_goal_string = scan.next();
                Date weight_goal_date = Date.valueOf(weight_goal_string); // convert to date format (YYYY-MM-DD)
                statement.setDate(8, weight_goal_date);

                // insert systolic and diastolic values
                int systolic = testBloodPressure("systolic");
                statement.setInt(9, systolic);
                int diastolic = testBloodPressure("diastolic");
                statement.setInt(10, diastolic);

                // insert into the database
                int inserted = statement.executeUpdate();

                // check if inserted successfully
                if (inserted > 0) {
                    System.out.println("You have registered successfully.");
                    currentUserName = first_name;
                    memberPortal();
                } else {
                    System.out.println("Failed to register new member.");
                }
            }
        } catch (NumberFormatException | SQLException | InputMismatchException e) {
            System.out.println("Please enter a valid format for the field required.");
        }
    }

    // Update all fields in member's profile
    public static void updateProfile(){
        try {
            Scanner scan = new Scanner(System.in);
            String query = "UPDATE Members SET first_name = ?, last_name = ?, passwd = ?, join_date = ?, exercise_routine = ?, height = ?, current_weight = ?, weight_goal = ?, achieve_by_date = ?, systolic = ?, diastolic = ? WHERE first_name = ?";
            PreparedStatement statement = connection.prepareStatement(query); // prepare query to be edited with user

            while (true) {
                System.out.println("Please enter input for the the following fields...");

                System.out.println("First Name: ");
                String first_name = scan.next();
                statement.setString(1, first_name);

                System.out.println("Last Name: ");
                String last_name = scan.next();
                statement.setString(2, last_name);

                System.out.println("Password: ");
                String passwd = scan.next();
                statement.setString(3, passwd);

                System.out.println("Joining Date: ");
                String join_date = scan.next();
                Date join_date_convert = Date.valueOf(join_date);
                statement.setDate(4, join_date_convert);

                System.out.println("Exercise Routine: ");
                String routine = scan.next();
                statement.setString(5, routine);

                scan.nextLine();

                System.out.println("Height (m): ");
                double height = scan.nextDouble();
                statement.setDouble(6, height);

                System.out.println("Current weight: ");
                int current_weight = scan.nextInt();
                statement.setInt(7, current_weight);

                System.out.println("Weight to achieve: ");
                int weight_goal = scan.nextInt();
                statement.setInt(8, weight_goal);

                System.out.println("Date to achieve weight goal by (YYYY-MM-DD): ");
                String weight_goal_string = scan.next();
                Date weight_goal_date = Date.valueOf(weight_goal_string); // convert to date format (YYYY-MM-DD)
                statement.setDate(9, weight_goal_date);

                // insert systolic and diastolic values
                int systolic = testBloodPressure("systolic");
                statement.setInt(10, systolic);
                int diastolic = testBloodPressure("diastolic");
                statement.setInt(11, diastolic);

                statement.setString(12, currentUserName);

                // insert into the database
                int updated = statement.executeUpdate();

                // check if inserted successfully
                if (updated > 0) {
                    System.out.println("Profile was updated successfully.");
                    currentUserName = first_name;
                    memberPortal();
                } else {
                    System.out.println("Failed to update profile.");
                }
            }
        } catch (NumberFormatException | SQLException | InputMismatchException e) {
            System.out.println("Please enter a valid format for the field required.");
        }
    }

    // Display personal info, exercise routine, fitness achievements and health stats
    public static void dashBoardDisplay(String name) {
        try {
            System.err.println(name);
            String query = "SELECT * FROM Members WHERE first_name = ?";
            PreparedStatement statement = connection.prepareStatement(query); 
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();

            while (result.next()){
                System.out.println("-----MEMBER INFO DISPLAY-----");
                System.out.println("\nName: " + result.getString(1) + " " + result.getString(2));
                System.out.println("\nDate Joined: " + result.getString(4));
                System.out.println("\n-Fitness Goals-");
                System.out.println("Exercise Routine: " + result.getString(5));
                System.out.println("Height: " + result.getString(6) + "m");
                System.out.println("Current Weight: " + result.getDouble(7) + "kg");
                System.out.println("Desired Weight: " + result.getDouble(8) + "kg");
                System.out.println("Achieve By: " + result.getString(9));
                System.out.println("Fitness Achievement: " + result.getString(12));
                System.out.println("\n-Health Metrics ~ Blood Pressure-");
                double bmi = Math.round(result.getDouble(7)/(result.getDouble(6) * result.getDouble(6)) * 10.0) / 10.0; //round to one decimal place
                System.out.println("BMI: " + bmi);
                if (bmi > 24.9){
                    System.out.println("Your BMI indicates you are overweight!");
                }
                else if (bmi > 18.5 && bmi <= 24.9){
                    System.out.println("Your BMI indicates you are normal!");
                }

                else if (bmi <= 18.4){
                    System.out.println("Your BMI indicates you are underweight!");
                }

                System.out.println("Systolic: " + result.getInt(10));
                System.out.println("Diastolic " + result.getInt(11));

                try {
                    String classQuery = "SELECT * FROM ClassParticipants WHERE first_name = ?";
                    PreparedStatement classStatement = connection.prepareStatement(classQuery);                                                                               
                    classStatement.setString(1, name);
                    ResultSet classFound = classStatement.executeQuery();

                    String sessionQuery = "SELECT * FROM SessionParticipants WHERE first_name = ?";
                    PreparedStatement sessionStatement = connection.prepareStatement(sessionQuery);                                                                               
                    sessionStatement.setString(1, name);
                    ResultSet sessionFound = sessionStatement.executeQuery();

                    //print all classes member is registered in, if any
                    while (classFound.next()){
                        System.out.println("\n----Registered Class----");
                        try {
                                int class_num = classFound.getInt(2);
                                String classRowQuery = "SELECT * FROM SessionsAndClasses WHERE room_num = ?";
                                PreparedStatement classSearch = connection.prepareStatement(classRowQuery);
                                classSearch.setInt(1, class_num);
                                ResultSet classRowFound = classSearch.executeQuery();

                                while (classRowFound.next()) {
                                    System.out.println("Room Number: " + classRowFound.getObject(1));
                                    System.out.println("Trainer: " + classRowFound.getObject(3));
                                    System.out.println("Start Date: " + classRowFound.getObject(4));
                                    System.out.println("Start Time: " + classRowFound.getObject(5));
                                }
                            } catch (SQLException e) {
                                System.out.println("SQLException when printing Classes.");
                            }
                        }
                    
                    //print all sessions member is registered in, if any
                    while (sessionFound.next()){
                            System.out.println("\n----Registered Session----");
                            try {
                                int sess_num = sessionFound.getInt(2);
                                String sessionRow = "SELECT * FROM SessionsAndClasses WHERE room_num = ?";
                                PreparedStatement sessionSearch = connection.prepareStatement(sessionRow);
                                sessionSearch.setInt(1, sess_num);
                                ResultSet sessionRowFound = sessionSearch.executeQuery();

                                while (sessionRowFound.next()) {
                                    System.out.println("Room Number: " + sessionRowFound.getObject(1));
                                    System.out.println("Trainer: " + sessionRowFound.getObject(3));
                                    System.out.println("Start Date: " + sessionRowFound.getObject(4));
                                    System.out.println("Start Time: " + sessionRowFound.getObject(5));
                                }
                            } catch (SQLException e) {
                                System.out.println("SQLException when printing Sessions.");
                            }
                        }
                    }
                
                catch (SQLException e){
                    System.out.println("SQLException when finding classes/sessions to display");
                }
            }
        }

        catch (SQLException e) {
            System.out.println("SQL Exception in Dashboard Display");
        }
    }

    // Test a member's blood pressure and health with a Sphygmomanometer
    public static int testBloodPressure(String type) {
        System.out.println(
                "\nWelcome to the Sphygmomanometer System. \nInstructions: \n1. Roll up the sleeves on your upper arm and wrap the rubber cuff around your skin."
                        +
                        "\n2. Make sure the cuff covers around 80% of your upper arm. \n3. Avoid engaging in other activities during the 4 minute session for a more accurate reading."
                        +
                        "\nSession has started.");
        Random random = new Random();
        int measure = 90;

        switch (type) {
            case "systolic":
                measure = random.nextInt(140 - 90 + 1) + 90;

            case "diastolic":
                measure = random.nextInt(90 - 60 + 1) + 60;
        }
        
        return measure;
    }

    // Display all classes or/and sessions
    public static void displayClassesSessions(String type){
        try {
            String query = "SELECT * FROM SessionsAndClasses WHERE of_type = ?";
            PreparedStatement statement = connection.prepareStatement(query); 
            statement.setString(1, type);
            ResultSet result = statement.executeQuery();

            if (result.next()){
                if (type.equals("Class")){
                    System.out.println("---LIST OF CLASSES---");
                    classCount = 0;
                    System.out.printf("\n%-15s%-15s%-15s%-15s%-15s\n", "Room Number", "Type", "Trainer", "Start Date",
                            "Start Time");
                    System.out.println("------------------------------------------------------------------------");
                    do {
                        for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                            System.out.printf("%-15s", result.getObject(i));
                            if (i % 5 == 0) {
                                System.out.println("\n");
                            }
                        }
                        classCount++;
                }
                while (result.next());

                }
                else{
                    System.out.println("\n---LIST OF SESSIONS---");
                    sessionCount = 0;
                    System.out.printf("\n%-15s%-15s%-15s%-15s%-15s\n", "Room Number", "Type", "Trainer", "Start Date",
                            "Start Time");
                    System.out.println("------------------------------------------------------------------------");
                    do {
                        for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                            System.out.printf("%-15s", result.getObject(i));
                            if (i % 5 == 0) {
                                System.out.println("\n");
                            }
                        }
                        sessionCount++;
                    } while (result.next());
                }
            }

            else{
                System.out.println("Sorry, there are no available classes or sessions during this time, please try again later.");
            }
        }
        catch (IllegalArgumentException | SQLException e){
            System.out.println("Please enter the correct input format for (displayClassesSessions)");
        }
            
    }

    //Register for classes and sessions
    public static void registerClassSession(){
        try{
            System.out.println("Would you like to (1) Schedule a session, or (2) Register for a class?");
            Scanner scan = new Scanner(System.in);
            int userInput = scan.nextInt();

            if (userInput == 1){
                   displayClassesSessions("Session");
                   System.out.println("Enter the room number for the session you would like to register in: ");
                   int sessionRoomNum = scan.nextInt();
                   String sessionQuery = "SELECT * FROM SessionsAndClasses WHERE room_num = ?";
                   PreparedStatement sessionStatement = connection.prepareStatement(sessionQuery); 
                   sessionStatement.setInt(1, sessionRoomNum);
                   ResultSet result = sessionStatement.executeQuery();

                   if (result.next()) {
                    try{
                        //check if a member is already registered in the session the user wants to register in
                        String checkSessionQuery = "SELECT * FROM SessionParticipants WHERE room_num= ?";
                        PreparedStatement checkSessionStatement = connection.prepareStatement(checkSessionQuery);
                        checkSessionStatement.setInt(1, sessionRoomNum);
                        ResultSet checkSessionResult = checkSessionStatement.executeQuery();

                        if (checkSessionResult.next()){
                            System.out.println("A member is already registered for this session.");
                        }

                        else {
                            sessionQuery = "INSERT INTO SessionParticipants (first_name, room_num) VALUES (?, ?)";
                            sessionStatement = connection.prepareStatement(sessionQuery);
                            sessionStatement.setString(1, currentUserName);
                            sessionStatement.setInt(2, sessionRoomNum);
                            int inserted = sessionStatement.executeUpdate();
    
                          if (inserted > 0){
                            System.out.println("You have registered for the session successfully.");
                          }
                          else{
                            System.out.println("Could not successfully register for the session.");
                          }
                        }
                    }
                    catch (SQLException e){
                        System.out.println("Cannot register for the same session again");
                    }
                  } 
                  else {
                      System.out.println("Failed to register in session.");
                    }
                }
                 
                if (userInput == 2){
                   displayClassesSessions("Class");
                   System.out.println("Enter the room number for the class you would like to register in: ");
                   int classRoomNum = scan.nextInt();
                   String classQuery = "SELECT * FROM SessionsAndClasses WHERE room_num = ?";
                   PreparedStatement classStatement = connection.prepareStatement(classQuery); 
                   classStatement.setInt(1, classRoomNum);
                   ResultSet classResult = classStatement.executeQuery();

                   if (classResult.next()) {
                    try{
                      classQuery = "INSERT INTO ClassParticipants (first_name, room_num) VALUES (?, ?)";
                      classStatement = connection.prepareStatement(classQuery);
                      classStatement.setString(1, currentUserName);
                      classStatement.setInt(2, classRoomNum);
                      int inserted = classStatement.executeUpdate();

                      if (inserted > 0){
                        System.out.println("You have registered for the class successfully.");
                      }
                      else{
                        System.out.println("Could not successfully register for the class.");
                      }
                    }
                    catch (SQLException e){
                        System.out.println("Cannot register for the same class again");
                    }
                } 
                  else {
                      System.out.println("Failed to register in class.");
                  }
                }
        }
                                                                            
        catch(IllegalArgumentException | SQLException e){
            System.out.println("Please enter the correct input format (registerClassSession).");
        }  
    }

    // Cancel regsitration for classes and sessions
    public static void cancelClassSession(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Would you like to cancel registration for a (1) Session, or (2) Class?");
            int type = scan.nextInt();
            System.out.println("Enter the room number for the class/session you wish to remove your registration for: ");
            int room_num = scan.nextInt();

            if (type == 1){
                try {
                    String classQuery = "DELETE FROM SessionParticipants WHERE first_name = ? AND room_num = ?";
                    PreparedStatement classStatement = connection.prepareStatement(classQuery);
                    classStatement.setString(1, currentUserName);
                    classStatement.setInt(2, room_num);
                    int deleted = classStatement.executeUpdate();

                    if (deleted > 0){
                        System.out.println("Your session registration has been removed.");
                    }

                    else{
                        System.out.println("Your session registration could not be removed.");
                    }
                }
                catch (SQLException e){
                    System.out.println("You are not initially registered for the session specified by room number " + room_num);
                }
                
            }
            
            if (type == 2){
                try {
                    String classQuery = "DELETE FROM ClassParticipants WHERE first_name = ? AND room_num = ?";
                    PreparedStatement classStatement = connection.prepareStatement(classQuery);
                    classStatement.setString(1, currentUserName);
                    classStatement.setInt(2, room_num);
                    int deleted = classStatement.executeUpdate();

                    if (deleted > 0){
                        System.out.println("Your class registration has been removed.");
                    }

                    else{
                        System.out.println("Your class registration could not be removed.");
                    }
                }
                catch (SQLException e){
                    System.out.println("You are not initially registered for the class specified by room number " + room_num);
                }
            }
        }
        catch (IllegalArgumentException e){
            System.out.println("Please enter the correct input format (cancelClassSession).");
        }
    }

    // Have member file any reports for malfunctioned equipment
    public static void fileEquipmentReport(){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("-----EQUIPMENT REPORT PORTAL-----");
            System.out.println("Please specify the equipment you wish to report: ");
            String equipment = scan.nextLine();
            System.out.println("Please specify the room number the equipment is in: ");
            int room_num = scan.nextInt();
           
            String query = "INSERT INTO EquipmentMaintenance (equipment_name, room_num, status) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, equipment);
            statement.setInt(2, room_num);
            statement.setString(3, "Malfunctioned");
            int inserted = statement.executeUpdate();

            if (inserted > 0){
                System.out.println("Successfully reported.");
            }

            else{
                System.out.println("Could not successfully file the report.");
            }
        }
        catch (SQLException e){
            System.out.println("Thank you for reporting, we are already investigating a similar report with the specified equipment and room number.");
        }
    }

    // Allow member to make a payment of all fees relating to membership, and registering for sessions/classes
    public static void makePayment(){
        try {
            String query = "SELECT * FROM Payments WHERE first_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, currentUserName);
            ResultSet result = statement.executeQuery();
            boolean unpaidBill = false;

            while (result.next()){
                if (result.getString("status").equalsIgnoreCase("Unpaid")){
                    unpaidBill = true;
                    String updateQuery = "UPDATE Payments SET status = ? WHERE first_name = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, "Paid");
                    updateStatement.setString(2, currentUserName);
                    int update = updateStatement.executeUpdate();

                    if (update > 0){
                        System.out.println("Successfully paid outstanding bill.");
                        displayReceipt(currentUserName);
                    }

                    else{
                        System.out.println("Could not pay outstanding bill.");
                    }
                }
            }

            if (!unpaidBill){
                System.out.println("You have no bills that are to be cleared.");
            }
        }
        catch (SQLException e){
                System.out.println("You have no outstanding bills.");
        }
    }
    
    // Display the receipt of the payment member has made
    public static void displayReceipt(String name){
        try {
            String getPaymentQuery = "SELECT * FROM Payments WHERE first_name = ?";
            PreparedStatement feeStatement = connection.prepareStatement(getPaymentQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
            feeStatement.setString(1, name);
            ResultSet payment = feeStatement.executeQuery();

             //calculate fees
            double membershipFee = 40.00;
            double classFee = 23.50;
            double sessionFee = 30.00;
            double equipmentUseFee = 11.00;
            double lockerUseFee = 6.50;
            double subtotal = membershipFee + (classFee*classCount) + (sessionFee*sessionCount) + equipmentUseFee + lockerUseFee;
            double tax = 0.13;
            double total = (tax * subtotal) + subtotal;

            System.out.println("========================================");
            System.out.println("            RECEIPT DETAILS             ");
            System.out.println("========================================");
            if (payment.next()) {
                System.out.println("Receipt ID: " + payment.getObject(1));
                System.out.println("Customer Name: " + payment.getObject(3) + " " + payment.getObject(4));
                System.out.println("Membership Fee: $" + 40.00);
                System.out.println("Classes Fee:");
                for (int i = 1; i <= classCount; i++) {
                    System.out.println(i + "x " + classFee);
                }
                System.out.println("= " + classCount * classFee);
                System.out.println("Sessions Fee:");
                for (int i = 1; i <= sessionCount; i++) {
                    System.out.println(i + "x " + sessionFee);
                }
                System.out.println("= " + sessionCount * sessionFee);
                System.out.println("Equipment Use Fee: $" + equipmentUseFee);
                System.out.println("Locker Use Fee: $" + lockerUseFee);
                System.out.println("--------------------------------");
                System.out.println("\nSubtotal: " + subtotal);
                System.out.println("Tax: " + tax);
                System.out.println("Total: " + total);
                System.out.println("\nTransaction Date: " + payment.getObject(6));
            }            
        }

        catch (SQLException e){
            System.out.println("Could not display receipt of payment.");
        }
    }
    

    //TRAINER FUNCTIONS

    // trainer portal
    public static void trainerPortal() {
        Scanner scan = new Scanner(System.in);
        try {
            while (true){
                System.out.println("\n-----TRAINER PORTAL-----");
                System.out.println(
                "Choose what you would like to do (0 to logout). \n(1) Manage Schedule \n(2) Write an achievement made by a member \n(3) View a Member's Profile \n(4) View notifications");
                int userInput = scan.nextInt();
                
                if (userInput == 0){
                    System.out.println("Logged out.");
                    break;
                }

                else if (userInput == 1){
                    scheduleManagement();
                }

                else if (userInput == 2){
                    try {
                        //display all member first names
                        String memberQuery = "SELECT * FROM Members ";
                        PreparedStatement memberStatement = connection.prepareStatement(memberQuery);
                        ResultSet members = memberStatement.executeQuery();
            
                        System.out.println("-----MEMBERS-----");
                        while (members.next()){
                            System.out.println("First Name: " + members.getObject(1));
                        }
                        try {
                            System.out.println("Enter the first name of the member who you would like to write an acheievemt about: ");
                            Scanner scanner = new Scanner(System.in);
                            String name = scanner.nextLine();

                            System.out.println("Write what notable fitness goal have they achieved: ");
                            Scanner scanner2 = new Scanner(System.in);
                            String achievement = scanner2.nextLine();

                            String updateAchievementQuery = "UPDATE Members SET fitness_achievement = ? WHERE first_name = ?";
                            PreparedStatement updateAchievementStatement = connection.prepareStatement(updateAchievementQuery);
                            updateAchievementStatement.setString(1, achievement);
                            updateAchievementStatement.setString(2, name);
                            int update = updateAchievementStatement.executeUpdate();
                            if (update > 0){
                                System.out.println("Successfully set member's achievement.");
                            }
                        }
                        catch (IllegalArgumentException e){
                            System.out.println("Please enter a string value/An existing member's name.");
                        }
                    }
                    catch(SQLException e){
                        System.out.println("Please enter a string value/An existing member's name.");
                    }
                }

                else if (userInput == 3){
                    try {
                        //display all member first names
                        String memberQuery = "SELECT * FROM Members ";
                        PreparedStatement memberStatement = connection.prepareStatement(memberQuery);
                        ResultSet members = memberStatement.executeQuery();
            
                        System.out.println("-----MEMBERS-----");
                        while (members.next()){
                            System.out.println("First Name: " + members.getObject(1));
                        }
                        try {
                            System.out.println("Enter the first name of the member whose profile you would like to view: ");
                            Scanner scanner = new Scanner(System.in);
                            String name = scanner.nextLine();
                            dashBoardDisplay(name);
                        }
                        catch (IllegalArgumentException e){
                            System.out.println("Please enter a string value.");
                        }
                    }
                    catch(SQLException e){
                        System.out.println("No members available to display.");
                    }
                }

                else if (userInput == 4){
                    try {
                        //display all of trainer's notifications
                        String notificationQuery = "SELECT * FROM TrainerNotifications WHERE tfirst_name = ?";
                        PreparedStatement notificationStatement = connection.prepareStatement(notificationQuery);
                        notificationStatement.setString(1, currentUserName);
                        ResultSet notifications = notificationStatement.executeQuery();
            
                        System.out.println("-----NOTIFICATIONS-----");
                        while (notifications.next()){
                            System.out.println("An admin has modified/created an event assigning you to room number " + notifications.getObject(2) + 
                            " on " + notifications.getObject(3));
                        }
                        //delete notification
                        String deleteQuery = "DELETE FROM TrainerNotifications WHERE tfirst_name = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, currentUserName);
                        deleteStatement.executeUpdate();
                    }
                    catch(SQLException e){
                        System.out.println("No notifications to display.");
                    }
                }
                    
            }
        }
        catch(NumberFormatException| InputMismatchException e){
            System.out.println("Please enter a valid number.");
        }
    }

    // Allow trainers to schedule sessions and classes
    public static void scheduleManagement() {
        try {
            System.out.println("-----SCHEDULE MANAGEMENT-----");
            System.out.println("Would you like to (1) insert classes/sessions, (2) delete your classes/sessions or (3) modify your existing classes or sessions?");

            Scanner scanner = new Scanner(System.in);
            int userInput = scanner.nextInt();
            int room_num;

            if (userInput == 1){
                try {
                    displayClassesSessions("Class");
                    displayClassesSessions("Session");

                    String updateQuery = "INSERT INTO SessionsAndClasses (room_num, of_type, tfirst_name, start_date, start_time) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    System.out.println("Enter the unique room number of the class/session you would like to insert: ");
                    Scanner scan = new Scanner(System.in);
                    room_num = scan.nextInt();

                    System.out.println("Enter the type of event (Class/Session): ");
                    Scanner scan2 = new Scanner(System.in);
                    String type = scan2.nextLine();

                    System.out.println("Enter the start date (YYYY-MM-DD): ");
                    Scanner scan3 = new Scanner(System.in);
                    String date = scan3.nextLine();
                    Date date_convert = Date.valueOf(date);

                    System.out.println("Enter the start time: ");
                    Scanner scan4 = new Scanner(System.in);
                    String time = scan4.nextLine();
                    Time time_convert = Time.valueOf(time);

                    updateStatement.setInt(1, room_num);
                    updateStatement.setString(2, type);
                    updateStatement.setString(3, currentUserName);
                    updateStatement.setDate(4, date_convert);
                    updateStatement.setTime(5, time_convert);

                    int update = updateStatement.executeUpdate();

                    if (update > 0){
                        System.out.println("Successfully modified event.");
                    }
                    else{
                        System.out.println("Could not modify original event.");
                    }   
                }

                catch (IllegalArgumentException | SQLException e){
                    System.out.println("Enter the correct format/unique room number.");
                }
            }

            if (userInput == 2){
                try {
                    displayClassesSessions("Class");
                    displayClassesSessions("Session");

                    String deleteQuery = "DELETE FROM SessionsAndClasses WHERE room_num = ? AND tfirst_name = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

                    System.out.println("Enter the unique room number of your class/session you would like to delete: ");
                    Scanner scan = new Scanner(System.in);
                    room_num = scan.nextInt();
                    deleteStatement.setInt(1, room_num);
                    deleteStatement.setString(2, currentUserName);

                    int delete = deleteStatement.executeUpdate(); //delete row in SessionsAndClasses

                    if (delete > 0){
                        System.out.println("Successfully deleted event.");
                    }
                    else{
                        System.out.println("Could not delete event.");
                    }   
                }

                catch (IllegalArgumentException | SQLException e){
                    System.out.println("Enter the correct format/Can only delete your event/Enter a unique room number.");
                }
            }


            else if (userInput == 3){
                try {
                    displayClassesSessions("Class");
                    displayClassesSessions("Session");

                    System.out.println("Enter the room number of your class/session you would like to modify: ");
                    Scanner scan = new Scanner(System.in);
                    room_num = scan.nextInt();

                    String updateQuery = "UPDATE SessionsAndClasses SET start_date = ?, start_time = ? WHERE room_num = ? AND tfirst_name = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    System.out.println("Enter the start date (YYYY-MM-DD): ");
                    Scanner scan3 = new Scanner(System.in);
                    String date = scan3.nextLine();
                    Date date_convert = Date.valueOf(date);

                    System.out.println("Enter the start time: ");
                    Scanner scan4 = new Scanner(System.in);
                    String time = scan4.nextLine();
                    Time time_convert = Time.valueOf(time);

                    updateStatement.setDate(1, date_convert);
                    updateStatement.setTime(2, time_convert);
                    updateStatement.setInt(3, room_num);
                    updateStatement.setString(4, currentUserName);

                    int update = updateStatement.executeUpdate();

                    if (update > 0){
                        System.out.println("Successfully modified event.");
                    }
                    else{
                        System.out.println("Could not modify original event.");
                    }
                }
                catch (IllegalArgumentException | SQLException e){
                    System.out.println("Please enter input in the correct format/You can only modify your evet/No event associated with entered room number.");
                }
            }
        }
        catch (IllegalArgumentException e){
            System.out.println("Please enter input in the correct format.");
        }
    }

    //ADMIN FUNCTIONS

    // Administrative portal
    public static void adminPortal() {
        Scanner scan = new Scanner(System.in);

        try {
            while (true){
                System.out.println("\n-----ADMIN PORTAL-----");
                System.out.println(
                "Choose what you would like to do (0 to logout). \n(1) Manage Room Bookings \n(2) Update Class Schedules \n(3) Monitor Equipment \n(4) Make Bill \n(5) Refund/Clear Payments");
                int userInput = scan.nextInt();
                
                if (userInput == 0){
                    System.out.println("Logged out.");
                    break;
                }

                else if (userInput == 1){
                    roomBookingManagement();
                }

                else if (userInput == 2){
                    updateClassSchedule();
                }

                else if (userInput == 3){
                    equipmentMaintenanceMonitoring();
                }

                else if (userInput == 4){
                    try {
                        //display all member first names
                        String memberQuery = "SELECT * FROM Members ";
                        PreparedStatement memberStatement = connection.prepareStatement(memberQuery);
                        ResultSet members = memberStatement.executeQuery();
            
                        System.out.println("-----MEMBERS-----");
                        while (members.next()){
                            System.out.println("First Name: " + members.getObject(1));
                        }
                        try {
                            System.out.println("Enter the first name of the member who you would like to issue a bill to: ");
                            Scanner scanner = new Scanner(System.in);
                            String name = scanner.nextLine();
                            makeBill(name);
                        }
                        catch (IllegalArgumentException e){
                            System.out.println("Please enter a string value.");
                        }
                    }
                    catch(SQLException e){
                        System.out.println("No members available to display.");
                    }
                    
                }

                else if (userInput == 5){
                    //display all payments
                    try {
                        String paymentQuery = "SELECT * FROM Payments";
                        PreparedStatement payStatement = connection.prepareStatement(paymentQuery);
                        ResultSet payments = payStatement.executeQuery();
            
                        System.out.println("-----PAYMENTS-----");
                        while (payments.next()){
                            System.out.println("Payment ID: " + payments.getObject(1));
                            System.out.println("Payment Status: " + payments.getObject(2));
                            System.out.println("First Name: " + payments.getObject(3));
                            System.out.println("Last Name: " + payments.getObject(4));
                            System.out.println("Fee: " + payments.getObject(5));
                            System.out.println("Date Paid: " + payments.getObject(6));
                        }
                        try {
                            System.out.println("Enter the id of the bill you would like to clear: ");
                            Scanner scanner2 = new Scanner(System.in);
                            int id = scanner2.nextInt();
                            refundAndClearPayment(id);
                        }
                        catch (IllegalArgumentException e){
                            System.out.println("Please enter a string value.");
                        }
                    }
                    catch(SQLException e){
                        System.out.println("No members available to display.");
                    }
                }
            }
        }

        catch(NumberFormatException| InputMismatchException e){
            System.out.println("Please enter a valid number.");
        }
    }

    //Have admin make a bill for member
    public static void makeBill(String member_name){
        try {
            String classQuery = "SELECT * FROM ClassParticipants WHERE first_name = ?";
            PreparedStatement classStatement = connection.prepareStatement(classQuery);
            classStatement.setString(1, member_name);

            String sessionQuery = "SELECT * FROM SessionParticipants WHERE first_name = ?";
            PreparedStatement sessionStatement = connection.prepareStatement(sessionQuery);
            sessionStatement.setString(1, member_name);

            ResultSet classResult = classStatement.executeQuery();
            ResultSet sessionResult = sessionStatement.executeQuery();

            classCount = 0;
            while (classResult.next()){
                classCount++;
            }

            sessionCount = 0;
            while (sessionResult.next()){
                sessionCount++;
            }

            //calculate fees
            double membershipFee = 40.00;
            double classFee = 23.50;
            double sessionFee = 30.00;
            double equipmentUseFee = 11.00;
            double lockerUseFee = 6.50;
            double subtotal = membershipFee + (classFee*classCount) + (sessionFee*sessionCount) + equipmentUseFee + lockerUseFee;
            double tax = 0.13;
            double total = (tax * subtotal) + subtotal;

            //check if the member already has made a previous payment
            String checkQuery = "SELECT * FROM Payments WHERE first_name = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, member_name);
            ResultSet resultSet = checkStatement.executeQuery();
            int rowCount = 0;

            while (resultSet.next()){
                rowCount++;
            }

            //if they have already made a previous payment, update the fee_paid
            if (rowCount > 0){
                try {
                    String updateFeeQuery = "UPDATE Payments SET fee_paid = ?, status = ? WHERE first_name = ?";
                    PreparedStatement updateFeeStatement = connection.prepareStatement(updateFeeQuery);
                    updateFeeStatement.setDouble(1, total);
                    updateFeeStatement.setString(2, "Unpaid");
                    updateFeeStatement.setString(3, member_name);
                    int updatePayment = updateFeeStatement.executeUpdate();

                    if (updatePayment > 0) {
                        System.out.println("Bill created.");
                    }
                    
                    else {
                        System.out.println("Could not create bill.");
                    }
                }

                catch (SQLException e){
                    System.out.println("SQLException when updating payment.");
                }
            }

            //insert a new payment
            else{
                try {
                    //get member's last name
                    String lastNameQuery = "SELECT last_name FROM Members WHERE first_name = ?";
                    PreparedStatement lastNamePreparedStatement = connection.prepareStatement(lastNameQuery);
                    lastNamePreparedStatement.setString(1, member_name);
                    ResultSet memberResult = lastNamePreparedStatement.executeQuery();
                    memberResult.next();
                    String last_name = memberResult.getString(1);

                    String insertFeeQuery = "INSERT INTO Payments (status, first_name, last_name, fee_paid) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertFeeStatement = connection.prepareStatement(insertFeeQuery);
                    insertFeeStatement.setString(1, "Unpaid");
                    insertFeeStatement.setString(2, member_name);
                    insertFeeStatement.setString(3, last_name);
                    insertFeeStatement.setDouble(4, total);

                    int insertPayment = insertFeeStatement.executeUpdate();

                    if (insertPayment > 0) {
                        System.out.println("Bill created successfully.");
                    }
                    
                    else {
                        System.out.println("Could not create bill.");
                    }       
                }
                catch (SQLException e){
                    System.out.println("SQLException when inserting payment.");
                }
            }
        }
            catch (SQLException e){
                System.out.println("You have already paid.");
            }
    }

    // Admin can refund bills that are paid or clear outstanding bills
    public static void refundAndClearPayment(int id){
            try {
                String query = "DELETE FROM Payments WHERE payment_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                int cleared = statement.executeUpdate();

                if (cleared > 0){
                    System.out.println("Bill successfully cleared/refunded.");
                }

                else{
                    System.out.println("Bill could not be cleared/refunded.");
                }
            }
            catch (SQLException e){
                System.out.println("Exception when clearing/refunding payment.");
            }
    }

    // Allow admins to manage room booking through updating room numbers
    public static void roomBookingManagement() {
        try {
            displayClassesSessions("Class");
            displayClassesSessions("Session");

            String updateQuery = "UPDATE SessionsAndClasses SET room_num = ? WHERE room_num = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            
            System.out.println("Enter the unique room number to change: ");
            Scanner scan = new Scanner(System.in);
            int original_room_num = scan.nextInt();

            System.out.println("Enter a new unique room number: ");
            Scanner scan2 = new Scanner(System.in);
            int new_room_num = scan2.nextInt();

            updateStatement.setInt(1, new_room_num);
            updateStatement.setInt(2, original_room_num);
            int update = updateStatement.executeUpdate();

            if (update > 0){
                System.out.println("Updated room number.");
                try {
                    String retrieveQuery = "SELECT * FROM SessionsAndClasses WHERE room_num = ?";
                    PreparedStatement retrieveStatement = connection.prepareStatement(retrieveQuery);
                    retrieveStatement.setInt(1, new_room_num);
                    ResultSet result = retrieveStatement.executeQuery();

                    while (result.next()){
                        updateTrainerNotification(result.getString(3), new_room_num);
                    }
                }
                catch (SQLException e){
                    System.out.println("");
                }
            }
            else {
                System.out.println("Could not update room number.");
            }
        }
        catch (IllegalArgumentException | SQLException e){
            System.out.println("Enter the correct format.");
        }
    }

    // Allow admin to update class schedules
    public static void updateClassSchedule() {
        try {
            System.out.println("-----SCHEDULE MANAGEMENT-----");
            System.out.println("Would you like to (1) insert classes, (2) delete classes or (3) modify existing classes?");

            Scanner scanner = new Scanner(System.in);
            int userInput = scanner.nextInt();
            int room_num;

            if (userInput == 1){
                try {
                    displayClassesSessions("Class");
                    displayClassesSessions("Session");

                    String updateQuery = "INSERT INTO SessionsAndClasses (room_num, of_type, tfirst_name, start_date, start_time) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    System.out.println("Enter the unique room number of the class you would like to insert: ");
                    Scanner scan = new Scanner(System.in);
                    room_num = scan.nextInt();

                    System.out.println("Enter the name of the trainer who you would like to assign to lead this class: ");
                    Scanner scan2 = new Scanner(System.in);
                    String name = scan2.nextLine();

                    System.out.println("Enter the start date (YYYY-MM-DD): ");
                    Scanner scan3 = new Scanner(System.in);
                    String date = scan3.nextLine();
                    Date date_convert = Date.valueOf(date);

                    System.out.println("Enter the start time: ");
                    Scanner scan4 = new Scanner(System.in);
                    String time = scan4.nextLine();
                    Time time_convert = Time.valueOf(time);

                    updateStatement.setInt(1, room_num);
                    updateStatement.setString(2, "Class");
                    updateStatement.setString(3, name);
                    updateStatement.setDate(4, date_convert);
                    updateStatement.setTime(5, time_convert);

                    int update = updateStatement.executeUpdate();

                    if (update > 0){
                        System.out.println("Successfully inserted event.");
                        updateTrainerNotification(name, room_num);
                    }
                    
                    else{
                        System.out.println("Could not insert event.");
                    }   
                }

                catch (IllegalArgumentException | SQLException e){
                    System.out.println("Please enter input in the correct format/Enter a unique room number that exists and is not associated with any classes/sessions.");
                }
            }

            if (userInput == 2){
                try {
                    displayClassesSessions("Class");
                    displayClassesSessions("Session");

                    if (classCount > 0){
                        String deleteQuery = "DELETE FROM SessionsAndClasses WHERE room_num = ? AND of_type = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

                        System.out.println("Enter the unique room number of the class you would like to delete: ");
                        Scanner scan = new Scanner(System.in);
                        int room_num_delete = scan.nextInt();
                        deleteStatement.setInt(1, room_num_delete);
                        deleteStatement.setString(2, "Class");

                        int delete = deleteStatement.executeUpdate(); // delete row in SessionsAndClasses

                        if (delete > 0) {
                            System.out.println("Successfully deleted class.");
                        } else {
                            System.out.println("Could not delete class.");
                        }   
                    }
                    
                }

                catch (IllegalArgumentException | SQLException e){
                    System.out.println("\"Please enter input in the correct format/Enter a unique room number that exists and is not associated with any classes/sessions.");
                }
            }


            else if (userInput == 3){
                try {
                    displayClassesSessions("Class");
                    displayClassesSessions("Session");

                    if (classCount > 0){
                        System.out.println("Enter the room number of the class you would like to modify: ");
                        Scanner scan = new Scanner(System.in);
                        int room_num_update = scan.nextInt();

                        String updateQuery = "UPDATE SessionsAndClasses SET tfirst_name = ?, start_date = ?, start_time = ? WHERE room_num = ? AND of_type = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                        System.out.println("Enter the trainer's first name: ");
                        Scanner scan2 = new Scanner(System.in);
                        String name = scan2.nextLine();

                        System.out.println("Enter the start date (YYYY-MM-DD): ");
                        Scanner scan3 = new Scanner(System.in);
                        String date = scan3.nextLine();
                        Date date_convert = Date.valueOf(date);

                        System.out.println("Enter the start time (HH:MM:SS): ");
                        Scanner scan4 = new Scanner(System.in);
                        String time = scan4.nextLine();
                        Time time_convert = Time.valueOf(time);

                        updateStatement.setString(1, name);
                        updateStatement.setDate(2, date_convert);
                        updateStatement.setTime(3, time_convert);
                        updateStatement.setInt(4, room_num_update);
                        updateStatement.setString(5, "Class");

                        int update = updateStatement.executeUpdate();

                        if (update > 0) {
                            System.out.println("Successfully modified event.");
                            updateTrainerNotification(name, room_num_update);

                        } else {
                            System.out.println("Could not modify original event.");
                        }
                    }
                }
                catch (IllegalArgumentException | SQLException e){
                    System.out.println("Please enter input in the correct format/Enter a unique room number that exists and is not associated with any classes/sessions.");
                }
            }
        }
        catch (IllegalArgumentException e){
            System.out.println("Please enter input in the correct format.");
        }
    }

    // Send notifications to trainer on changes to their scheduled classes and sessions
    public static void updateTrainerNotification(String trainer_name, int room_num){
        try {
            // Insert a new notification
            String insertNotificationQuery = "INSERT INTO TrainerNotifications (tfirst_name, room_num) VALUES (?, ?)";
            PreparedStatement insertNotificationStatement = connection.prepareStatement(insertNotificationQuery);
            insertNotificationStatement.setString(1, trainer_name);
            insertNotificationStatement.setInt(2, room_num);
            insertNotificationStatement.executeUpdate();
            System.out.println("Successfully sent trainer a notification.");
        }
        catch (SQLException e) {
            System.out.println("Could not send trainer a notification.");
        }
    }

    // Allow admin to monitor maintenance
    // Monitoring includes both viewing the maintenance history of fitness equipment and updating it as necessary.
    public static void equipmentMaintenanceMonitoring() {
        try {
            System.out.println("-----EQUIPMENT MAINTENANCE-----");
            System.out.println("Would you like to (1) View Equipment Maintenance History or (2) Update Equipment Status");
            Scanner scan = new Scanner(System.in);
            int userInput = scan.nextInt();

            if (userInput == 1){
                try {
                    String query = "SELECT * FROM EquipmentMaintenance";
                    PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet view = statement.executeQuery();

                    if (!view.next()){
                        System.out.println("There are no reports to display.");
                    }

                    else{
                        System.out.println("\nEQUIPMENT HISTORY");
                        view.beforeFirst();
                        while (view.next()) {
                            System.out.println("Equipment ID: " + view.getObject(1));
                            System.out.println("Equipment Name: " + view.getObject(2));
                            System.out.println("Room Number: " + view.getObject(3));
                            System.out.println("Date Reported: " + view.getObject(4));
                            System.out.println("Status: " + view.getObject(5) + "\n");
                        }
                    }
                }

                catch (SQLException e){
                    System.out.println("No equipments to display.");
                }
            }

            else if (userInput == 2){
                try {
                    String updateQuery = "UPDATE EquipmentMaintenance SET status = ? WHERE room_num = ? AND equipment_name = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    System.out.println("Enter room number: ");
                    Scanner roomScan = new Scanner(System.in);
                    int room_num = roomScan.nextInt();

                    System.out.println("Enter equipment name: ");
                    Scanner equipScan = new Scanner(System.in);
                    String equipment = equipScan.nextLine();

                    System.out.println("What would you like to set the status to (Malfunctioned/Fixed)?");
                    Scanner statusScan = new Scanner(System.in);
                    String status = statusScan.nextLine();

                    updateStatement.setString(1, status);
                    updateStatement.setInt(2, room_num);
                    updateStatement.setString(3, equipment);
                    int update = updateStatement.executeUpdate();

                    if (update > 0){
                        System.out.println("There are either no equipments to maintain or you need to enter the correct fields.");
                    }
                }

                catch (SQLException e){
                    System.out.println("No equipments to display.");
                }
            }

        }
        catch (IllegalArgumentException e){
        }
    }
}