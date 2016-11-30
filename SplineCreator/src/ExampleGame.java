import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Trajectory.Pair;
import com.team254.lib.trajectory.Trajectory.Segment;
import com.team254.lib.trajectory.TrajectoryGenerator.Config;
import com.team254.lib.trajectory.WaypointSequence;
import com.team254.lib.trajectory.WaypointSequence.Waypoint;

/**
 * @author Jacob
 */
public class ExampleGame extends BasicGame {
	public ExampleGame() {
		super("Wizard game");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new ExampleGame());
			app.setDisplayMode(500, 400, false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	int currentIndex;
	Point[] points;

	@Override
	public void init(GameContainer container) throws SlickException {
		points = new Point[6];
		waypoints=new WaypointSequence(6);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {

	}
	WaypointSequence waypoints;
	Trajectory traj;
	public void mousePressed(int button, int x, int y) {
		if (currentIndex < waypoints.getMaxWaypoints()) {
			Waypoint w = waypoints.getWaypoint(currentIndex-1);
			double theta=currentIndex==0?0:Math.atan((y-w.y)/(x-w.x));
			if(currentIndex>0)w.theta=Math.atan(w.y-y)/(w.x-x)+w.theta/2;

		    waypoints.addWaypoint(new WaypointSequence.Waypoint(x, y, theta));
			currentIndex++;
		}else{
			Config config=new Config();
		      config.dt = .01;
		      config.max_acc = 8.0;
		      config.max_jerk = 50.0;
		      config.max_vel = 10.0;

			traj=PathGenerator.generateFromPath(waypoints, config);
			start=System.currentTimeMillis();
			System.out.println(waypoints.getNumWaypoints());
		}
	}

	double start;
	public void render(GameContainer container, Graphics g) throws SlickException {
		for(int x=0;x<waypoints.getNumWaypoints();x++){
			Waypoint w = waypoints.getWaypoint(x);
			//g.drawLine((float)w.x,(float)w.y,(float)(w.x+Math.cos(w.theta)*10),(float)(w.y+Math.sin(w.theta)*10));
			g.fillOval((float)w.x,(float)w.y,5,5);

		}
		if(traj!=null){
			g.setColor(Color.red);
			for(int x=0;x<traj.getNumSegments();x++){
				Segment s = traj.getSegment(x);
				System.out.println(s.x);
				g.fillOval((float)s.x,(float)s.y,2,2);
			}
		}
	}
}