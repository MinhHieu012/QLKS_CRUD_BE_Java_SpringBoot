package vn.eledevo.vksbe.exception;

import vn.eledevo.vksbe.constant.ResponseMessage;

public class UserAccountLockedException extends Exception {
    public UserAccountLockedException(String message) {
        super(message);
    }
}
