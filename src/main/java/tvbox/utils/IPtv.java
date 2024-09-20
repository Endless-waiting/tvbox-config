package tvbox.utils;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.sql.*;
import java.time.Instant;

public class Iptv {
    private static final String PLAYLIST_FILE = "playlists/";
    private static final String M3U8_FILE_PATH = "output/";
    private static final int DELAY_THRESHOLD = 5000;

    private Tools T;
    private DataBase DB;
    private long now;

    public Iptv() {
        this.T = new Tools();  // 假设存在一个 Tools 类
        this.DB = new DataBase();  // 假设存在一个 DataBase 类
        this.now = Instant.now().toEpochMilli();
    }

    public List<Map<String, String>> getPlaylist() {
        List<Map<String, String>> playList = new ArrayList<>();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(PLAYLIST_FILE));
            for (Path path : stream) {
                File file = path.toFile();
                if (file.isFile()) {
                    if (file.getName().endsWith(".txt")) {
                        List<String> lines = Files.readAllLines(path);
                        for (String line : lines) {
                            String[] item = line.split(",", 2);
                            if (item.length == 2) {
                                Map<String, String> data = new HashMap<>();
                                data.put("title", item[0]);
                                data.put("url", item[1]);
                                playList.add(data);
                            }
                        }
                    } else if (file.getName().endsWith(".m3u")) {
                        // 需要找到适合 Java 的 m3u8 解析库并替代 m3u8.load 方法
                        try {
                            M3U8Parser m3u8Obj = M3U8Parser.load(file.getAbsolutePath()); // 假设有 M3U8Parser 类
                            for (M3U8Segment segment : m3u8Obj.getSegments()) {
                                Map<String, String> data = new HashMap<>();
                                data.put("title", segment.getTitle());
                                data.put("url", segment.getUri());
                                playList.add(data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playList;
    }

    public boolean checkPlayList(List<Map<String, String>> playList) {
        int total = playList.size();
        if (total <= 0) return false;

        for (int i = 0; i < total; i++) {
            String tmpTitle = playList.get(i).get("title");
            String tmpUrl = playList.get(i).get("url");
            System.out.println("Checking[ " + (i + 1) + " / " + total + " ]: " + tmpTitle);

            int netstat = T.chkPlayable(tmpUrl);
            if (netstat > 0 && netstat < DELAY_THRESHOLD) {
                Map<String, Object> data = new HashMap<>();
                data.put("title", tmpTitle);
                data.put("url", tmpUrl);
                data.put("delay", netstat);
                data.put("updatetime", now);
                addData(data);
            }
        }
        return true;
    }

    public void addData(Map<String, Object> data) {
        String sql = String.format("SELECT * FROM %s WHERE title= '%s'", DB.getTable(), data.get("title"));
        try {
            List<Map<String, Object>> result = DB.query(sql);
            if (result.size() == 0) {
                DB.insert(data);
            } else {
                int oldDelay = (int) result.get(0).get("delay");
                if ((int) data.get("delay") < oldDelay) {
                    int id = (int) result.get(0).get("id");
                    DB.edit(id, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeM3U8File() {
        System.out.println(M3U8_FILE_PATH);
        T.mkdir("output");
        String sql = String.format("SELECT * FROM %s ORDER BY delay DESC", DB.getTable());

        try {
            List<Map<String, Object>> result = DB.query(sql);
            if (result.size() > 0) {
                String outputFile = M3U8_FILE_PATH + (System.currentTimeMillis()) + ".m3u";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write("#EXTM3U\n");
                    for (Map<String, Object> item : result) {
                        writer.write(String.format("#EXTINF:-1, %s\n", item.get("title")));
                        writer.write(String.format("%s\n", item.get("url")));
                    }
                    System.out.println("共获得 " + result.size() + " 个有效直播源！");
                }
            } else {
                System.out.println("无有效直播源！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Iptv iptv = new Iptv();
        System.out.println("开始......");
        iptv.checkPlayList(iptv.getPlaylist());
        iptv.writeM3U8File();
        System.out.println("结束.....");
    }
}
