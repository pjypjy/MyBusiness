package com.myBusiness.products.restaurant.service;

import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.entity.SaleCommentEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public interface SaleSituationService {

    /**
     * 获取销量、评价的统计信息
     * @param goodsId
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseResult getGoodsSaleSituation(int goodsId, LocalDate startDate, LocalDate endDate);

    /**
     * 支付成功的回调成功时，更新销量
     * @param situationObjList
     */
    public void updateSaleSituation(List<SituationObj> situationObjList);

    /**
     * 订单完成后用户评价，分级、评论
     * @param orderId
     * @param goodsId
     * @param score
     * @param comment
     * @return
     * @throws Exception
     */
    public ResponseResult evaluateGoodsInOrder(long orderId,int goodsId, int score,String comment) throws Exception;

    public SituationObj genUpdateSituationObj(int goodsId, LocalDate localDate, int amount,int score);

    /**
     * 追评
     * @param orderId
     * @param goodsId
     * @param review
     * @return
     */
    public ResponseResult reviewGoodsInOrder(Long orderId, Integer goodsId, String review);

    /**
     * @param orderId 订单id
     * @return
     */
    public ResponseResult queryEvaluateByOrderId(long orderId);

    public SaleCommentEntity queryEvaluateById(int goodsId, long orderId);

    public List<SaleCommentEntity> queryEvaluateByGoodsId(int goodsId, Long minOrderId, int limit);

    public class SituationObj implements Serializable {
        private static final long serialVersionUID = 6459162330718870239L;
        private Integer goodsId;
        private LocalDate date;
        private Integer amount;
        private Integer score;
        public SituationObj(Integer goodsId, LocalDate date, int amount,int score){
            this.goodsId = goodsId;
            this.date = date;
            this.amount = amount;
            this.score = score;
        }

        public Integer getGoodsId() {
            return goodsId;
        }

        public LocalDate getDate() {
            return date;
        }

        public Integer getAmount() {
            return amount;
        }

        public Integer getScore() {
            return score;
        }
    }
}
