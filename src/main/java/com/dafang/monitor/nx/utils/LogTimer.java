package com.dafang.monitor.nx.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;

import java.util.Date;

/**
 * @author 日志计时器
 *
 */
public class LogTimer {
	
	
	private String run_stime;//程序运行开始时间
	private String run_etime;//程序运行结束时间
	private Long run_stime_ms;//程序运行开始时间、毫秒形式
	private Long run_etime_ms;//程序运行结束时间、毫秒形式
	private double run_time; //运行时间
	private Long run_time_ms;//程序运行总耗时、毫秒形式
	
	/**
	 * 构造时
	 */
	public LogTimer() {
		this.run_stime_ms=System.currentTimeMillis();
	}
	
	/**
	 * 开始计时
	 */
	public void start(){
		this.run_stime_ms=System.currentTimeMillis();
		Date date = new Date(this.run_stime_ms);
		this.run_stime = DateUtil.dateToStr(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 截止时间
	 */
	public void end(){
		this.run_etime_ms = System.currentTimeMillis();
		Date date = new Date(this.run_stime_ms);
		this.run_etime = DateUtil.dateToStr(date, "yyyy-MM-dd HH:mm:ss");
		this.run_time_ms = run_etime_ms - run_stime_ms;
		this.run_time = Convert.toLong(NumberUtil.round(run_time_ms / 1000, 2));
	}
	
	public String getRun_stime() {
		return run_stime;
	}
	public void setRun_stime(String run_stime) {
		this.run_stime = run_stime;
	}
	public String getRun_etime() {
		return run_etime;
	}
	public void setRun_etime(String run_etime) {
		this.run_etime = run_etime;
	}
	public Long getRun_stime_ms() {
		return run_stime_ms;
	}
	public void setRun_stime_ms(Long run_stime_ms) {
		this.run_stime_ms = run_stime_ms;
	}
	public Long getRun_etime_ms() {
		return run_etime_ms;
	}
	public void setRun_etime_ms(Long run_etime_ms) {
		this.run_etime_ms = run_etime_ms;
	}
	public double getRun_time() {
		return run_time;
	}
	public void setRun_time(double run_time) {
		this.run_time = run_time;
	}
	public Long getRun_time_ms() {
		return run_time_ms;
	}
	public void setRun_time_ms(Long run_time_ms) {
		this.run_time_ms = run_time_ms;
	}
	

}
