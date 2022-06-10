package example.facade;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Database {
    private Database(){}
    public static Properties getProperties(String dbName){
        String fileName = dbName + ".txt";
        Properties prop = new Properties();
        try{
            prop.load(new FileInputStream(fileName));
        }catch (IOException ie){
            System.out.println("Warning: " + fileName + "is not found.");
        }
        return prop;
    }
}
