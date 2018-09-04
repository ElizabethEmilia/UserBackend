package org.ruoxue.backend.bean;

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
@TableName("t_tax_account")
public class TTaxAccount extends Model<TTaxAccount> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer uid;
	private Integer cid;
	@TableField("ty_apt_ratio")
	private Double tyAptRatio;
	private Double balance;


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

	public Double getTyAptRatio() {
		return tyAptRatio;
	}

	public void setTyAptRatio(Double tyAptRatio) {
		this.tyAptRatio = tyAptRatio;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TTaxAccount{" +
			"id=" + id +
			", uid=" + uid +
			", cid=" + cid +
			", tyAptRatio=" + tyAptRatio +
			", balance=" + balance +
			"}";
	}
}
