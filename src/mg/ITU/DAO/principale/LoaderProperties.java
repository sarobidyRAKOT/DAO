package mg.ITU.DAO.principale;

import java.io.IOException;
import java.util.Properties;

public class LoaderProperties {
    
    public static Properties load_properties (String file_name) {
        
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(file_name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Properties load_properties () {
        
        Properties properties = new Properties();
        
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
