package main;

public class Vector2dDouble {
    public final double x;
    public final double y;

    public Vector2dDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2dDouble add(Vector2dDouble v){
        return new Vector2dDouble(this.x + v.x, this.y + v.y);
    }
    public Vector2dDouble subtract(Vector2dDouble v){
        return new Vector2dDouble(this.x - v.x, this.y - v.y);
    }
    public Vector2dDouble opposite(){
        return new Vector2dDouble(-this.x, -this.y);
    }


    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2dDouble))
            return false;
        Vector2dDouble that = (Vector2dDouble) other;
        return (this.x == that.x && this.y == that.y);
    }
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
