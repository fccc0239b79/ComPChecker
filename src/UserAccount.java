
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tom
 * @author Lillie
 */
public class UserAccount {

    private String username;
    private String password;
    private String fName;
    private String sName;
    private String email;
    private boolean type;          //True for admin, false for general. 

    private String loggedFname;
    private String loggedSname;
    private String loggedEmail;



    /**
     *
     * @return
     */
    
    public String getFname(){
        return loggedFname;
    }
    public String getSname(){
        return loggedSname;
    }
    public String getEmail(){
        return loggedEmail;
    }
    public String getUsername() {

        return username;
    }
    
   
    
    
    
    /**
     *
     * @param enteredUname
     * @param enteredPass
     * @return
     */
    public boolean LogInService(String enteredUname, String enteredPass) {
        //Checks entered username and password against ones stored in database.
        Connection con = DatabaseConnection.establishConnection();
        String dbUname, dbPassword;
        
        try {
            Statement stmt = (Statement) con.createStatement();
            String query = ("SELECT ID, Password, accountType, Fname, Sname, Email FROM Account WHERE ID='" + enteredUname+"'");

            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {

                dbUname = rs.getString("ID");
                dbPassword = rs.getString("Password");
                
                loggedFname = rs.getString("Fname");
                loggedSname = rs.getString("Sname");
                loggedEmail = rs.getString("Email");

                 //System.out.println(loggedFname + loggedSname + loggedEmail);
                 
                boolean dbType = rs.getBoolean("accountType");
                if (dbUname.equals(enteredUname) && dbPassword.equals(enteredPass)) { //Comparison check
                    setType(dbType); //Sets type of user.
                    con.close();
                    setUsername(dbUname);
                    setPassword(dbPassword);
                    return true;
                }

            }

        } catch (SQLException err) {
            System.out.println(err.getMessage());   //Prints out SQL error 
        }

        return false;
        
    }

    /**
     * checks whether password entered into GUI is equal to password
     * stored in the database.
     *
     * @param username
     * @param password
     * @return
     */
    ArrayList<String> builds = new ArrayList<String>();

    public ArrayList<String> getBuilds(){
        builds.clear();
           Connection con = DatabaseConnection.establishConnection();
           String user = username;
            
      //ArrayList<String> builds = new ArrayList<String>();
           
            try {
            Statement stmt = (Statement) con.createStatement();
            String query = ("SELECT name FROM Build WHERE Account='" + user+"'");

            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();
            
            while (rs.next()) {
          
                builds.add(rs.getString("name"));
                
            }
            if(!rs.first()){
                builds.add("No Builds");
                return builds;
            }
           }
        catch (SQLException err) {
            System.out.println(err.getMessage());   //Prints out SQL error 
        }
     //System.out.println(builds);

           return builds;
    }
    
    
    public boolean checkPassword(String username, String password){
            // this statement establishes the connection between netbeans and the vm
           Connection con = DatabaseConnection.establishConnection();
           String user = username;
           String enteredPassword = password;
            try {
            Statement stmt = (Statement) con.createStatement();
            String query = ("SELECT ID, Password FROM Account WHERE ID='" + user+"'");

            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                   String dbPassword = rs.getString("Password");
                   if(dbPassword == enteredPassword){
                   return true;
                   
                   }else{
                       return false;
                       
                   }
                       
                       
                   
            

                }
            

            }

        catch (SQLException err) {
            System.out.println(err.getMessage());   //Prints out SQL error 
            return false;
        }
        
        
        
         return false;
            }
   
    
    
    /**
     * Enables user to change password in the GUI and new password is then
     * accepted through connected database. 
     * 
     * @param username
     * @param newPassword
     * @return
     */
    
    public void changePassword(String username, String newPassword){
    Connection con = DatabaseConnection.establishConnection();
    String user = username;
    String password = newPassword;
    try {
    
       //SQL query - changes password where the username is equal to input, entered by user.
       String query = ("UPDATE Account SET Password '" + password + "' WHERE Username = '" + username +"' ;  ");
       PreparedStatement statement = con.prepareStatement(query);
    
 
    }
    catch(SQLException err){ //error message
        
    
    }
    
    // setting the users data entered below
    }
    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param fname
     */
    public void setFname(String fname) {
        this.fName = fname;
    }

    /**
     *
     * @param sname
     */
    public void setSname(String sname) {
        this.sName = sname;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @param type
     */
    public void setType(boolean type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public boolean getType() {
        return type;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LogIn frm = new LogIn();
        frm.setVisible(true);

    }

    /**
     * checking the availability of a username in the database.
     * @param username
     * @return
     */
    public boolean usernameAvailability(String username) {
        Connection con = DatabaseConnection.establishConnection();

        try {
            int availability;
            Statement stmt = (Statement) con.createStatement();
            //checks username entered against usernames within database.
            String query = ("SELECT COUNT(*) FROM Account WHERE ID = '" + username + "'");

            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
               availability = rs.getInt("COUNT(*)");
               if(availability == 1){ //username is already taken
                   return false;
               }else{
                return true; //username is available
               }
            }
            
        } catch (SQLException err) {

            System.out.println(err.getMessage());
            return false;
        }
        return false;
    }    
    
    /**
     * saves the users details into the database
     */
    public void saveUser(){
    
        //connecting to the vm
    Connection con = DatabaseConnection.establishConnection();
    
    try {
        //SQL query for inserting data into account table
       String query = "INSERT INTO Account values (?,?,?,?,?,?)"; 
          
       PreparedStatement statement = con.prepareStatement(query);
       
       //setting user inputs into sql query
       statement.setString(1,username);
       statement.setString(2, password);
       statement.setString(3, fName);
       statement.setString(4, sName);
       statement.setString(5,email);
       statement.setBoolean(6, type);
       statement.execute();
    }
    catch(SQLException err){
        
    
    }
    
 
    
    
    
    }
    
    
     protected void reset(){
        username = " ";
   password= " ";
    fName= " ";
    sName= " ";
     email= " ";
     type= false;         //True for admin, false for general. 

   loggedFname = " ";
     loggedSname= " ";
    loggedEmail= " ";

    }
    
    
}

