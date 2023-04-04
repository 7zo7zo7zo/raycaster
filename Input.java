import java.awt.*;
import java.awt.event.*;

public class Input implements KeyListener, MouseMotionListener {

	private boolean[] keys;

	public boolean forward, backward, left, right, strafeL, strafeR, keyHeld;

	public boolean mouseCaptured;
	public int mouseX, lastX, xChange;

	public Input() {
		keys = new boolean[KeyEvent.VK_CONTEXT_MENU + 1];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !keyHeld) {
			keyHeld = true;
			mouseCaptured = !mouseCaptured;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			keyHeld = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void tick() {
		//esc = keys[KeyEvent.VK_ESCAPE];
		forward = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		backward = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
		strafeL = keys[KeyEvent.VK_A];
		strafeR = keys[KeyEvent.VK_D];

		/*
		//if(esc) mouseCaptured = !mouseCaptured;
		if(MouseInfo.getPointerInfo().getLocation().x != mouseX) {
			xChange = mouseX - lastX;
			lastX = mouseX;
		}
		else xChange = 0;
		*/
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//mouseX = e.getX();
		if(mouseCaptured) {
			mouseX = e.getX();
		}
		//robot.mouseMove((int) (locationOnScreen.getX() + frame.getWidth() / 2), (int) (locationOnScreen.getY() + frame.getHeight() / 2));
	}
}
