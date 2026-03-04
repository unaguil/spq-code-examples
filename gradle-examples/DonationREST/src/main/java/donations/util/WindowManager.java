package donations.util;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;

public abstract class WindowManager {

	private static Point getCenterPoint() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	}
	
	private static int getMaxWidth() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	}
	
	private static int getMaxHeight() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	}
	
	public static void topLeft(Window window) {
		window.setBounds(0, 0, window.getWidth(), window.getHeight());
        window.validate();
	}
	
	public static void topCenter(Window window) {
        int x = WindowManager.getCenterPoint().x - window.getWidth()/2;
		window.setBounds(x, 0, window.getWidth(), window.getHeight());
        window.validate();
	}
	
	public static void topRight(Window window) {		
        int x = WindowManager.getMaxWidth() - window.getWidth();
		window.setBounds(x, 0, window.getWidth(), window.getHeight());
        window.validate();
	}
	
	public static void middleLeft(Window window) {
        int y = WindowManager.getCenterPoint().y - window.getHeight()/2;
        window.setBounds(0, y, window.getWidth(), window.getHeight());
        window.validate();
    }
	
	public static void middleCenter(Window window) {
        Point centerPoint = WindowManager.getCenterPoint();
        int x = centerPoint.x - window.getWidth()/2;
        int y = centerPoint.y - window.getHeight()/2;
        window.setBounds(x, y, window.getWidth(), window.getHeight());
        window.validate();
    }
	
	public static void middleRight(Window window) {
        int x = WindowManager.getMaxWidth() - window.getWidth();
        int y = WindowManager.getCenterPoint().y - window.getHeight()/2;
        window.setBounds(x, y, window.getWidth(), window.getHeight());
        window.validate();
    }
	
	public static void bottomLeft(Window window) {
		int y = WindowManager.getMaxHeight() - window.getHeight();
        window.setBounds(0, y, window.getWidth(), window.getHeight());
        window.validate();
    }
	
	public static void bottomCenter(Window window) {
        int x = WindowManager.getCenterPoint().x - window.getWidth()/2;
        int y = WindowManager.getMaxHeight() - window.getHeight();
        window.setBounds(x, y, window.getWidth(), window.getHeight());
        window.validate();
    }
	
	public static void bottomRight(Window window) {
        int x = WindowManager.getMaxWidth() - window.getWidth();
        int y = WindowManager.getMaxHeight() - window.getHeight();
        window.setBounds(x, y, window.getWidth(), window.getHeight());
        window.validate();
    }
}