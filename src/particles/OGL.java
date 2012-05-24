/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

import java.io.File;
import org.lwjgl.input.Mouse;
import java.util.LinkedList;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 *
 * @author claus
 */
class OGL extends Thread {

    boolean run=true;
    
    float x, y, z;
    float rotationX, rotationY;
    float mouseSpeed = 0.5f;
    
    DisplayMode dm;
    int windowWidth;
    int windowHeight;
    float aspect;
    
    float particleRadius = 0.03f;
    int numParticles;
    
    LinkedList<Particle> particles;
    ParticleSystem ps;
    
    public OGL() {
        x = 0;
        y = 0;
        z = 9;
        rotationX = 0;
        rotationY = 0;
    }

    @Override
    public void run() {
        try {
            initGL();
            ps.lastTime=System.currentTimeMillis();
            while(run && !Display.isCloseRequested()) {
                
                doInput();
                ps.doGeneration();
                ps.sync();
                particles = ps.particles;
                render();
                Display.update();
                updateFPSandDelta();
                Display.sync(60);
            }
            Display.destroy();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    private void initGL() throws LWJGLException {
        //get librarypath -Djava.library.path=lib/native/windows/
        String ds = System.getProperty("file.separator");
        System.setProperty("org.lwjgl.librarypath", new File("").getAbsolutePath()+ ds +"lib"+ds+"native"+ds+"linux");       
        
        dm = null;
        for (DisplayMode d : Display.getAvailableDisplayModes()) {
            //System.out.println(d.toString());
            if (d.getWidth() == 800 && d.getHeight() == 600 && d.getFrequency() == 60) {
                //myDM = d;
            }
        }
        if (dm == null) {
            dm = Display.getDesktopDisplayMode();
        }
        System.out.println("Selected: " + dm);
        
        windowHeight=dm.getHeight();
        windowWidth=dm.getWidth();
        aspect = (float)windowWidth/(float)windowHeight;
        Display.setDisplayMode(dm);
        Display.setVSyncEnabled(true);
        Display.setTitle("particles");
        Display.setFullscreen(true);
        Display.create(new PixelFormat(8, 0, 0, 4));

        glDisable(GL_LIGHTING);
        //glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
        glShadeModel(GL_SMOOTH); // Enable Smooth Shading
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        glClearDepth(1.0); // Depth Buffer Setup
        glDepthFunc(GL_LESS); // The Type Of Depth Testing To Do
        glEnable(GL_DEPTH_TEST); // Enables Depth Testing
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        
        //glEnable(GL_CULL_FACE);
        //glFrontFace(GL_CW);

        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix

        //glEnable(GL_CULL_FACE);
        //glFrontFace(GL_CCW);
        

        // Really Nice Perspective Calculations
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    private void perspective(boolean right) {
        int vx = right ? windowWidth/2 : 0;
        
        glViewport(vx, 0, windowWidth/2, windowHeight);
        glMatrixMode (GL_PROJECTION);                       // Select The Projection Matrix
        glLoadIdentity ();                          // Reset The Projection Matrix
        // Calculate The Aspect Ratio Of The Window
        gluPerspective(
                80.0f,
                aspect/2,
                0.001f,
                1024.0f);
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    private void render() throws LWJGLException {
        glClear(GL_COLOR_BUFFER_BIT);
        
        perspective(false);
        glLoadIdentity();
        glTranslatef(-0.01f,0,0);
        drawWindow();
        
        perspective(true);
        glLoadIdentity();
        glTranslatef(0.02f,0,0);
        drawWindow();
    }
    
    private void drawWindow() {
        //camera
        Particle f = particles.element();
        Vec3f m2 = new Vec3f(f.speed);
        m2.normalize();
        //m2.multToThis(3);
        glRotatef(rotationY, 1.0f, 0.0f, 0.0f);  //rotate our camera on teh x-axis (up down)
        glRotatef(rotationX, 0.0f, 1.0f, 0.0f);  //rotate our camera on the y-axis (left right)
        gluLookAt(f.pos.x, f.pos.y, f.pos.z, f.pos.x+m2.x,f.pos.y+m2.y,f.pos.z+m2.z,  f.pos.x,f.pos.y,f.pos.z);
        //glTranslatef(0,particleRadius,-particleRadius);
        //glTranslated(-x, -y, -z); //translate the screento the position of our camera
        
        //get hold of current list
        LinkedList<Particle> l = particles;
        float[] col = new float[3];
        glBegin(GL_TRIANGLES);        
        for(Particle p: l) {
            if(p.forever){
                continue; //don't draw the viewer
            }
            Vec3f dir = new Vec3f(p.speed); dir.normalize();
            
            col = p.color.getColorComponents(col);
            //glColor3f(col[0]*p.life, col[1]*p.life, col[2]*p.life);
            glColor4f(col[0],col[1],col[2],p.life);
            glVertex3f(p.pos.x+dir.x*particleRadius, p.pos.y+dir.y*particleRadius, p.pos.z+dir.z*particleRadius ); //tip
            glVertex3f(p.pos.x-dir.y/2*particleRadius, p.pos.y+dir.x/2*particleRadius, p.pos.z); //base 1
            glVertex3f(p.pos.x+dir.y/2*particleRadius, p.pos.y-dir.x/2*particleRadius, p.pos.z); //base 2
            
            //glVertex3f(p.pos.x+particleRadius, p.pos.y+particleRadius, p.pos.z);
            //glVertex3f(p.pos.x-particleRadius, p.pos.y+particleRadius, p.pos.z);
            //glVertex3f(p.pos.x+particleRadius, p.pos.y-particleRadius, p.pos.z);
            //glVertex3f(p.pos.x-particleRadius, p.pos.y-particleRadius, p.pos.z);
            
        }
        glEnd();
        //System.out.println(l.size()+" particles drawn.");
    }
    
    /**
     * time at last frame
     */
    long lastFrame;
    /**
     * frames per second
     */
    int fps;
    /**
     * last fps time
     */
    long lastFPS;
    float deltaGTime;
    float deltaTime;

    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPSandDelta() {
        long time = getTime();
        //System.out.println(time);
        deltaTime = (time - lastFrame) / 1000f;
        lastFrame = time;

        if (time - lastFPS > 1000) {
            Display.setTitle("particles:"+ps.numParticles+" FPS:" + fps +
                    " deltaT:" + deltaTime +" deltaG:"+ps.deltaTime);
            fps = 0; //reset the FPS counter
            lastFPS = time; //add one second
        }
        fps++;
    }

    /**
     * Get the time in milliseconds
     *
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    private void doInput() {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
            try {
                Display.setFullscreen(false);
            } catch (LWJGLException ex) {
                ex.printStackTrace();
            }
        }
        
        if (Mouse.isGrabbed()) {
            rotationX += Mouse.getDX() * 0.1f * mouseSpeed;
            rotationY += -Mouse.getDY() * 0.1f * mouseSpeed;
            if (rotationY > 90f) {
                rotationY = 90f;
            } else if (rotationY < -90f) {
                rotationY = -90f;
            }
            if (rotationX < 0f) {
                rotationX += 360f;
            } else if (rotationX > 359f) {
                rotationX -= 360f;
            }

            //TODO calculate camera normal vector
            //player.rotationNormal=Vec3f.BACK;
            //player.rotationNormal=Quaternionf.rotate(player.rotationNormal, -player.rotation.y, true, false, false);
            //player.rotationNormal=Quaternionf.rotate(player.rotationNormal, -player.rotation.x, false, true, false);


        } else if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
            try {
                Display.setFullscreen(true);
            } catch (LWJGLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
