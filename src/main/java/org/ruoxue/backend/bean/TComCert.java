package org.ruoxue.backend.bean;

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
 * @since 2018-08-30
 */
@TableName("t_com_cert")
public class TComCert extends Model<TComCert> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cid;
	private Integer uid;
	@TableField("cert_no")
	private String certNo;
	@TableField("cert_name")
	private String certName;
	@TableField("cert_img")
	private String certImg;
	@TableField("tm_upd")
	private Date tmUpd;
	private Integer status;


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

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	public String getCertImg() {
		return certImg;
	}

	public void setCertImg(String certImg) {
		this.certImg = certImg;
	}

	public Date getTmUpd() {
		return tmUpd;
	}

	public void setTmUpd(Date tmUpd) {
		this.tmUpd = tmUpd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TComCert{" +
			"id=" + id +
			", cid=" + cid +
			", uid=" + uid +
			", certNo=" + certNo +
			", certName=" + certName +
			", certImg=" + certImg +
			", tmUpd=" + tmUpd +
			", status=" + status +
			"}";
	}
}
