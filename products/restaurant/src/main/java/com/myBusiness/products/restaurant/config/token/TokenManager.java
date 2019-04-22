package com.myBusiness.products.restaurant.config.token;

public interface TokenManager {

    public void put(String token,UserToken userToken);

    public UserToken get();

    public UserToken get(String token);

    public long getUserId(String token);

    public long getUserId();

    public boolean isAvailable(String token);

    public void delete(String token);

    public void refreshExpireDate(String token);
}
