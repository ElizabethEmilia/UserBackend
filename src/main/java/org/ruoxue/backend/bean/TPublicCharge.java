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
 * @since 2018-09-06
 */
@TableName("t_public_charge")
public class TPublicCharge extends Model<TPublicCharge> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer uid;
	private Integer type;
	private BigDecimal amount;
	private String name;
	private String account;
	private String bank;
	private String credit;
	private Integer status;
	@TableField("tm_create")
	private Date tmCreate;
	@TableField("tm_confirm")
	private Date tmConfirm;


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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
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

	public Date getTmConfirm() {
		return tmConfirm;
	}

	public void setTmConfirm(Date tmConfirm) {
		this.tmConfirm = tmConfirm;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TPublicCharge{" +
			"id=" + id +
			", uid=" + uid +
			", type=" + type +
			", amount=" + amount +
			", name=" + name +
			", account=" + account +
			", bank=" + bank +
			", credit=" + credit +
			", status=" + status +
			", tmCreate=" + tmCreate +
			", tmConfirm=" + tmConfirm +
			"}";
	}
}
