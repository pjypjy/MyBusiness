package com.myBusiness.common.exception;

import java.io.Serializable;

public class TokenExpireException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 6563100960081453882L;

    public TokenExpireException(String token) {
        super("token"+token+"过期");
    }
}
