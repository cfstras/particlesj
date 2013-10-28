
package particles;

/**
 *  Float 3-Dimensional Vector
 * @author claus
 */
public class Vec3f implements Cloneable {
    
    public float x;
    public float y;
    public float z;
    
    public static final Vec3f NULL =    new Vec3f(0, 0, 0);
    public static final Vec3f UP =      new Vec3f(0, 1, 0);
    public static final Vec3f DOWN =    new Vec3f(0,-1, 0);
    public static final Vec3f LEFT =    new Vec3f(-1,0, 0);
    public static final Vec3f RIGHT =   new Vec3f(1, 0, 0);
    public static final Vec3f FRONT =   new Vec3f(0, 0, 1);
    public static final Vec3f BACK =    new Vec3f(0, 0,-1);
    public static final Vec3f ONE =     new Vec3f(1, 1, 1);
    
    public Vec3f(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public Vec3f(Vec3f cloneFrom){
        this.x=cloneFrom.x;
        this.y=cloneFrom.y;
        this.z=cloneFrom.z;
    }
    
    public static Vec3f add(Vec3f a, Vec3f b){
        return new Vec3f(a.x+b.x,a.y+b.y,a.z+b.z);
    }
    
    
    /**
     * Adds another Vector to this one and stores the result in this Vector.
     * @return this object.
     */
    public Vec3f addToThis(Vec3f other){
        this.x+=other.x;
        this.y+=other.y;
        this.z+=other.z;
        return this;
    }
    
    /**
     * Adds another Vector to this one and stores the result in a new Vector.
     * @return a new Vector
     */
    public Vec3f add(Vec3f other){
        return add(this,other);
    }
    
    public static float scalProd(Vec3f a, Vec3f b){
        return a.x*b.x+a.y*b.y+a.z*b.z;
    }
    
    public float scalProd(Vec3f other){
        return scalProd(this,other);
    }
    
    public static Vec3f mult(Vec3f a,Vec3f b){
        return new Vec3f(
                a.y*b.z-a.z*b.y,
                a.z*b.x-a.x*b.z,
                a.x*b.y-a.y*b.x);
    }
    public Vec3f multToThis(float factor){
        this.x *=factor;
        this.y*= factor;
        this.z*=factor;
        return this;
    }
    
    /**
     * Multiplies another Vector to this one and stores the result in this Vector.
     * @return this object.
     */
    public Vec3f multToThis(Vec3f other) {
        float oldX=x;
        float oldY=y;
        x= y*other.z - z*other.y;
        y= z*other.x - oldX*other.z;
        z= oldX*other.y - oldY*other.x;
        return this;
    }
    
    /**
     * Multiplies another Vector to this one and stores the result in a new Vector.
     * @return a new Vector.
     */
    public Vec3f mult(Vec3f other){
        return mult(this,other);
    }
    
    /**
     * Multiplies all values of this Vector with a value and stores the result in a new Vector.
     * @return a new Vector.
     */
    public Vec3f mult(float factor){
        return new Vec3f(x*factor,y*factor,z*factor);
    }
    
    public void normalize() {
        float length = (float)Math.sqrt(x*x+y*y+z*z);
        x/=length;
        y/=length;
        z/=length;
    }
    
    
    @Override
    public String toString(){
        return "V3f["+x+","+y+","+z+"]";
    }
}
