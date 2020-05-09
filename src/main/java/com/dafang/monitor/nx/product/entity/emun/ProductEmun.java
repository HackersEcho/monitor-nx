package com.dafang.monitor.nx.product.entity.emun;

import com.dafang.monitor.nx.product.impl.ClimateInfo;

public enum ProductEmun {

    PRODUCT1(1,"气候概况"),
    PRODUCT2(2,"宁夏气象干旱监测报告"),
    PRODUCT3(3,"重要气候信息"),
    PRODUCT4(4,"极端天气气候事件监测报告"),
    PRODUCT5(5,"决策服务材料"),
    PRODUCT6(6,"专题报告");

    private Integer id;
    private String fileName;

    ProductEmun(Integer id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ProductEmun{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    /*
    根据id找到产品名
     */
    public static String getFileName(Integer id){
        String fileName = "";
        for (ProductEmun value : ProductEmun.values()) {
            if(value.id == id){
                fileName = value.fileName;
            }
        }
        return fileName;
    }

}
