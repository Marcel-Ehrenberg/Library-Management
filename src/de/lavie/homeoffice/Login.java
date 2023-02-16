package de.lavie.homeoffice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;

public class Login {

    DataBaseConnector DB = new DataBaseConnector();

    private static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static String getSecurePassword(String password, byte[] salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private boolean userAlreadyExists(String userName) throws SQLException {
        Connection conn = DB.connectDB();

        boolean match = false;
        Statement stmt;
        ResultSet rs;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM login");

        while (rs.next()){
            String storedUser = rs.getString("user_name");

            if(storedUser.equals(userName))
            {
                match = true;
            }
        }
        conn.close();
        return match;
    }

    public void register(String userName, String passWord) throws SQLException {
        Connection conn = DB.connectDB();

        if(!userAlreadyExists(userName)) {
            byte[] salt = getSalt();
            String hash = getSecurePassword(passWord, salt);
            CallableStatement stmt = conn.prepareCall("INSERT INTO login (user_name,hash, salt) VALUES (?,?,?);");
            stmt.setString(1, userName);
            stmt.setString(2, hash);
            stmt.setBytes(3, salt);
            stmt.executeUpdate();
        }
        else
        {
            System.out.println("User already exists\n");
        }
        conn.close();
    }

    public boolean login(String userName, String passWord) throws SQLException {
        Connection conn = DB.connectDB();

        boolean match = false;
        String newHash;
        Statement stmt;
        ResultSet rs;

        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM login");

        while (rs.next()){
            String storedUser = rs.getString("user_name");
            String storedHash = rs.getString("hash");
            byte[] storedSalt = rs.getBytes("salt");
            newHash = getSecurePassword(passWord, storedSalt);

            if(userName.equals(storedUser) && newHash.equals(storedHash))
            {
               match = true;
            }
            else
            {
                System.out.print("Wrong username or password.");
            }
        }

        rs.close();
        stmt.close();
        conn.close();
        return match;
    }
}
