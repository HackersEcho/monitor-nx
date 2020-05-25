package com.dafang.monitor.nx.product.impl.evaluate.climateAccessment;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;

import java.util.List;
import java.util.Map;

public class ClimateHandleDate {
	/**
	 * 平均每10年变化
	 * 斜度计算公式(y=bx+a)	平均每10年升高X℃  = (b*10)
	 * 和EXCEL斜率计算对过，结果一致
	 * @param datas 数据集合
	 * @param field	要素
	 * @param perennialValue 常年值
 	 * @return
	 */
	public static Double calSlopeData(List<Map<String, Object>> datas, String field) {
		//斜度集合
		double changeValue = 0.0d;
		if(datas!=null) {
			//年份平均值
			double avgX = datas.stream().mapToDouble(x->Double.parseDouble(x.get("id").toString())).average().getAsDouble();
			//要素值平均值
			double avgY = datas.stream().mapToDouble(x->Double.parseDouble(x.get(field).toString())).average().getAsDouble();
			double bv1 = 0.0;
			double bv2 = 0.0;
			double y = 0.0;
			for (int n = 0; n < datas.size(); n++) {
				double xi = Double.parseDouble(datas.get(n).get("id").toString());
				double yi = Double.parseDouble(datas.get(n).get(field).toString());
				bv1 += Convert.toDouble(NumberUtil.mul(NumberUtil.sub(xi,avgX), NumberUtil.sub(yi,avgY),4));
				bv2 += Convert.toDouble(NumberUtil.mul(NumberUtil.sub(xi,avgX),NumberUtil.sub(xi,avgX),4));
			}
			double b = NumberUtil.div(bv1, bv2, 4);
			double a = Convert.toDouble(NumberUtil.sub(avgY, NumberUtil.mul(b, avgX, 1), 4));
			//平均每10年变化
			changeValue = Convert.toDouble(NumberUtil.mul(b, 10, 2));
		}
		return changeValue;
	}
	/**
	 * 斜度计算公式(y=bx+a)	平均每10年升高X℃  = (b*10)
	 * 和EXCEL斜率计算对过，结果一致
	 * @param datas 数据集合
	 * @param field	要素
	 * @param perennialValue 常年值
	 * @param id 年份序列
 	 * @return
	 */
	public static Double calSlopeDataY(List<Map<String, Object>> datas, String field, int id) {
		//斜度集合
		double slope = 0.0d;
		if(datas!=null) {
			//年份平均值
			double avgX = datas.stream().mapToDouble(x->Double.parseDouble(x.get("id").toString())).average().getAsDouble();
			//要素值平均值
			double avgY = datas.stream().mapToDouble(x->Double.parseDouble(x.get(field).toString())).average().getAsDouble();
			double bv1 = 0.0;
			double bv2 = 0.0;
			double y = 0.0;
			for (int n = 0; n < datas.size(); n++) {
				double xi = Double.parseDouble(datas.get(n).get("id").toString());
				double yi = Double.parseDouble(datas.get(n).get(field).toString());
				bv1 += Convert.toDouble(NumberUtil.mul(NumberUtil.sub(xi,avgX),NumberUtil.sub(yi,avgY),4));
				bv2 += Convert.toDouble(NumberUtil.mul(NumberUtil.sub(xi,avgX),NumberUtil.sub(xi,avgX),4));
			}
			double b = NumberUtil.div(bv1, bv2, 4);
			double a = Convert.toDouble(NumberUtil.sub(avgY, NumberUtil.mul(b, avgX, 1), 4));
			
			//斜度
			slope = Convert.toDouble(NumberUtil.add(NumberUtil.mul(b, id, 4),a,2));
		}
		return slope;
	}
}
