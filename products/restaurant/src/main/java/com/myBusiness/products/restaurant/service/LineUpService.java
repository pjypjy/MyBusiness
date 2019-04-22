package com.myBusiness.products.restaurant.service;

public interface LineUpService {

    /**
     * 增加一个排队的人
     * @param userId 用户id
     * @return 成功/失败
     */
    public Long inLineUp(long userId);

    /**
     * 将序号最小的人拿出来
     * @return null-取不到人，否则返回userId
     */
    public Long outLineUp();

    /**
     * 重置序号
     * @return
     */
    public void resetLineUp();

    /**
     * 查询序号最小的人
     * @return null-取不到人，否则返回userId
     */
    public Long next();
}
