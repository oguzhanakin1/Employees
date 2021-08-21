
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EmployeeTransactions 
{
    private Connection con = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    public EmployeeTransactions() 
    {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.dbName 
                + "?useUnicode=true&characterEncoding=utf8";
        
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver bulundu");
        } 
        catch(ClassNotFoundException e)
        {
            System.out.println("Driver bulunamadı");
        }
        
        try 
        {
            con = DriverManager.getConnection(url,Database.username,Database.password);
            System.out.println("Bağlantı başarılı");
        } 
        catch (SQLException ex) 
        {
            System.out.println("Bağlantı başarısız");
        }
    }
    
    public boolean login(String username, String password)
    {
        String query = "SELECT * FROM adminler WHERE username = ? AND password = ?";
        try 
        {
            preparedStatement = con.prepareStatement(query);
            
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            return rs.next();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EmployeeTransactions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }    
    }
    
    public ArrayList<Employee> fetchEmployees()
    {
        ArrayList<Employee> result = new ArrayList<Employee>();
        
        try 
        {
            statement = con.createStatement();
            String query = "SELECT * FROM calisanlar";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("ad");
                String surname = rs.getString("soyad");
                String department = rs.getString("departman");
                String salary = rs.getString("maas");
                
                result.add(new Employee(id, name, surname, department, salary));
            }
            return result;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EmployeeTransactions.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void addEmployee(String name, String surname, String department, String salary)
    {
        try {
            String query = "INSERT INTO calisanlar (ad, soyad, departman, maas) VALUES(?,?,?,?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, department);
            preparedStatement.setString(4, salary);
            
            preparedStatement.executeUpdate();
            /*statement = con.createStatement();
             String add = "Insert into calisanlar (ad, soyad, departman, maas) VALUES(" 
                    + "'" + name + "'," + "'" + surname + "'," + "'" + department + "'," + "'" + salary + "')";
            statement.executeUpdate(add);*/
            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EmployeeTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    
    public void updateEmployee(int id, String name, String surname, String department, String salary)
    {
        String query = "UPDATE calisanlar SET ad = ?, soyad = ?,"
                + " departman = ?, maas = ? WHERE id = ?";
        
        try 
        {
            preparedStatement = con.prepareStatement(query);
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, department);
            preparedStatement.setString(4, salary);
            preparedStatement.setInt(5, id);
            
            preparedStatement.executeUpdate();    
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EmployeeTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteEmployee(int id)
    {
        String query = "DELETE FROM calisanlar WHERE id = ?";
        
        try 
        {
            preparedStatement = con.prepareStatement(query);
            
            preparedStatement.setInt(1, id);
            
            preparedStatement.executeUpdate();    
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EmployeeTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    