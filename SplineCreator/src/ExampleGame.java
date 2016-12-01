import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Trajectory.Segment;
import com.team254.lib.trajectory.TrajectoryGenerator.Config;
import com.team254.lib.trajectory.WaypointSequence;
import com.team254.lib.trajectory.WaypointSequence.Waypoint;

/**
 * @author Jacob
 */
public class ExampleGame extends BasicGame {
	public static double METERS_PER_PIXEL_WIDTH = 0.0132124704, METERS_PER_PIXEL_HEIGHT = 0.01305609003;

	public ExampleGame() {
		super("Wizard game");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new ExampleGame());
			app.setDisplayMode(1265, 622, false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	int currentIndex;

	@Override
	public void init(GameContainer container) throws SlickException {
		waypoints = new WaypointSequence(6);
		map = new Image("map.png");
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {

	}

	WaypointSequence waypoints;
	Trajectory traj;

	public void mousePressed(int button, int x, int y) {
		double x_meters = x * METERS_PER_PIXEL_WIDTH;
		double y_meters = y * METERS_PER_PIXEL_HEIGHT;

		if (currentIndex < waypoints.getMaxWaypoints()) {
			Waypoint w = waypoints.getWaypoint(currentIndex - 1);
			double theta = currentIndex == 0 ? 0 : Math.atan((y_meters - w.y) / (x_meters - w.x));
			// if(currentIndex>0)w.theta=Math.atan(w.y-y)/(w.x-x)+w.theta/2;

			waypoints.addWaypoint(new WaypointSequence.Waypoint(x_meters, y_meters, theta));
			currentIndex++;
		} else {
			for (int c = 0; c < waypoints.getNumWaypoints(); c++) {
				Waypoint w=waypoints.getWaypoint(c);
				float x1_pixels = (float) (w.x / METERS_PER_PIXEL_WIDTH);
				float y1_pixels = (float) (w.y / METERS_PER_PIXEL_HEIGHT);
				if (Math.hypot(x1_pixels - x, y1_pixels - y) < 50) {
					w.theta=Math.atan((y1_pixels-y) / (x1_pixels-x));
					System.out.println("mouse setting angle");
				}
			}
			Config config = new Config();
			config.dt = .01;
			config.max_acc = 8.0;
			config.max_jerk = 50.0;
			config.max_vel = 10.0;
			traj = PathGenerator.generateFromPath(waypoints, config);
		}
	}
	Image map;

	public void render(GameContainer container, Graphics g) throws SlickException {
		map.draw(0, 0);
		
		g.setLineWidth(3);
		if (traj != null) {
			g.setColor(Color.green);
			for (int x = 0; x < traj.getNumSegments(); x++) {
				Segment s = traj.getSegment(x);
				double x_pixels = s.x / METERS_PER_PIXEL_WIDTH;
				double y_pixels = s.y / METERS_PER_PIXEL_HEIGHT;

				g.fillOval((float) x_pixels-2, (float) y_pixels-2, 4, 4);
			}

			Segment s = traj.getSegment(traj.getNumSegments() - 1);
			float x1_pixels = (float) (s.x / METERS_PER_PIXEL_WIDTH);
			float y1_pixels = (float) (s.y / METERS_PER_PIXEL_HEIGHT);
			Segment s2 = traj.getSegment(0);

			float x2_pixels = (float) (s2.x / METERS_PER_PIXEL_WIDTH);
			float y2_pixels = (float) (s2.y / METERS_PER_PIXEL_HEIGHT);

			float sign = Math.signum(y2_pixels - y1_pixels);
			float h = y1_pixels - sign * 20;
			g.setLineWidth(1);
			g.setColor(Color.red);
			g.drawString(Math.floor(Math.abs(s.x - s2.x) * 100) / 100 + " meters", (x1_pixels + x2_pixels / 2) - 20, h);
			g.drawLine(x1_pixels, y1_pixels, x1_pixels, h - sign * 5);
			g.drawLine(x2_pixels, y2_pixels, x2_pixels, h - sign * 5);
			g.drawLine(x1_pixels, h, x2_pixels, h);
		}
		g.setLineWidth(1);
		for (int x = 0; x < waypoints.getNumWaypoints(); x++) {
			Waypoint w = waypoints.getWaypoint(x);
			double x_meters = w.x / METERS_PER_PIXEL_WIDTH;
			double y_meters = w.y / METERS_PER_PIXEL_HEIGHT;
			g.setColor(Color.transparent);
			g.fillOval((float) x_meters - 25, (float) y_meters - 25, 50, 50);
			g.setColor(Color.magenta);
			g.fillOval((float) x_meters - 5, (float) y_meters - 5, 10, 10);

			g.setColor(Color.blue);
			g.drawLine((float) (x_meters - Math.cos(w.theta) * 22), (float) (y_meters - Math.sin(w.theta) * 22),
					(float) (x_meters + Math.cos(w.theta) * 22), (float) (y_meters + Math.sin(w.theta) * 22));

		}

	}
}