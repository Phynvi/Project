package com.overload;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import com.overload.sign.Signlink;

public class ClientWindow extends Client {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public static int port = 43594;
	
	private static Insets insets;
	
	public ClientWindow(int width, int height, boolean resizable) {
		super();
		try {
			Signlink.startpriv(InetAddress.getByName(server));
			initUI(width, height, resizable);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private final WindowAdapter windowAdapter = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent we) {
			
			
		}
	};

	public void initUI(int width, int height, boolean resizable) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			frame = new JFrame(frameTitle);
			frame.setLayout(new BorderLayout());
			frame.setResizable(resizable);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addWindowListener(windowAdapter);
			JPanel gamePanel = new JPanel();
			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(width, height));
			frame.add(gamePanel, BorderLayout.CENTER);
			frame.pack();
			insets = frame.getInsets();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			init();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public URL getCodeBase() {
		try {
			return new URL("http://" + server + "/cache");
		} catch (Exception e) {
			return super.getCodeBase();
		}
	}

	public URL getDocumentBase() {
		return getCodeBase();
	}

	public void loadError(String s) {
		System.out.println("loadError: " + s);
	}

	public String getParameter(String key) {
		return "";
	}

	public static Insets getInset() {
		return insets;
	}
	
	public static JFrame getFrame() {
		return frame;
	}

	public static String detail = null;
	int world = 1;
	public static String setchateffects = null;
	public static String setname = null;
	public static String setchat = null;
	public static String Serverip;
	public static double version = 13.0;
	public boolean onTop = false;
	private String frameTitle = "Overload";

}