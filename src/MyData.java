/**
 * @author nikki
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyData {
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static FileOutputStream fos;
    private static FileInputStream fis;
    private static String root;
    private static final String FILE_NAME = "MyData.dat";
    public static String getRootDir()   {
        try {
            File f = new File(FILE_NAME);
            if (f.exists()) {
                fis = new FileInputStream(f);
                ois = new ObjectInputStream(fis);
                root = (String) ois.readObject();
                ois.close();
                fis.close();

                fis = null;
                ois = null;
                return  root;
            }
        }   catch(Exception e)  {
            
        }
        fis = null;
        ois = null;
        return "";
    }
    public static void saveRootDir(String root)   {
        try {
            File f = new File(FILE_NAME);
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(root);
            oos.close();
            fos.close();
        }   catch(Exception e)  {
            
        }
        fos = null;
        oos = null;
    }
}
