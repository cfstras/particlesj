/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

/**
 *
 * @author claus
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OGL ogl = new OGL();
        ParticleSystem ps = new ParticleSystem(ogl);
        ogl.ps = ps;
        ogl.start();
        
        //ps.start();
    }
}
