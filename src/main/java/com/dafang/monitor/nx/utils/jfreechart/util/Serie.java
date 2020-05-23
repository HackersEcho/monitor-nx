package com.dafang.monitor.nx.utils.jfreechart.util;

import java.io.Serializable;
import java.util.Vector;

/**
 * ϵ��:���ֺ����ݼ��� ����һ������</br> ���Խ�serie����һ���߻���һ�����ӣ�
 * 
 * <p>
 * ����JSͼ�����������ݣ�</br> series: [{ name: 'Tokyo', data: [7.0, 6.9, 9.5, 14.5]
 * },</br> { name: 'New York', data: [-0.2, 0.8, 5.7, 11.3} ]</br>
 * </p>
 * 
 * @author ccw
 * @date 2014-6-4
 */
public class Serie implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;// ����
	private Vector<Object> data;// ����ֵ

	public Serie() {

	}

	/**
	 * 
	 * @param name
	 *            ���ƣ��������ƣ�
	 * @param data
	 *            ���ݣ������ϵ���������ֵ��
	 */
	public Serie(String name, Vector<Object> data) {

		this.name = name;
		this.data = data;
	}

	/**
	 * 
	 * @param name
	 *            ���ƣ��������ƣ�
	 * @param array
	 *            ���ݣ������ϵ���������ֵ��
	 */
	public Serie(String name, Object[] array) {
		this.name = name;
		if (array != null) {
			data = new Vector<Object>(array.length);
			for (int i = 0; i < array.length; i++) {
				data.add(array[i]);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<Object> getData() {
		return data;
	}

	public void setData(Vector<Object> data) {
		this.data = data;
	}

}
