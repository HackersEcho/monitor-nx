package com.dafang.monitor.nx.product.entity.emun;

import com.dafang.monitor.nx.product.impl.ClimateInfo;

public enum ProductEmun {

    PRODUCT1(3,"重要气候信息");

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
