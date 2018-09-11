package org.ruoxue.backend.bean;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengjb
 * @since 2018-09-11
 */
@TableName("t_receipt_stat")
public class TReceiptStat extends Model<TReceiptStat> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer uid;
	private Integer cid;
	private Double income12;
	@TableField("amount_normal")
	private Double amountNormal;
	@TableField("time_normal")
	private Integer timeNormal;
	@TableField("count_normal")
	private Integer countNormal;
	@TableField("amount_spec")
	private Double amountSpec;
	@TableField("time_spec")
	private Integer timeSpec;
	@TableField("count_spec")
	private Integer countSpec;
	@TableField("cur_pretax")
	private Double curPretax;
	private Integer aid;
	@TableField("tm_modify")
	private Date tmModify;
	@TableField("hash_original")
	private String hashOriginal;


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

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Double getIncome12() {
		return income12;
	}

	public void setIncome12(Double income12) {
		this.income12 = income12;
	}

	public Double getAmountNormal() {
		return amountNormal;
	}

	public void setAmountNormal(Double amountNormal) {
		this.amountNormal = amountNormal;
	}

	public Integer getTimeNormal() {
		return timeNormal;
	}

	public void setTimeNormal(Integer timeNormal) {
		this.timeNormal = timeNormal;
	}

	public Integer getCountNormal() {
		return countNormal;
	}

	public void setCountNormal(Integer countNormal) {
		this.countNormal = countNormal;
	}

	public Double getAmountSpec() {
		return amountSpec;
	}

	public void setAmountSpec(Double amountSpec) {
		this.amountSpec = amountSpec;
	}

	public Integer getTimeSpec() {
		return timeSpec;
	}

	public void setTimeSpec(Integer timeSpec) {
		this.timeSpec = timeSpec;
	}

	public Integer getCountSpec() {
		return countSpec;
	}

	public void setCountSpec(Integer countSpec) {
		this.countSpec = countSpec;
	}

	public Double getCurPretax() {
		return curPretax;
	}

	public void setCurPretax(Double curPretax) {
		this.curPretax = curPretax;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Date getTmModify() {
		return tmModify;
	}

	public void setTmModify(Date tmModify) {
		this.tmModify = tmModify;
	}

	public String getHashOriginal() {
		return hashOriginal;
	}

	public void setHashOriginal(String hashOriginal) {
		this.hashOriginal = hashOriginal;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TReceiptStat{" +
			"id=" + id +
			", uid=" + uid +
			", cid=" + cid +
			", income12=" + income12 +
			", amountNormal=" + amountNormal +
			", timeNormal=" + timeNormal +
			", countNormal=" + countNormal +
			", amountSpec=" + amountSpec +
			", timeSpec=" + timeSpec +
			", countSpec=" + countSpec +
			", curPretax=" + curPretax +
			", aid=" + aid +
			", tmModify=" + tmModify +
			", hashOriginal=" + hashOriginal +
			"}";
	}
}
