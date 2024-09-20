package tvbox.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.*;
import java.util.Objects;

public class Tools {

    public Tools() {
    }

    // 删除指定路径下的所有文件和目录
    public void delFile(String path) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory()) {
                    delFile(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }
    }

    // 创建目录
    public boolean mkdir(String path) {
        // 去除首位和尾部的空格及路径符号
        path = path.trim();
        path = path.replaceAll("\\\\+$", "");

        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdirs(); // 如果目录不存在则创建
        }
        return true; // 如果目录已经存在，直接返回 true
    }

    // 检查URL是否可用，返回响应时间（毫秒），如果不可用返回0
    public int chkPlayable(String urlStr) {
        long startTime = System.currentTimeMillis();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); // 设置连接超时时间
            connection.setReadTimeout(5000); // 设置读取数据超时时间
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                long endTime = System.currentTimeMillis();
                return (int) (endTime - startTime); // 返回响应时间
            } else {
                return 0; // 非200状态码返回0
            }
        } catch (SocketTimeoutException e) {
            // 超时处理
            return 0;
        } catch (IOException e) {
            // 其他IO异常处理
            return 0;
        }
    }
}
