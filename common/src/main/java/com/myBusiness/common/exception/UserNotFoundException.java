package com.myBusiness.common.exception;

import java.io.Serializable;

public class UserNotFoundException extends Exception implements Serializable {

    private static final long serialVersionUID = -5141995512603598054L;

    public UserNotFoundException(long userId) {
        super("用户"+userId+"不存在");
    }
}
