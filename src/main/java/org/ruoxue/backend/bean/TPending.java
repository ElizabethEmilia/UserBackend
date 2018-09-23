package org.ruoxue.backend.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengjb
 * @since 2018-09-23
 */
@TableName("t_pending")
public class TPending extends Model<TPending> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer uid;
	private Integer aid;
	private Integer gid;
	private String description;
	private Date tm;
	private Integer processed;
    /**
     * 表示是用户还是管理员
     */
	private Integer receiver;
	private Integer senderaid;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTm() {
		return tm;
	}

	public void setTm(Date tm) {
		this.tm = tm;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public Integer getReceiver() {
		return receiver;
	}

	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}

	public Integer getSenderaid() {
		return senderaid;
	}

	public void setSenderaid(Integer senderaid) {
		this.senderaid = senderaid;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TPending{" +
			"id=" + id +
			", uid=" + uid +
			", aid=" + aid +
			", gid=" + gid +
			", description=" + description +
			", tm=" + tm +
			", processed=" + processed +
			", receiver=" + receiver +
			", senderaid=" + senderaid +
			"}";
	}
}
