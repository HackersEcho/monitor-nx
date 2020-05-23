package com.dafang.monitor.nx.product.entity.emun;

public enum ProductEmun {

    PRODUCT1(1,"气候概况"),
    PRODUCT2(2,"宁夏气象干旱监测报告"),
    PRODUCT3(3,"重要气候信息"),
    PRODUCT4(4,"极端天气气候事件监测报告"),
    PRODUCT5(5,"决策服务材料"),
    PRODUCT6(6,"专题报告"),
    PRODUCT7(7,"月气候影响评价"),
    PRODUCT8(8,"季气候影响评价"),
    PRODUCT9(9,"年气候影响评价"),
    PRODUCT10(10,"气候与人体舒适度"),
    PRODUCT11(11,"气候与水资源"),
    PRODUCT12(12,"气候与农业"),
    PRODUCT13(13,"气候与植被"),
    PRODUCT14(14,"气候与电力"),
    PRODUCT15(15,"气候与空气质量"),
    PRODUCT16(16, "气候与交通"),
    PRODUCT17(17, "气候与旅游"),
    PRODUCT18(18, "宁夏变化评估报告"),
    PRODUCT19(19, "气候对其他行业的影响");

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
