package com.myBusiness.common.exception;

import java.io.Serializable;

public class MenuBarNotFoundException extends Exception implements Serializable {

    private static final long serialVersionUID = 4823103399637768580L;

    public MenuBarNotFoundException(long menuId) {
        super("菜单"+menuId+"不存在");
    }
}
