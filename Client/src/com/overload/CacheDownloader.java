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


public class CacheDownloader {

	private final static int BUFFER = 1024;
	
	private static String[][] versionNames = {
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null},
		{null, null}
	};
	
	private static FileType[] needsDownloading = {
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
	};
	
	private enum FileType {
		MAIN_DAT(  Signlink.findcachedir(), "main_file_cache.dat",   "main_file_cachedat",	  "Main"),
		INDEX_0(   Signlink.findcachedir(), "main_file_cache.idx0",  "main_file_cache.idx0",  "Configs"),
		INDEX_1(   Signlink.findcachedir(), "main_file_cache.idx1",  "main_file_cache.idx1",  "Models"),
		INDEX_2(   Signlink.findcachedir(), "main_file_cache.idx2",  "main_file_cache.idx2",  "Music"),
		INDEX_3(   Signlink.findcachedir(), "main_file_cache.idx3",  "main_file_cache.idx3",  "Anims"),
		INDEX_4(   Signlink.findcachedir(), "main_file_cache.idx4",  "main_file_cache.idx4",  "Maps"),
		INDEX_5(   Signlink.findcachedir(), "main_file_cache.idx5",  "main_file_cache.idx5",  ""),
		INDEX_6(   Signlink.findcachedir(), "main_file_cache.idx6",  "main_file_cache.idx6",  ""),
		INDEX_7(   Signlink.findcachedir(), "main_file_fonts.dat",   "main_file_fontsdat",    "Fonts"),
		SPRITE_DAT(Signlink.findcachedir(), "main_file_sprites.dat", "main_file_spritesdat",  "Sprites"),
		SPRITE_IDX(Signlink.findcachedir(), "main_file_sprites.idx", "main_file_sprites.idx", "Sprites");
		
		private String cacheDir, localName, serverName, downloadName, url;
		
		FileType(String cacheDir, String localName, String serverName, String downloadName) {
			this.cacheDir = cacheDir;
			this.localName = localName;
			this.serverName = serverName;
			this.downloadName = downloadName;
			this.url = "https://eq-rsps.io/public/downloads/overload/cache/";
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

		public static FileType getTypeByLocalName(String s) {
			for (FileType type : FileType.values()) {
				if (type.getLocalFilename().equalsIgnoreCase(s)) {
					return type;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}
		
	};

	private static int lastPercent = 0;
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
						client.drawLoading(loadingText + " " + type.getDownloadName(), percentage);
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
		try (InputStream input = new URL("https://eq-rsps.io/public/downloads/overload/cache/cache.php?file=" + type.getServerFilename()).openStream()) {
			md5 = IOUtils.toString(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return md5;
	}

	public static void populateNewestVersions() {
		String[] md5 = null;
		try (InputStream input = new URL("https://eq-rsps.io/public/downloads/overload/cache/cache_all.php").openStream()) {
			md5 = IOUtils.toString(input).split("-");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int counter = 0;
		for (String splitter : md5) {
			String[] tmp = splitter.split("#");
			versionNames[counter] = new String[] { tmp[0], tmp[1] };
			counter++;
		}
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
		if (Configuration.UPDATE_CACHE) {
			int index = 0;
			int counter = 0;
			populateNewestVersions();
			try {
				for (String[] version : versionNames) {
					FileType type = FileType.getTypeByLocalName(version[0]);
					if (!version[1].equalsIgnoreCase(getCurrentVersion(type))) {
						needsDownloading[index] = type;
						index++;
					}
					client.drawLoading("Validating Cache", (int) (((double) counter / (double) needsDownloading.length) * 100D));
					counter++;
				}
				
			} catch (Exception e) {
				handleException(e);
			}
			needsDownloadingSize = index;
			if (index > 0) {
				client.drawLoading("", 100);
				client.drawLoading("", 0);
				startDownload(client);
			}
		}
	}
}
