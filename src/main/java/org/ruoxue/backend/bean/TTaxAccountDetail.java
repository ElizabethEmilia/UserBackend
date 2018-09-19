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
 * @since 2018-09-19
 */
@TableName("t_tax_account_detail")
public class TTaxAccountDetail extends Model<TTaxAccountDetail> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer cid;
	private Integer rangeid;
	@TableField("receipt_amount")
	private Double receiptAmount;
	@TableField("pre_tax_retio")
	private Double preTaxRetio;
	private Integer type;
	@TableField("change_amount")
	private Double changeAmount;
	@TableField("change_type")
	private Integer changeType;
	@TableField("account_type")
	private Integer accountType;
	@TableField("bank_account")
	private String bankAccount;
	@TableField("tm_op")
	private Date tmOp;


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

	public Integer getRangeid() {
		return rangeid;
	}

	public void setRangeid(Integer rangeid) {
		this.rangeid = rangeid;
	}

	public Double getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Double getPreTaxRetio() {
		return preTaxRetio;
	}

	public void setPreTaxRetio(Double preTaxRetio) {
		this.preTaxRetio = preTaxRetio;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(Double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Date getTmOp() {
		return tmOp;
	}

	public void setTmOp(Date tmOp) {
		this.tmOp = tmOp;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TTaxAccountDetail{" +
			"id=" + id +
			", cid=" + cid +
			", rangeid=" + rangeid +
			", receiptAmount=" + receiptAmount +
			", preTaxRetio=" + preTaxRetio +
			", type=" + type +
			", changeAmount=" + changeAmount +
			", changeType=" + changeType +
			", accountType=" + accountType +
			", bankAccount=" + bankAccount +
			", tmOp=" + tmOp +
			"}";
	}
}
