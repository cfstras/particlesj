package particles;

import java.io.*;
/**
*
* @author claus
*/
public class Main {

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) throws IOException {

        String system;
        String prop = System.getProperty("os.name");
        if(prop==null) {
            System.out.println("Error: could not determine system type.");
            return;
        }
        if(prop.contains("Linux")){
            system = "linux";
        } else if(prop.contains("Windows")){
            system = "windows";
        } else if(prop.contains("Mac")){
            system = "macosx";
        } else if(prop.contains("Solaris")){
            system = "solaris";
        } else {
            System.out.println("can't identify system \""+prop+"\"");
            return;
        }

        System.setProperty("org.lwjgl.librarypath",new File(".").getCanonicalPath()+"/lib/native/"+system);
        System.setProperty("org.lwjgl.util.Debug","true");
        System.setProperty("org.lwjgl.util.NoChecks","false");

        OGL ogl = new OGL();
        ParticleSystem ps = new ParticleSystem(ogl);
        ogl.ps = ps;
        ogl.start();

    //ps.start();
    }
}
