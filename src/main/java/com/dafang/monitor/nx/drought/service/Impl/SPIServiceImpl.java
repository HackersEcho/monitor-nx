package com.dafang.monitor.nx.drought.service.Impl;

import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.mapper.DroughtMapper;
import com.dafang.monitor.nx.drought.service.SPIService;
import com.dafang.monitor.nx.utils.Gamma;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SPIServiceImpl implements SPIService {

    @Autowired
    DroughtMapper mapper;
    private static final double C0 = 2.515517;
    private static final double C1 = 0.802853;
    private static final double C2 = 0.010328;
    private static final double D1 = 1.432788;
    private static final double D2 = 0.189269;
    private static final double D3 = 0.001308;
    private static double dataCounter, zeroCounter;//降水日数，无降水日数
    private static double para_A, para_Gamma, para_Beta;

    @Override
    public List<Map<String, Object>> getContinueSPI(DroughtParam params) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Drought> preList = mapper.getContinueData(params);
        List<String> stationNos = preList.stream().map(Drought::getStationNo).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            Map<String,Object> map = new HashMap<>();
            double sum = 0.0,temp = 0.0;//降水总和，降水对数总和
            List<Drought> singleList = preList.stream().filter(x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
            Drought drought = singleList.get(0);
            dataCounter = singleList.stream().filter(x->x.getVal() >= 0.1).count();
            zeroCounter = singleList.stream().filter(x->x.getVal() < 0.1).count();
            sum = singleList.stream().filter(x -> x.getVal() >= 0.1).mapToDouble(Drought::getVal).summaryStatistics().getSum();
            temp = singleList.stream().filter(x -> x.getVal() >= 0.1).mapToDouble(x->Math.log(x.getVal())).summaryStatistics().getSum();
            if(dataCounter!=zeroCounter){
                double xAver = NumberUtil.div(sum,(dataCounter-zeroCounter));
                para_A = Math.log(xAver); // para_A = ln(perciption的平均数)
                para_A = para_A - NumberUtil.div(temp,(dataCounter-zeroCounter)); // A = ln(perciption的平均数) - Sum(ln(perciption))
                // 最佳的α、β 估计值可以采用的极大似然数估计法求得
                para_Gamma = (1 + Math.log(1 + 4 * para_A / 3)) / (4 * para_A);//α>0 为形状参数
                para_Beta = xAver / para_Gamma;//β>0为尺度参数
            }
            OptionalDouble optional = singleList.stream().filter(x -> StringUtils.equals(x.getObserverTime().toString(), params.getEndDate())).
                    mapToDouble(Drought::getVal).findFirst();
            double perciption = 0.0,spi = 0.0;
            if (optional.isPresent()){
                perciption = optional.getAsDouble();
            }
            spi = getSPI(perciption);
            map.put("stationNo",stationNo);
            map.put("stationName",drought.getStationName());
            map.put("longitude",drought.getLongitude());
            map.put("latitude",drought.getLatitude());
            map.put("spi",spi);
            if (spi > -0.5) {
                map.put("level", "无旱");
            } else if (spi > -1.0 && spi <= -0.5) {
                map.put("level", "轻旱");
            } else if (spi > -1.5 && spi <= -1) {
                map.put("level", "中旱");
            } else if (spi > -2.0 && spi <= -1.5) {
                map.put("level", "重旱");
            } else {
                map.put("level", "特旱");
            }
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getPeriodSPI(DroughtParam params) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Drought> preList = mapper.getPeriodData(params);
        List<String> stationNos = preList.stream().map(Drought::getStationNo).distinct().collect(Collectors.toList());
        List<Integer> years = preList.stream().map(Drought::getYear).distinct().collect(Collectors.toList());
        int sYear = Integer.parseInt(params.getStartDate().substring(0,4));
        int eYear = Integer.parseInt(params.getEndDate().substring(0,4));
        for (int i = sYear; i <= eYear; i++){
            int year = i;
            for (String stationNo : stationNos) {
                Map<String,Object> map = new HashMap<>();
                double sum = 0.0,temp = 0.0;//降水总和，降水对数总和
                List<Drought> singleList = preList.stream().filter(x -> StringUtils.equals(stationNo, x.getStationNo())
                        && x.getYear().equals(year)).collect(Collectors.toList());
                Drought drought = singleList.get(0);
                dataCounter = singleList.stream().filter(x->x.getVal() >= 0.1).count();
                zeroCounter = singleList.stream().filter(x->x.getVal() < 0.1).count();
                sum = singleList.stream().filter(x -> x.getVal() >= 0.1).mapToDouble(Drought::getVal).summaryStatistics().getSum();
                temp = singleList.stream().filter(x -> x.getVal() >= 0.1).mapToDouble(x->Math.log(x.getVal())).summaryStatistics().getSum();
                if(dataCounter!=zeroCounter){
                    double xAver = NumberUtil.div(sum,(dataCounter-zeroCounter));
                    para_A = Math.log(xAver); // para_A = ln(perciption的平均数)
                    para_A = para_A - NumberUtil.div(temp,(dataCounter-zeroCounter)); // A = ln(perciption的平均数) - Sum(ln(perciption))
                    // 最佳的α、β 估计值可以采用的极大似然数估计法求得
                    para_Gamma = (1 + Math.log(1 + 4 * para_A / 3)) / (4 * para_A);//α>0 为形状参数
                    para_Beta = xAver / para_Gamma;//β>0为尺度参数
                }
                OptionalDouble optional = singleList.stream().filter(x -> StringUtils.equals(x.getObserverTime().toString().replaceAll("-","")
                        .substring(4,8), params.getET())).mapToDouble(Drought::getVal).findFirst();
                double perciption = 0.0,spi = 0.0;
                if (optional.isPresent()){
                    perciption = optional.getAsDouble();
                }
                spi = getSPI(perciption);
                map.put("stationNo",stationNo);
                map.put("stationName",drought.getStationName());
                map.put("year",year);
                map.put("longitude",drought.getLongitude());
                map.put("latitude",drought.getLatitude());
                map.put("spi",spi);
                if (spi > -0.5) {
                    map.put("level", "无旱");
                } else if (spi > -1.0 && spi <= -0.5) {
                    map.put("level", "轻旱");
                } else if (spi > -1.5 && spi <= -1) {
                    map.put("level", "中旱");
                } else if (spi > -2.0 && spi <= -1.5) {
                    map.put("level", "重旱");
                } else {
                    map.put("level", "特旱");
                }
                result.add(map);
            }
        }
        return result;
    }

    //根据降水量计算SPI
    public static double getSPI(double perciption) {
        double F = 0.0; // F = H(X) 累计概率
        if (perciption == 0) {
            if(zeroCounter==dataCounter){
                F = 0.0;
            }else{
                if (dataCounter != 0){
                    F = NumberUtil.div(zeroCounter,dataCounter);
                }else {
                    F = 0.0;
                }
            }
        } else {
            F = integral(0.0000001, perciption); // 求解定积分
        }
        double S = -1;
        // 0 < H(x) <= 0.5 或 0.5 < H(x) < 1，对应不同的表达式
        if (F > 0.5) {
            F = 1 - F;
            S = 1;
        }
        double t = Math.sqrt(Math.log(1 / (F * F)));
        double Z = S * (t - (C0 + C1 * t + C2 * t * t) / (1 + D1 * t + D2 * t * t + D3 * t * t * t));
        if(Double.isNaN(Z) || Double.isInfinite(Z)){
            Z = -3.0;
        }
        return NumberUtil.div(Z,1,2);
    }

    // 定积分运算
    public static double integral(double a, double b) {
        double N = 100000 * para_A * para_A;
        double s = 0, h;
        s = (f_probabilityDensity(a) + f_probabilityDensity(b)) / 2.0;
        h = (b - a) / N;
        for (int i = 1; i < N; i++)
            s += f_probabilityDensity(a + i * h);
        return (s * h);
    }

    //累积概率
    public static double f_probabilityDensity(double x) {
        double E = 2.71828;
        double t_a = 1 / (Math.pow(para_Beta, para_Gamma) * f_gamma(para_Gamma, 1000));
        double t_b = Math.pow(x, para_Gamma - 1);
        double t_c = Math.pow(E, -x / para_Beta);
        return (t_a * t_b * t_c);
    }

    // Gamma 函数
    public static double f_gamma(double z, int Nterms) {
        return Gamma.gamma(z);
    }
}
