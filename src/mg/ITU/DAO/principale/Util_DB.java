package mg.ITU.DAO.principale;



import java.sql.*;
import java.util.Properties;

public class Util_DB {
    
    private String user;
    private String password;
    private String url;


    Connection conn;
    
    
    public Util_DB () {}

    private Util_DB (String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }



    public static Util_DB get_Instance () throws ClassNotFoundException {

        Properties properties = LoaderProperties.load_properties();

        String url = properties.getProperty("app.base.url");
        String user = properties.getProperty("app.base.utilisateur");
        String mdp = properties.getProperty("app.base.mot.de.passe");
        String className = properties.getProperty("app.classe.for.name");

        try {
            Class.forName(className);
            Util_DB util_BD = new Util_DB(url, user, mdp);
            return util_BD;
        } catch (ClassNotFoundException e) {
            throw e;
        }        
        
    }
    
    
    public Connection connect () throws SQLException {
                                
        this.conn = DriverManager.getConnection(this.url, this.user, this.password);
        return this.conn;
    }


}
