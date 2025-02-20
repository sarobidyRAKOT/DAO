package mg.ITU.DAO.principale;



import java.sql.*;
import java.util.Properties;

public class Util_BD {
    
    private String user;
    private String password;
    private String url;


    Connection conn;
    
    
    public Util_BD () {}

    private Util_BD (String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }



    public static Util_BD get_Instance () throws ClassNotFoundException {

        Properties properties = LoaderProperties.load_properties();

        String url = properties.getProperty("app.base.url");
        String user = properties.getProperty("app.base.utilisateur");
        String mdp = properties.getProperty("app.base.mot.de.passe");
        String className = properties.getProperty("app.classe.for.name");

        try {
            Class.forName(className);
            Util_BD util_BD = new Util_BD(url, user, mdp);
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
