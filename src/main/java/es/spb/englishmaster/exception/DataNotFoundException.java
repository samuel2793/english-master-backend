package es.spb.englishmaster.exception;

import es.spb.englishmaster.constants.ErrorMessages;

public class DataNotFoundException extends SHSRuntimeException {
    public static String USER_NOT_FOUND_KEY = "DATA_NOT_FOUND.USER";
    public static String TOKEN_NOT_FOUND_KEY = "DATA_NOT_FOUND.TOKEN";

    public DataNotFoundException(String message, String translationKey) {
        super(message, translationKey);
    }

    public static DataNotFoundException userNotFound() {
        return new DataNotFoundException(ErrorMessages.USER_NOT_FOUND, USER_NOT_FOUND_KEY);
    }

    public static DataNotFoundException tokenNotFound() {
        return new DataNotFoundException(ErrorMessages.TOKEN_NOT_FOUND, TOKEN_NOT_FOUND_KEY);
    }
}