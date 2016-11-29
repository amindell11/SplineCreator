import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
/**
 * @author Jacob
 */
public class ExampleGame extends BasicGame
{
    public ExampleGame()
    {
        super("Wizard game");
    }
 
    public static void main(String[] arguments)
    {
        try
        {
            AppGameContainer app = new AppGameContainer(new ExampleGame());
            app.setDisplayMode(500, 400, false);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
 
    @Override
    public void init(GameContainer container) throws SlickException
    {
    	spline=new QuinticBezierSpline(new float[]{0,5,10,15,20,25});
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {

    }
    QuinticBezierSpline spline;
 
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	for(int x=0;x<500;x++){
    		g.draw(new Circle(x,spline.get(x),2));
    	}
    }
}