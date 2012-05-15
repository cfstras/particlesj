/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

import java.awt.Color;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

/**
 *
 * @author claus
 */
class ParticleSystem extends Thread {
    
    int maxParticles = 9001;
    
    float particleSpawnInterval = 20f/maxParticles;
    
    OGL ogl;
    boolean run=true;
    boolean pause=false;
    
    long lastTime;
    float deltaTime=0;
    long time_temp;
    long timeSince;
    float time;
    long startTime;
    float lastParticleBirth;
    
    int numParticles;
    
    Random r;
    LinkedList<Particle> particles;
    
    public ParticleSystem(OGL ogl) {
        this.ogl=ogl;
        particles = new LinkedList<Particle>();
        startTime = System.currentTimeMillis();
        r = new Random();
        Particle first = new Particle(0.3f,startTime,r);
        first.lifeSpan = 100f; first.forever=true; first.life=1f;
        first.color=Color.WHITE;
        first.pos=Vec3f.UP;
        first.speed=Vec3f.ONE.mult(1);
        particles.add(first);
    }
    
    @Override public void run(){
        while(run) {
            if(pause) {
                synchronized(this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {}
                }
            } else {
                doGeneration();
                sync();
                feedData();
            }
        }
    }

    void doGeneration() {
        numParticles = particles.size();
        while(numParticles<maxParticles && lastParticleBirth+particleSpawnInterval<time) {
            newParticle();
            lastParticleBirth+=particleSpawnInterval;
        }
        ListIterator<Particle> it = particles.listIterator();
        Particle p;
        while(it.hasNext()) {
            p=it.next();
            if(p.forever){
                //i'm indestructable
                p.life=1f;
                movement(p,0.5f,1f);
            } else {
                if(p.spawnTime+p.lifeSpan<time) {
                    it.remove();
                    numParticles--;
                    //System.out.println("death: "+p.toString());
                    continue;
                }
                if(p.spawning){
                    p.life = (time - p.spawnTime)/p.lifeSpan*10;
                    if(p.life>=1f) p.spawning=false;
                } else {
                    p.life = 1- ( time-p.spawnTime )/ p.lifeSpan;
                }
                movement(p,0.03f,5f);
            }
            
            
        }
    }
    
    void movement(Particle p, float factor, float gravity) {
        //move
        p.pos.addToThis(p.speed.mult(deltaTime * 0.3f * factor));
        //add pull
        p.speed.addToThis(p.pos.mult(-1f * deltaTime * factor*gravity)); // the great attractor is at 0,0,0
    }
    
    private void newParticle() {
        Particle p = new Particle(1.0f, time, r);
        //p.color=Color.RED;
        particles.add(p);
        numParticles++;
        //System.out.println("new Particle: "+p.toString());
    }
    
    private void feedData() {
        LinkedList<Particle> clone = new LinkedList<Particle>();
        for(Particle p : particles) {
            clone.add(p.clone());
        }
        ogl.particles=clone;
        ogl.numParticles=numParticles;
        ogl.deltaGTime=deltaTime;
    }

    void sync() {
        time_temp = System.currentTimeMillis();
        timeSince = time_temp-lastTime;
        //if(timeSince<16) { //16ms/frame => 62 fps
        //    try {
        //        sleep(16-timeSince);
        //    } catch (InterruptedException ex) {}
        //    time_temp = System.currentTimeMillis();
        //    timeSince = time_temp-lastTime;
        //}
        lastTime=time_temp;
        deltaTime = timeSince/1000f;
        time = (time_temp-startTime)/1000f;
    }

    
}
