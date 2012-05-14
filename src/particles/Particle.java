/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author claus
 */
class Particle implements Cloneable {
    
    float size;
    float spawnTime;
    float lifeSpan;
    float life;
    Color color;
    
    Vec3f pos;
    Vec3f speed;
    float weight;
    
    public Particle(Vec3f pos, Vec3f speed, Color color, float weight, float size, float lifeSpan, float spawnTime) {
        this.size=size; this.speed=speed; this.color=color; this.pos=pos;
        this.weight=weight; this.size=size; this.lifeSpan=lifeSpan;
        this.spawnTime=spawnTime;
        life=1f;
    }
    
    public Particle(Vec3f pos, Vec3f speed, Color color, float weight, float size, float lifeSpan, float spawnTime,float life) {
        this.size=size; this.speed=speed; this.color=color; this.pos=pos;
        this.weight=weight; this.size=size; this.lifeSpan=lifeSpan;
        this.spawnTime=spawnTime;
        this.life = life;
    }
    
    public Particle(float magnitude, float spawnTime, Random r) {
        size = (float)Math.abs(magnitude*(r.nextGaussian()+1)*3);
        this.spawnTime = spawnTime;
        lifeSpan=magnitude*20;
        weight = magnitude;
        
        pos = new Vec3f((float)r.nextGaussian()*magnitude,
                (float)r.nextGaussian()*magnitude,
                (float)r.nextGaussian()*magnitude);
        speed = new Vec3f((float)r.nextGaussian()*magnitude*3,
                (float)r.nextGaussian()*magnitude*3,
                (float)r.nextGaussian()*magnitude*3);
        color = new Color(Color.HSBtoRGB(r.nextFloat(),
                (float)r.nextGaussian()*0.3f+0.6f,
                (float)r.nextGaussian()*0.3f+0.6f));
        life=1f;
    }
    
    @Override public Particle clone() {
        Particle n = new Particle(pos,speed,color,weight,size,lifeSpan,spawnTime,life);
        return n;
    }
    
    @Override public String toString() {
        return "[P:p="+pos.toString()+";c="+color.toString()+"]";
    }
    
}
