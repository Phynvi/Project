package com.overload.util;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MiscUtils {

    private static final char[] validChars = {'_', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9'};
    
    
    public static int formatColor(float f) {
		if(f <= 11.1){
			return 0xff000;
		} else if (f > 11.1 && f <= 22.2) {
			return 0xff3300;
		} else if (f > 22.2 && f <= 33.3) {
			return 0xff4d00;
		} else if (f > 33.3 && f <= 44.4) {
			return 0xff6700;
		} else if (f > 44.4 && f <= 55.5) {
			return 0xff9a00;
		} else if (f > 55.5 && f <= 66.6) {
			return 0xffce00;
		} else if (f > 66.6 && f <= 77.8) {
			return 0xe3ff00;
		} else if (f > 77.8 && f <= 88.8) {
			return 0xb0ff00;
		} else if (f > 88.8 && f <= 99.9) {
			return 0x2fff00;
		} else {
			return 0x2fff00;
		}
	}

    public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}
    
    public static void launchURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla",
                        "netscape", "safari"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]})
                            .waitFor() == 0)
                        browser = browsers[count];
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else
                    Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static long longForName(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += 1 + c - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += 1 + c - 97;
            } else if (c >= '0' && c <= '9') {
                l += 27 + c - 48;
            }
        }

        for (; l % 37L == 0L && l != 0L; l /= 37L) {
            ;
        }
        return l;
    }

    public static String nameForLong(long l) {
        try {
            if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
                return "invalid_name";
            }

            if (l % 37L == 0L) {
                return "invalid_name";
            }

            int i = 0;
            char ac[] = new char[12];

            while (l != 0L) {
                long l1 = l;
                l /= 37L;
                ac[11 - i++] = validChars[(int) (l1 - l * 37L)];
            }

            return new String(ac, 12 - i, i);
        } catch (RuntimeException runtimeexception) {
            System.out.println("81570, " + l + ", " + (byte) -99 + ", " + runtimeexception.toString());
        }

        throw new RuntimeException();
    }

    public static String fixName(String s) {
        if (s.length() > 0) {
            char ac[] = s.toCharArray();
            for (int j = 0; j < ac.length; j++) {
                if (ac[j] == '_') {
                    ac[j] = ' ';
                    if (j + 1 < ac.length && ac[j + 1] >= 'a' && ac[j + 1] <= 'z') {
                        ac[j + 1] = (char) (ac[j + 1] + 65 - 97);
                    }
                }
            }

            if (ac[0] >= 'a' && ac[0] <= 'z') {
                ac[0] = (char) (ac[0] + 65 - 97);
            }
            return new String(ac);
        } else {
            return s;
        }
    }
    

	
    public static String wrap(final String str, final int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }

    public static String wrap(final String str, final int wrapLength, final String newLineStr, final boolean wrapLongWords) {
        return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
    }
    
    public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords, String wrapOn) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = System.lineSeparator();
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        if (wrapOn == null || wrapOn == "") {
            wrapOn = " ";
        }
        final Pattern patternToWrapOn = Pattern.compile(wrapOn);
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

        while (offset < inputLineLength) {
            int spaceToWrapAt = -1;
            Matcher matcher = patternToWrapOn.matcher(str.substring(offset, Math.min(offset + wrapLength + 1, inputLineLength)));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    offset += matcher.end();
                    continue;
                }
                spaceToWrapAt = matcher.start() + offset;
            }

            // only last line without leading spaces is left
            if(inputLineLength - offset <= wrapLength) {
                break;
            }

            while(matcher.find()){
                spaceToWrapAt = matcher.start() + offset;
            }

            if (spaceToWrapAt >= offset) {
                // normal case
                wrappedLine.append(str, offset, spaceToWrapAt);
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;

            } else {
                // really long word or URL
                if (wrapLongWords) {
                    // wrap really long word one line at a time
                    wrappedLine.append(str, offset, wrapLength + offset);
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    // do not wrap really long word, just extend beyond limit
                    matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
                    if (matcher.find()) {
                        spaceToWrapAt = matcher.start() + offset + wrapLength;
                    }

                    if (spaceToWrapAt >= 0) {
                        wrappedLine.append(str, offset, spaceToWrapAt);
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str, offset, str.length());
                        offset = inputLineLength;
                    }
                }
            }
        }

        // Whatever is left in line is short enough to just pass through
        wrappedLine.append(str, offset, str.length());

        return wrappedLine.toString();
    }

}
