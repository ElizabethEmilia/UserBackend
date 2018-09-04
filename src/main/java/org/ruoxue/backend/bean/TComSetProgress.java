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
@TableName("t_com_set_progress")
public class TComSetProgress extends Model<TComSetProgress> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cid;
	private Integer uid;
	private Date tm;
	private Integer status;
	private String note;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Date getTm() {
		return tm;
	}

	public void setTm(Date tm) {
		this.tm = tm;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TComSetProgress{" +
			"id=" + id +
			", cid=" + cid +
			", uid=" + uid +
			", tm=" + tm +
			", status=" + status +
			", note=" + note +
			"}";
	}
}
