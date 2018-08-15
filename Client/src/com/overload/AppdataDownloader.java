package com.overload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import com.overload.sign.Signlink;

public class AppdataDownloader {

	private final static int BUFFER = 1024;
	
	private enum FileType {
		ADVANCED_SETTINGS(Signlink.getDataDirectory(), 	"AdvancedSettings.dat",  "AdvancedSettingsdat",   "Settings"),
		SETTINGS(		  Signlink.getDataDirectory(), 	"settings.dat",  		 "settingsdat",   		  "Settings"),
		FONTS(			  Signlink.getDataDirectory(), 	"fonts.dat",  			 "fontsdat",  			  "Fonts"),
		SPRITES_DAT(	  Signlink.getDataDirectory(), 	"sprites.dat",  		 "spritesdat",  		  "Sprites"),
		SPRITES_IDX(	  Signlink.getDataDirectory(), 	"sprites.idx",  		 "sprites.idx",  		  "Sprites");
		
		private String cacheDir, localName, serverName, downloadName, url;
		
		FileType(String cacheDir, String localName, String serverName, String downloadName) {
			this.cacheDir = cacheDir;
			this.localName = localName;
			this.serverName = serverName;
			this.downloadName = downloadName;
			this.url = "http://overloadps.com/ClientFiles/appdata/";
		}

		public String getDirectory() {
			return cacheDir;
		}

		public String getLocalFilename() {
			return localName;
		}

		public String getServerFilename() {
			return serverName;
		}
		
		public String getDownloadName() {
			return downloadName;
		}

		public String getUrl() {
			return url;
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}
		
	};

	private static int lastPercent;
	private static int lastFinalPercent = 0;
	
	private static void downloadFile(Client client, FileType type) {
		try {
			String loadingText = new File(type.getDirectory() + "/" + type.getLocalFilename()).exists() ? "Updating" : "Downloading";
			URL url = new URL(type.getUrl() + type.getServerFilename());
			URLConnection conn = url.openConnection();
			try (OutputStream out = new BufferedOutputStream(new FileOutputStream(type.getDirectory() + "/" + type.getLocalFilename())); InputStream in = conn.getInputStream()) {
				byte[] data = new byte[BUFFER];
				int numRead;
				long numWritten = 0;
				int length = conn.getContentLength();
				while ((numRead = in.read(data)) != -1) {
					out.write(data, 0, numRead);
					numWritten += numRead;
					int percentage = (int) (1 + ((((double) numWritten / (double) length) * 100D) / needsDownloadingSize) + lastFinalPercent);
					if (percentage != lastPercent) {
						client.drawLoadingText(2, loadingText + " " + type.getDownloadName() + " " + percentage + "%");
						lastPercent = percentage;
					}
				}
				lastFinalPercent = lastPercent;
			}
		} catch (IOException ex) {
			handleException(ex);
		}
	}
	
	public static String getNewestVersion(FileType type) {
		String md5 = null;
		try (InputStream input = new URL("http://overloadps.com/ClientFiles/appdata/appdata.php?file=" + type.getServerFilename()).openStream()) {
			md5 = IOUtils.toString(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return md5;
	}
	
	public static String getCurrentVersion(FileType type) {
		File file = new File(type.getDirectory() + "/" + type.getLocalFilename());
		if (!file.exists()) {
			return "";
		}
		String md5 = null;
		try (FileInputStream fis = new FileInputStream(file)) {
			md5 = DigestUtils.md5Hex(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5;
	}
	
	private static void handleException(Exception e){
		StringBuilder strBuff = new StringBuilder();
		strBuff.append("Please Screenshot this message, and post it in #Bugs on discord!\r\n\r\n");
        strBuff.append(e.getClass().getName()).append(" \"").append(e.getMessage()).append("\"\r\n");
		for(StackTraceElement s : e.getStackTrace())
			strBuff.append(s.toString()).append("\r\n");
		alert("Exception [" + e.getClass().getSimpleName() + "]",strBuff.toString(),true);
	}
	
	private static void alert(String title,String msg,boolean error){
		JOptionPane.showMessageDialog(null,
			   msg,
			   title,
			    (error ? JOptionPane.ERROR_MESSAGE : JOptionPane.PLAIN_MESSAGE));
	}
	
	static int needsDownloadingSize = 0;
	private static FileType[] needsDownloading = {
			null,
			null,
			null,
			null,
			null
	};
	
	public static void startDownload(Client client) {
		try {
			for (FileType type : needsDownloading) {
				if (type == null)
					continue;
				downloadFile(client, type);
				type = null;
			}
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	public static void start(Client client) {
		int index = 0;
		int counter = 0;
		try {
			for (FileType type : FileType.values()) {
				if (!getNewestVersion(type).equalsIgnoreCase(getCurrentVersion(type))) {
					needsDownloading[index] = type;
					index++;
				}
				client.drawLoadingText(2, "Validating Appdata" + " " + type.getDownloadName() + " " + (int) (((double) counter / (double) needsDownloading.length) * 100D) + "%");
				counter++;
			}
		} catch (Exception e) {
			handleException(e);
		}
		needsDownloadingSize = index;
		if (index > 0)
			startDownload(client);
		client.drawLoadingText(2, "Loading Fonts");
	}
}
