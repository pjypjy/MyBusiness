package com.myBusiness.common.exception;

import java.io.Serializable;

public class RoleNotFoundException extends Exception implements Serializable {

    private static final long serialVersionUID = 6634687822267802200L;

    public RoleNotFoundException(long roleId) {
        super("角色"+roleId+"不存在");
    }
}
