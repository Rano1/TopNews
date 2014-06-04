package com.topnews.bean;

import java.io.Serializable;

/*
 * 城市实体类
 */
public class CityEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2005295701925847160L;
	/* 城市ID */
	public Integer id;
	/* 城市NAME */
	public String name;
	/* 城市拼音首字母 */
	public char pinyin;

	public CityEntity(Integer id, String name, char pinyin) {
		this.id = id;
		this.name = name;
		this.pinyin = pinyin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getPinyin() {
		return pinyin;
	}

	public void setPinyin(char pinyin) {
		this.pinyin = pinyin;
	}

}
