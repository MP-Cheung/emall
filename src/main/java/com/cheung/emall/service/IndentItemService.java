



package com.cheung.emall.service;

import java.util.List;

import com.cheung.emall.dao.IndentItemDao;
import com.cheung.emall.pojo.Indent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cheung.emall.pojo.IndentItem;

/**
 * 作用：
 * 1.计算每个订单中的每个商品的数目。<br>
 * 2.计算每个订单的总金额。<br>
 * 3.展示订单中每个商品的缩略图。<br>
 * 4.为每个订单实体，设置对应的订单项因为数据库中，
 * 订单实体没有对应的订单项属性，而订单和订单项属于一对多关系。<br>
 * @author 张卓宏
 * @version 1.0
 */
@Service
public class IndentItemService {
    @Autowired IndentItemDao indentItemDao;
    @Autowired GoodImageService goodImageService;


    public List<IndentItem> findByIndent(Indent indent) {
        return  indentItemDao.findByIndentOrderByIdDesc(indent);
    }
    /**
     * 为多个订单设置订单项
     * @param indents List
     * @return void
     */
    public void setMultipleIndent(List<Indent> indents){
        for (Indent indent : indents) {
            setSingleIndent(indent);
        }
    }
    /**
     * 为单个订单(Indnet)设置订单项(IndnetItem)
     * 1.计算订单的商品数目
     * 2.计算订单总金额
     * 3.设置订单项的商品缩略图
     * @param indent Indent
     * @return void
     */
    public void setSingleIndent(Indent indent){
        List<IndentItem> indentItems = indentItemDao.findByIndentOrderByIdDesc(indent);
        float eachItemTotalPrice = 0;
        int eachItemAmount = 0;
        for (IndentItem indentItem : indentItems) {
            eachItemTotalPrice += indentItem.getNumber() * indentItem.getGood().getPromotePrice();
            eachItemAmount = indentItem.getNumber();
            goodImageService.findShrinkImage(indentItem.getGood());
        }
        indent.setIndentItems(indentItems);
        indent.setEachItemAmount(eachItemAmount);
        indent.setEachItemTotalPrice(eachItemTotalPrice);
        
    }
    
}