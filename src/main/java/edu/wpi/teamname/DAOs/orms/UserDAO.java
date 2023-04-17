package edu.wpi.teamname.DAOs.orms;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
  private static final Map<String, User> users = new HashMap<>();
  private static final Map<String, User> sessions = new HashMap<>();
  private static final SecureRandom random = new SecureRandom();

  public static void addUser(User user) {
    users.put(user.getUserName(), user);
  }

  public static User getUser(String username) {
    return users.get(username);
  }

  public static boolean authenticate(String username, String password) {
    User user = getUser(username);
    if (user == null) {
      return false;
    }
    return user.checkLogin(password);
  }

  public static String createSession(String username) {
    User user = getUser(username);
    if (user == null) {
      return null;
    }
    String sessionId = generateSessionId();
    //    user.setSessionID(sessionId);
    sessions.put(sessionId, user);
    return sessionId;
  }

  public static User getUserBySession(String sessionId) {
    return sessions.get(sessionId);
  }

  public static void removeSession(String sessionId) {
    User user = getUserBySession(sessionId);
    if (user != null) {
      //      user.setSessionID(null);
    }
    sessions.remove(sessionId);
  }

  public static boolean hasPermission(String sessionId, Permission permission) {
    User user = getUserBySession(sessionId);
    if (user == null) {
      return false;
    }

    if (permission == null) {
      return false;
    }
    return user.getPermission().equals(permission);
  }

  private static String generateSessionId() {
    byte[] bytes = new byte[16];
    random.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
