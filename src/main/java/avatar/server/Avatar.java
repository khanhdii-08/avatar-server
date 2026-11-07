package avatar.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Avatar {

    public static void main(String[] args) {
        avatar.AvatarApplication.main(args);
    }

    public static void printBanner() {
        System.out.println("     _                      _                      ____                                      \n    / \\    __   __   __ _  | |_    __ _   _ __    / ___|    ___   _ __  __   __   ___   _ __ \n   / _ \\   \\ \\ / /  / _` | | __|  / _` | | '__|   \\___ \\   / _ \\ | '__| \\ \\ / /  / _ \\ | '__|\n  / ___ \\   \\ V /  | (_| | | |_  | (_| | | |       ___) | |  __/ | |     \\ V /  |  __/ | |   \n /_/   \\_\\   \\_/    \\__,_|  \\__|  \\__,_| |_|      |____/   \\___| |_|      \\_/    \\___| |_|   \n                                                                                             ");
    }

    /**
     * Starts the socket server without Spring. The Spring Boot entry point is {@code avatar.AvatarApplication}.
     */
    public static void start() {
        Thread serverThread = new Thread(() -> {
            printBanner();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown Server!");
                ServerManager.stop();
            }));
            ServerManager.init();
            ServerManager.start();
        });
        serverThread.start();
    }

    /**
     * convert .png image to byteArray
     *
     * @param url -> img path
     * @return -> arr image
     */
    public static byte[] getFile(String url) {
        try {

            FileInputStream fis = new FileInputStream(url);
            byte[] ab = new byte[fis.available()];
            fis.read(ab, 0, ab.length);
            fis.close();
            return ab;
        } catch (IOException e) {
            return null;
        }
    }

    public static int getFileSize(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            int size = fis.available();
            fis.close();
            return size;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveFile(String url, byte[] ab) {
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(url);
            fos.write(ab);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
