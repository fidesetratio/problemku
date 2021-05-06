package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5327050062461556302L;
	public Integer id;
	public Date create_date;
	public String path_file;
	public String file_name;
	public String file_type;
	public String title;
	public Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getId() {
		return id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public String getPath_file() {
		return path_file;
	}

	public String getFile_name() {
		return file_name;
	}

	public String getFile_type() {
		return file_type;
	}

	public String getTitle() {
		return title;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public void setPath_file(String path_file) {
		this.path_file = path_file;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}