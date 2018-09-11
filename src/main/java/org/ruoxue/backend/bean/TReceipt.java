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
@TableName("t_receipt")
public class TReceipt extends Model<TReceipt> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer uid;
	private Integer cid;
	@TableField("rec_type")
	private Integer recType;
	@TableField("cus_name")
	private String cusName;
	@TableField("rec_amount")
	private Double recAmount;
	@TableField("pretax_ratio")
	private Double pretaxRatio;
	private Double pretax;
	private Integer status;
	private String reason;
	@TableField("tm_submit")
	private Date tmSubmit;
	@TableField("tm_vallidate")
	private Date tmVallidate;
    /**
     * 寄发票的地址
     */
	private String address;


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

	public Integer getRecType() {
		return recType;
	}

	public void setRecType(Integer recType) {
		this.recType = recType;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public Double getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(Double recAmount) {
		this.recAmount = recAmount;
	}

	public Double getPretaxRatio() {
		return pretaxRatio;
	}

	public void setPretaxRatio(Double pretaxRatio) {
		this.pretaxRatio = pretaxRatio;
	}

	public Double getPretax() {
		return pretax;
	}

	public void setPretax(Double pretax) {
		this.pretax = pretax;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getTmSubmit() {
		return tmSubmit;
	}

	public void setTmSubmit(Date tmSubmit) {
		this.tmSubmit = tmSubmit;
	}

	public Date getTmVallidate() {
		return tmVallidate;
	}

	public void setTmVallidate(Date tmVallidate) {
		this.tmVallidate = tmVallidate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TReceipt{" +
			"id=" + id +
			", uid=" + uid +
			", cid=" + cid +
			", recType=" + recType +
			", cusName=" + cusName +
			", recAmount=" + recAmount +
			", pretaxRatio=" + pretaxRatio +
			", pretax=" + pretax +
			", status=" + status +
			", reason=" + reason +
			", tmSubmit=" + tmSubmit +
			", tmVallidate=" + tmVallidate +
			", address=" + address +
			"}";
	}
}
