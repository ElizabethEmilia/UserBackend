package org.ruoxue.backend.bean;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
@TableName("t_logs")
public class TLogs extends Model<TLogs> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer aid;
	private Date tm;
	private String description;
	private Integer cls;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Date getTm() {
		return tm;
	}

	public void setTm(Date tm) {
		this.tm = tm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCls() {
		return cls;
	}

	public void setCls(Integer cls) {
		this.cls = cls;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TLogs{" +
			"id=" + id +
			", aid=" + aid +
			", tm=" + tm +
			", description=" + description +
			", cls=" + cls +
			"}";
	}
}
