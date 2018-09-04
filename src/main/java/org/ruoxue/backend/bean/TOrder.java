package org.ruoxue.backend.bean;

import java.math.BigDecimal;
import java.util.Date;
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
 * @since 2018-09-04
 */
@TableName("t_order")
public class TOrder extends Model<TOrder> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cid;
	private String type;
	private BigDecimal amount;
	private Integer status;
	@TableField("tm_create")
	private Date tmCreate;
	@TableField("tm_paid")
	private Date tmPaid;


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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTmCreate() {
		return tmCreate;
	}

	public void setTmCreate(Date tmCreate) {
		this.tmCreate = tmCreate;
	}

	public Date getTmPaid() {
		return tmPaid;
	}

	public void setTmPaid(Date tmPaid) {
		this.tmPaid = tmPaid;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TOrder{" +
			"id=" + id +
			", cid=" + cid +
			", type=" + type +
			", amount=" + amount +
			", status=" + status +
			", tmCreate=" + tmCreate +
			", tmPaid=" + tmPaid +
			"}";
	}
}
