package com.mycompany.fisicalab.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static UserManager instance; // Instancia Singleton
    private static final String USERS_FILE = "users.dat";
    public static final int MAX_USERS = 10;
    private Map<String, String> users; // username -> password
    private String currentUser;

    private UserManager() { // Constructor privado para Singleton
        users = new HashMap<>();
        loadUsers();
    }

    public static UserManager getInstance() { // Método para obtener la instancia Singleton
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (Map<String, String>) ois.readObject();
            // Cargar el usuario actual si existe
            if (ois.available() > 0) {
                currentUser = (String) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de usuarios no encontrado. Se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            // Guardar el usuario actual
            oos.writeObject(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        if (users.size() >= MAX_USERS) {
            System.out.println("Límite de usuarios alcanzado (" + MAX_USERS + ").");
            return false;
        }
        if (users.containsKey(username)) {
            System.out.println("El nombre de usuario ya existe.");
            return false;
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            saveUsers(); // Guardar la sesión del usuario
            return true;
        }
        return false;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        saveUsers(); // Guardar que no hay usuario logueado
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
    
    public int getUserCount() {
        return users.size();
    }
}
