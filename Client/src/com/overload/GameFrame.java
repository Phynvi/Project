package com.overload;

import com.apple.eawt.Application;
import com.overload.sign.Signlink;
import com.overload.util.OSValidator;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public final class GameFrame extends Frame {

	private static final long serialVersionUID = 1L;
	protected final Insets insets;
	private final GameApplet applet;
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Dimension screenSize = toolkit.getScreenSize();
	public int screenWidth = (int) screenSize.getWidth();
	public int screenHeight = (int) screenSize.getHeight();

	public GameFrame(GameApplet applet, int width, int height, boolean resizable, boolean fullscreen) {
		this.applet = applet;
		setTitle(Configuration.CLIENT_NAME);
		setResizable(resizable);
		setUndecorated(fullscreen);
		setVisible(true);
		insets = getInsets();
		if (resizable) {
			setMinimumSize(new Dimension(766 + insets.left + insets.right, 536 + insets.top + insets.bottom));
		}
		setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
		setLocationRelativeTo(null);
		setBackground(Color.BLACK);
		requestFocus();
		setIcons();
		toFront();
	}
	
	 ArrayList<Image> icons = new ArrayList<Image>();
	
	public void setIcons() {
		Image icon128 = null;
		Image icon64 = null;
		Image icon32 = null;
		Image icon16 = null;
		Image MacIcon = null;
		
		try {
			icon128 = ImageIO.read(new File(Signlink.getDataDirectory() + File.separator + "128x128.png"));
			icon64 = ImageIO.read(new File(Signlink.getDataDirectory() + File.separator + "64x64.png"));
			icon32 = ImageIO.read(new File(Signlink.getDataDirectory() + File.separator + "32x32.png"));
			icon16 = ImageIO.read(new File(Signlink.getDataDirectory() + File.separator + "16x16.png"));
			MacIcon = ImageIO.read(new File(Signlink.getDataDirectory() + File.separator + "128x128.png"));
			icons.add(icon128);
			icons.add(icon64);
			icons.add(icon32);
			icons.add(icon16);
			if (OSValidator.isWindows()) {
	            this.setIconImages(icons);
	        } else if (OSValidator.isMac()) {
	            Application application = Application.getApplication();
	            application.setDockIconImage(MacIcon);
	        }
		}catch(Exception e) {
			System.out.println("Could not Set Icons.");
		}
	}

	/*public void setIcon(Image image) {
		if (OSValidator.isWindows()) {
            this.setIconImage(image);
        } else if (OSValidator.isMac()) {
            Application application = Application.getApplication();
            application.setDockIconImage(image);
        }
	}*/

	public Graphics getGraphics() {
		final Graphics graphics = super.getGraphics();
		Insets insets = this.getInsets();
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.translate(insets != null ? insets.left : 0, insets != null ? insets.top : 0);
		return graphics;
	}

	public int getFrameWidth() {
		Insets insets = this.getInsets();
		return getWidth() - (insets.left + insets.right);
	}

	public int getFrameHeight() {
		Insets insets = this.getInsets();
		return getHeight() - (insets.top + insets.bottom);
	}

	public void update(Graphics graphics) {
		applet.update(graphics);
	}

	public void paint(Graphics graphics) {
		applet.paint(graphics);
	}
}