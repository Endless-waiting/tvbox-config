package tvbox.utils;

import java.sql.*;
import java.io.File;
import java.util.Map;

public class DataBase {

    private String dbAddress = "database/";
    private String table = "playlists";
    private Tools T;
    private boolean connStat;
    private Connection conn;
    private Statement stmt;

    public DataBase() {
        T = new Tools();
        T.mkdir(dbAddress);  // 假设 Tools 类中有 mkdir 方法
        T.delFile(dbAddress);  // 假设 Tools 类中有 delFile 方法

        if (!connect()) {
            connStat = false;
        } else {
            connStat = true;
            chkTable();
        }
    }

    //@Override
    protected void close() throws Throwable {
        if (connStat) {
            disConn();
        }
    }

    public boolean connect() {
        try {
            File dbDir = new File(dbAddress);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            String dbFile = dbAddress + "db.sqlite3";
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            stmt = conn.createStatement();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void create() {
        if (!connStat) return;

        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "url TEXT, " +
                "delay INTEGER, " +
                "updatetime TEXT)";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String sql) {
        if (!connStat) return null;

        try {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean execute(String sql) {
        if (!connStat) return false;

        try {
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insert(Map<String, Object> data) {
        if (!connStat) return;

        StringBuilder keyList = new StringBuilder();
        StringBuilder valList = new StringBuilder();
        data.forEach((k, v) -> {
            keyList.append(", `").append(k).append("`");
            valList.append(", '").append(v.toString().replace("\"", "\\\"").replace("'", "''")).append("'");
        });

        String sql = "INSERT INTO " + table + " (" + keyList.substring(2) + ") VALUES (" + valList.substring(2) + ")";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void edit(int id, Map<String, Object> data) {
        if (!connStat) return;

        StringBuilder param = new StringBuilder();
        data.forEach((k, v) -> {
            param.append(", `").append(k).append("` = '")
                    .append(v.toString().replace("\"", "\\\"").replace("'", "''"))
                    .append("'");
        });

        String sql = "UPDATE " + table + " SET " + param.substring(2) + " WHERE id = " + id;
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disConn() {
        if (!connStat) return;

        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void chkTable() {
        if (!connStat) return;

        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                create();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataBase db = new DataBase();
        // 测试连接、创建表、检查表
    }
}