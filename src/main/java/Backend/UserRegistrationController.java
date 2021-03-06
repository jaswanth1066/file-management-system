package Backend;
//Author: @Smit_Thakkar
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import Exception.AuthException;

public final class UserRegistrationController {

    private void validateUser(final User user)
            throws AuthException {
        final boolean isUserNameValid = ValidationUtil.isUserNameValid(user.getUserName());
        if (!isUserNameValid) {
            throw new AuthException("Invalid username entered!");
        }
        final boolean isEmailValid = ValidationUtil.isEmailValid(user.getEmail());
        if (!isEmailValid) {
            throw new AuthException("Invalid email entered!");
        }
        final boolean isPasswordValid = ValidationUtil.isPasswordValid(user.getPassword());
        if (!isPasswordValid) {
            throw new AuthException("Invalid password entered!");
        }
        final boolean isSecurityAnswer1Valid = ValidationUtil.isSecurityAnswerValid(user.getSecurityQ1Ans());
        if (!isSecurityAnswer1Valid) {
            throw new AuthException("Invalid security answer 1!");
        }
        final boolean isSecurityAnswer2Valid = ValidationUtil.isSecurityAnswerValid(user.getSecurityQ2Ans());
        if (!isSecurityAnswer2Valid) {
            throw new AuthException("Invalid security answer 2!");
        }
        final boolean isSecurityAnswer3Valid = ValidationUtil.isSecurityAnswerValid(user.getSecurityQ3Ans());
        if (!isSecurityAnswer3Valid) {
            throw new AuthException("Invalid security answer 3!");
        }
        final boolean isSecurityAnswer4Valid = ValidationUtil.isSecurityAnswerValid(user.getSecurityQ4Ans());
        if (!isSecurityAnswer4Valid) {
            throw new AuthException("Invalid security answer 4!");
        }
    }

    private boolean validateUserExists(final User user)
            throws AuthException {
        try (final BufferedReader usersFileReader = new BufferedReader(new FileReader(Constant.USERS_FILE))) {
            String userDetails;
            while ((userDetails = usersFileReader.readLine()) != null) {
                final String[] userDetailsArr = userDetails.split(" ");
                final boolean isSameUserName = userDetailsArr[1].equals(user.getUserName());
                final boolean isSameEmail = userDetailsArr[2].equals(user.getEmail());
                if (isSameUserName || isSameEmail) {
                    return true;
                }
            }
            return false;
        } catch (final IOException e) {
            throw new AuthException("Something went wrong. Please try again after sometime!");
        }
    }

    private String getNewUser(final long userId,
                              final User user)
            throws NoSuchAlgorithmException {
        return userId + " " +
                user.getUserName() + " " +
                user.getEmail() + " " +
                securityAlgorithm.getHash(user.getPassword()) + " " +
                user.getSecurityQ1Ans() + " " +
                user.getSecurityQ2Ans() + " " +
                user.getSecurityQ3Ans() + " " +
                user.getSecurityQ4Ans() + "\n";
    }

    private boolean register(final User user)
            throws AuthException {
        try (final FileWriter fileWriter = new FileWriter(Constant.USERS_FILE, true)) {
            final long userId = Files.lines(Paths.get(Constant.USERS_FILE)).count() + 1;
            final String newUser = getNewUser(userId, user);
            fileWriter.append(newUser);
            return true;
        } catch (final IOException | NoSuchAlgorithmException e) {
            throw new AuthException("Something went wrong. Please try again after sometime!");
        }
    }

    public boolean registerUser(final User user)
            throws AuthException {
        validateUser(user);
        final boolean userExists = validateUserExists(user);
        if (userExists) {
            throw new AuthException("User exists already!");
        }
        return register(user);
    }
}