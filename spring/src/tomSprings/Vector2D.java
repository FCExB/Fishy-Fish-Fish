package tomSprings;
public class Vector2D {
	 float x;
     float y;    

     Vector2D()
     {
         x = 0.0F;
         y = 0.0F;		    		    
     }

     float distance(Vector2D other) 
     {
         return (float)Math.sqrt((x - other.x) * (x - other.x) 
             + (y - other.y) * (y - other.y));    
     }

}
