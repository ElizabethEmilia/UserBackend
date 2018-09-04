package org.ruoxue.backend.bean;

import java.math.BigDecimal;
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
@TableName("t_exchange")
public class TExchange extends Model<TExchange> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer running;
	private Integer uid;
	private Integer cid;
	private BigDecimal amount;
	private Integer paymethod;
	private String note;
	private Date tm;
	private Integer state;
	private Integer type;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRunning() {
		return running;
	}

	public void setRunning(Integer running) {
		this.running = running;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(Integer paymethod) {
		this.paymethod = paymethod;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getTm() {
		return tm;
	}

	public void setTm(Date tm) {
		this.tm = tm;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TExchange{" +
			"id=" + id +
			", running=" + running +
			", uid=" + uid +
			", cid=" + cid +
			", amount=" + amount +
			", paymethod=" + paymethod +
			", note=" + note +
			", tm=" + tm +
			", state=" + state +
			", type=" + type +
			"}";
	}
}
