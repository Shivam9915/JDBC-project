import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Dbconnection {
    private static Dbconnection instance;
    private static Connection con;

    public Dbconnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "Majanani@1984");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public static Dbconnection getInstance() {
        if (instance == null) {
            instance = new Dbconnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }

}

// ........................patients class ..............//
class Patients {
    private int Id;
    private String Name;
    private int Age;
    private String Gender;

    public Patients(int Id, String Name, int Age, String Gender) {
        this.Id = Id;
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
    }

    public int getId() {
        return Id;

    }
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getAge() {
        return Age;

    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public String getGender() {
        return Gender;

    }

}

// ......................doctors classs.................//
class Doctors {
    private int Id;
    private String Name;
    private String Specialization;
    private int patients_id;
    private int doctors_id;

    public Doctors(int Id, String Name, String Specialization, int patients_id, int doctors_id) {
        this.Id = Id;
        this.Name = Name;
        this.Specialization = Specialization;
        this.patients_id = patients_id;
        this.doctors_id = doctors_id;

    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }

    public int getPatients_id() {
        return patients_id;
    }

    public int getDoctors_id() {
        return doctors_id;
    }

}
class PatientsDao{
    private Connection connection;
    private Scanner sc;

    // public PatientsDao(Connection connection,Scanner sc){
    //     this.connection=connection;
    //     this.sc=sc;
    // }
    public PatientsDao(){
        connection=Dbconnection.getInstance().getConnection();
        sc=new Scanner (System.in);
        
}
public void insertPatients(){
    System.out.println("Welcome to Aiims Bhopal...PLease Answer The Below Asked Questions");
    System.out.print("Enter Your Name : ");
    String Name=sc.next();
    sc.nextLine();
    System.out.print("Enter Your Age : ");
    int Age=sc.nextInt();
    System.out.print("Enter Your Gender : ");
    String Gender=sc.next();

    try{

        String query="INSERT INTO PATIENTS(Name,Age,Gender)VALUES(?,?,?)";
        PreparedStatement ps=connection.prepareStatement(query);
        ps.setString(1, Name);
        ps.setInt(2,Age);
        ps.setString(3,Gender);
        int rows=ps.executeUpdate();

        if(rows>0){
            System.out.println("Patient Added Successfully");
        }
        else{
            System.out.println("There is something wrong at your end....Failed to add patient");
        }
    }
    catch(SQLException e){
        e.printStackTrace();

    }
            
}
public void viewPatient(){

    String query="SELECT * FROM patients";
    try{

        PreparedStatement ps=connection.prepareStatement(query);
        ResultSet rs=ps.executeQuery();
        System.out.println("Patients");
        System.out.println( "+-----------+----------------+-----------+--------------------+");
        System.out.println(  "|    Id    |    Name         |    Age    |    Gender          |");
        System.out.println( "+-----------+----------------+-----------+--------------------+");
        while(rs.next()){
            int id=rs.getInt("id");
            String name=rs.getString("Name");
            int age=rs.getInt("Age");
            String Gender=rs.getString("Gender");
            System.out.printf("|%-10s|%-17s|%-11s|%-20s|\n",id,name,age,Gender);
            System.out.println("+-----------+----------------+-----------+-------------------+");
        }

    }
    catch(SQLException e){
        e.printStackTrace();
    }

}

public boolean getPatientById(int id){
    String query="SELECT * FROM patients WHERE ID=?";
try{

    PreparedStatement ps =connection.prepareStatement(query);
    ps.setInt(1,id);
    ResultSet rs=ps.executeQuery();
    if(rs.next()){
        return true;
    }
    else{
        return false;
    }
}
catch(SQLException e){
    e.printStackTrace();
}

return false;

}


}

// ........................doctorsdao....................//

class DoctorsDao{
    private Connection connection;
    private Scanner sc;

    // public DoctorsDao(Connection connection,Scanner sc){
    //     this.connection=connection;
    //     this.sc=sc;
    // }
    public DoctorsDao(){
        connection=Dbconnection.getInstance().getConnection();
        
}

// .................view doctors..............................//

public void viewDoctor(){

    String query="SELECT * FROM doctors";
    try{

        PreparedStatement ps=connection.prepareStatement(query);
        ResultSet rs=ps.executeQuery();
        System.out.println("doctors List :-");
        System.out.println( "+----------+------------------+------------------------------+");
        System.out.println(  "|    Id    |    Name           |    Specialization         |");
        System.out.println( "+----------+------------------+------------------------------+");
        while(rs.next()){
            int id=rs.getInt("Id");
            String name=rs.getString("Name");
            String specialization=rs.getString("Specialization");
            System.out.printf("|%-10s|%-20s|%-27s|\n",id,name,specialization);
            System.out.println( "+----------+-------------------+------------------------------+");
        }

    }
    catch(SQLException e){
        e.printStackTrace();
    }

}

public boolean getDoctorsById(int id){
    String query="SELECT * FROM doctors WHERE ID=?";
try{

    PreparedStatement ps =connection.prepareStatement(query);
    ps.setInt(1,id);
    ResultSet rs=ps.executeQuery();
    if(rs.next()){
        return true;
    }
    else{
        return false;
    }
}
catch(SQLException e){
    e.printStackTrace();
}

return false;

}


}


class BookingAppointment{
    private Connection connection;

    public BookingAppointment(){
        connection=Dbconnection.getInstance().getConnection();   
     }

     public void BookAppointment(PatientsDao patient,DoctorsDao doctor,Scanner sc){
        System.err.println("Enter Patient-id");
        int patientId=sc.nextInt();
        System.out.println("Enter doctor-id");
        int doctorId=sc.nextInt();
        System.out.println("Enter Appointment date (YYYY-MM-DD)");
        String AppointmentDate=sc.next();
        if(patient.getPatientById(patientId)&& doctor.getDoctorsById(doctorId)){
            if(checkDrAvailable(doctorId,AppointmentDate)){
               try{

                String appointment="INSERT INTO Apointment(patients_id,doctors_id,Appointment_date)VALUES(?,?,?)";
                PreparedStatement ps=connection.prepareStatement(appointment);
                ps.setInt(1,patientId);
                ps.setInt(2,doctorId);
                ps.setString(3,AppointmentDate);
                int rows=ps.executeUpdate();
                if(rows>0){
                   System.err.println("Appointment Booked");
                }
                else{
                   System.out.println("Appointment failed");
                }
               }
               catch(SQLException e){
                e.printStackTrace();
               }
            }

        }
        else{
            System.err.println("Either Doctor or patient isnt't available");
        }


     }
     
    //  utility function to check if dr is available on specified date //

     public boolean checkDrAvailable(int doctorid,String AppointmentDate){
       try{
        String query="SELECT COUNT * FROM apointment WHERE doctors_id=? AND Appointment_date=?";
        PreparedStatement ps=connection.prepareStatement(query);
        ps.setInt(1,doctorid);
        ps.setString(2,AppointmentDate);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
           int count= rs.getInt(1);
            if(count==0){
                return true;

            }
            else{
                return false;

            }

        }

       }
       catch(SQLException e){
        e.printStackTrace();
       }


       return false;
     }

}

// .....................hospital Management class......................//

class HospitalManagement{
    private PatientsDao patientdao;
    private DoctorsDao doctordao;
    private BookingAppointment appointment;
    private Scanner sc;

    public HospitalManagement(){
    
        patientdao=new PatientsDao();
        doctordao=new DoctorsDao();
        appointment=new BookingAppointment();
        sc=new Scanner (System.in);
    }
    public void run(){
        while(true){
            System.out.println("Hospital Mangemnet System");
            System.out.println("1. Book Appointment ");
            System.out.println("2. Add Patients");
            System.out.println("3. View All Patients ");
            System.out.println("4. View All Doctors ");
            System.out.println("5. Exit");
            System.out.println("Chhose an option");
            int option=sc.nextInt();
            switch (option) {
                case 1: 
                appointment.BookAppointment(patientdao, doctordao, sc);1
                    
                  break;
                  case 2:
                  patientdao.insertPatients();
                  break;
                  case 3:
                  patientdao.viewPatient();
                  break;
                  case 4:
                  doctordao.viewDoctor();
                    break;
                    case 5:
                    return;
                    default:
                    System.out.println("Please Enter Valid Option");
            }


        }
    }
}

    

public class HospitalManager{
    public static void main(String[] args) {
        HospitalManagement hospital=new HospitalManagement();
        hospital.run();

        
    }
}

