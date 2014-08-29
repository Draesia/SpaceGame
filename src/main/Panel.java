package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import ship.Ship;

public class Panel {

	public static void main(String[] args) {
		new Panel();
	}

	public Panel() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				JFrame frame = new JFrame("SpaceGame!");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new TestPane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public enum KeyState {
		UP, DOWN, LEFT, RIGHT, SPACE;
	}

	public class TestPane extends JPanel {

		private Engine engine;

		public TestPane() {
			engine = new Engine(this);
			engine.gameStart();

			InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up_pressed");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down_pressed");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left_pressed");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right_pressed");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "up_released");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down_released");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left_released");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right_released");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space_pressed");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "space_released");
			ActionMap am = getActionMap();
			am.put("up_pressed", new AddState(engine, KeyState.UP));
			am.put("up_released", new RemoveState(engine, KeyState.UP));
			am.put("down_pressed", new AddState(engine, KeyState.DOWN));
			am.put("down_released", new RemoveState(engine, KeyState.DOWN));
			am.put("left_pressed", new AddState(engine, KeyState.LEFT));
			am.put("left_released", new RemoveState(engine, KeyState.LEFT));
			am.put("right_pressed", new AddState(engine, KeyState.RIGHT));
			am.put("right_released", new RemoveState(engine, KeyState.RIGHT));
			am.put("space_pressed", new AddState(engine, KeyState.SPACE));
			am.put("space_released", new RemoveState(engine, KeyState.SPACE));
		}



		@Override
		public Dimension getPreferredSize() {
			return new Dimension(800, 800);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			int width = this.getWidth();
			int height = this.getHeight();
			//FILL BACKGROUND
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color color1 = new Color(0x18393a);
			Color color2 = new Color(0x10161e);
			GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
			g2.setPaint(gp);
			g2.fillRect(0, 0, width, height);
			//END FILL BACKGROUND
			//ADD STARS


			//END STARS

			g2.setColor(Color.RED);
			int xOffset = (int) (Engine.space.myShip.x-(width/2));
			int yOffset = (int) (Engine.space.myShip.y-(height/2));
			g2.translate(-xOffset, -yOffset);

			for(Entity e : Engine.space.objects)
			{
				if(e instanceof Ship)
				{
					Ship ship = (Ship) e;
					ship.drawShip(g2);
				}
				if(e instanceof Planet)
				{
					Planet p = (Planet) e;
					p.drawPlanet(g2);
				}
				if(e instanceof Bullet){
					Bullet b = (Bullet)e;
					b.drawTrajectory(g2);
					g2.setColor(Color.WHITE);
					g2.drawOval(b.getX()-2, b.getY()-2, 4, 4);
				}
				if(e instanceof Asteroid){
					Asteroid a = (Asteroid) e;
					g2.setColor(Color.GRAY);
					g2.fillPolygon(a.getPolygon());
				}
			}
			Point me = Engine.space.myShip.getCenter();
			for(Entity e : Engine.space.objects)
			{
				if(e instanceof Ship && !e.equals(Engine.space.myShip))
				{
					Ship ship = (Ship) e;
					Point them = ship.getCenter();
					int i = width < height ? width : height;
					int x = ((me.x - them.x)*-100)/i;
					int y = ((me.y - them.y)*-100)/i;
					g2.setColor(Color.RED);
					g2.drawOval((int)Engine.space.myShip.getCenter().getX()+x-5,(int) Engine.space.myShip.getCenter().getY()+y-5, 10, 10);
				}
				if(e instanceof Planet)
				{
					Planet p = (Planet) e;
					int i = width < height ? width : height;
					int x = (int) (((me.x - p.x)*-100)/i);
					int y = (int) (((me.y - p.y)*-100)/i);
					g2.setColor(Color.BLUE);
					g2.fillOval((int)Engine.space.myShip.getCenter().getX()+x-(p.radius/20),(int) Engine.space.myShip.getCenter().getY()+y-(p.radius/20), p.radius/10, p.radius/10);
				}
			}
			g2.dispose();

		}
	}

	public static class Engine {

		public static final int MAP_WIDTH = 15 * 4;
		public static final int MAP_HEIGHT = 9 * 4;
		public static final int X_DELTA = 8;
		public static final int Y_DELTA = 8;

		//This value would probably be stored elsewhere.
		public static final double GAME_HERTZ = 60.0;
		//Calculate how many ns each frame should take for our target game hertz.
		public static final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		//We will need the last update time.
		static double lastUpdateTime = System.nanoTime();
		//Store the last time we rendered.
		static double lastRenderTime = System.nanoTime();

		//If we are able to get as high as this FPS, don't render again.
		final static double TARGET_FPS = GAME_HERTZ;
		final static double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		//Simple way of finding FPS.
		static int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		public static int fps = 60;
		public static int frameCount = 0;
		public static Space space;
		private boolean isGameFinished;
		private JPanel panel;
		private Set<KeyState> keyStates;
		public Engine(JPanel bufferRenderer) {
			keyStates = new HashSet<>(4);
			this.panel = bufferRenderer;
		}

		public void gameStart() {
			space = new Space();
			Thread gameThread = new Thread() {
				@Override
				public void run() {
					gameLoop();
				}
			};
			gameThread.setDaemon(false);
			gameThread.start();
		}

		public void gameLoop() {
			while (!isGameFinished) {
				double now = System.nanoTime();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				gameUpdate();
				panel.repaint();
				checkFPS(now);
				try { Thread.sleep(15);
				} catch (InterruptedException e) {}
			}
		}

		private void checkFPS(double now)
		{
			frameCount++;
			lastRenderTime = now;
			int thisSecond = (int) (lastUpdateTime / 1000000000);
			if (thisSecond > lastSecondTime) {
				fps = frameCount;
				frameCount = 0;
				lastSecondTime = thisSecond;
			}
		}


		protected void gameUpdate() {
			if (keyStates.contains(KeyState.DOWN)) {
				space.myShip.accelerate(-1F);
			} else if (keyStates.contains(KeyState.UP)) {
				space.myShip.accelerate(1F);
			}
			if(keyStates.contains(KeyState.SPACE))
			{
				for(Bullet b : space.myShip.fire())
				{
					space.objects.add(b);
				}

			}
			if (keyStates.contains(KeyState.RIGHT)) {
				space.myShip.rotation += 4;
			} else if (keyStates.contains(KeyState.LEFT)) {
				space.myShip.rotation -= 4;
				if(space.myShip.rotation == 360) space.myShip.rotation = 0;
			}
			space.updatePosition();

		}

		public void addKeyState(KeyState state) {
			keyStates.add(state);
		}

		public void removeKeyState(KeyState state) {
			keyStates.remove(state);
		}

	}

	public class AddState extends AbstractAction {

		private Engine engine;
		private KeyState state;

		public AddState(Engine engine, KeyState state) {
			this.engine = engine;
			this.state = state;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			engine.addKeyState(state);
		}

	}

	public class RemoveState extends AbstractAction {

		private Engine engine;
		private KeyState state;

		public RemoveState(Engine engine, KeyState state) {
			this.engine = engine;
			this.state = state;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			engine.removeKeyState(state);
		}

	}

}