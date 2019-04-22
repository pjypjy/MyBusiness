package com.myBusiness.products.restaurant.service;

import com.myBusiness.common.exception.RoleNotFoundException;
import com.myBusiness.common.exception.UserNotFoundException;
import com.myBusiness.common.model.ResponseResult;

public interface AuthorityService {

    public boolean saveRoleForUser(long roleId, long userId) throws RoleNotFoundException, UserNotFoundException;

    public ResponseResult saveMenuBarForRole(long menuBarId, long roleId) ;

    public ResponseResult deleteMenuBarForRole(long menuBarId, long roleId) ;
}
