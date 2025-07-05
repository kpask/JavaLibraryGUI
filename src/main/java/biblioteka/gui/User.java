package biblioteka.gui; // << MOVED to core package

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a user of the library system.
 * Each user has a unique username, a hashed password for authentication,
 * and an administrator status flag (isAdmin). This class is {@link Serializable}
 * to allow user data to be persisted.
 *
 * @author kapa1135
 * @version 1.0
 * @see Serializable
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    private final String username;
    private final String passwordHash; // WARNING: Hashing method is insecure
    private final boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        // WARNING: This is NOT a secure way to store passwords!
        this.passwordHash = Integer.toString(password.hashCode());
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean checkPassword(String input) {
        // For a real application with strong hashing:
        // return BCrypt.checkpw(input, this.passwordHash);
        return Objects.equals(passwordHash, Integer.toString(input.hashCode()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}