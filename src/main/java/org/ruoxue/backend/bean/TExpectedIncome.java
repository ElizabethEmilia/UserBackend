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
@TableName("t_expected_income")
public class TExpectedIncome extends Model<TExpectedIncome> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer cid;
	private Integer uid;
	@TableField("ysa_range")
	private Integer ysaRange;
	@TableField("pre_tax_ratio")
	private Double preTaxRatio;
	private Integer status;
	@TableField("tm_activate")
	private Date tmActivate;
	@TableField("tm_inactivate")
	private Date tmInactivate;
	private String oper;
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

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getYsaRange() {
		return ysaRange;
	}

	public void setYsaRange(Integer ysaRange) {
		this.ysaRange = ysaRange;
	}

	public Double getPreTaxRatio() {
		return preTaxRatio;
	}

	public void setPreTaxRatio(Double preTaxRatio) {
		this.preTaxRatio = preTaxRatio;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTmActivate() {
		return tmActivate;
	}

	public void setTmActivate(Date tmActivate) {
		this.tmActivate = tmActivate;
	}

	public Date getTmInactivate() {
		return tmInactivate;
	}

	public void setTmInactivate(Date tmInactivate) {
		this.tmInactivate = tmInactivate;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
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
		return "TExpectedIncome{" +
			"id=" + id +
			", cid=" + cid +
			", uid=" + uid +
			", ysaRange=" + ysaRange +
			", preTaxRatio=" + preTaxRatio +
			", status=" + status +
			", tmActivate=" + tmActivate +
			", tmInactivate=" + tmInactivate +
			", oper=" + oper +
			", tmOp=" + tmOp +
			"}";
	}
}
