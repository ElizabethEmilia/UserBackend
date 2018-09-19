package org.ruoxue.backend.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
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
 * @since 2018-09-19
 */
@TableName("t_customer")
public class TCustomer extends Model<TCustomer> {

    private static final long serialVersionUID = 1L;

	@TableId(value="uid", type= IdType.AUTO)
	private Integer uid;
	private String lid;
	private String name;
	private Integer type;
	private Integer industry;
	private String phone;
	private String email;
	private String wechat;
	private String qq;
	private String fax;
	private String province;
	private String city;
	private String district;
	private String address;
	private String avatar;
	@TableField("other_contact")
	private String otherContact;
	private Integer paid;
	private Double balance;
	@TableField("rec_type")
	private Integer recType;
	@TableField("reg_date")
	private Date regDate;
    /**
     * 用户的状态： 1-正常, 2-冻结, 3-删除
     */
	private Integer status;
	private Integer aid;


	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getRecType() {
		return recType;
	}

	public void setRecType(Integer recType) {
		this.recType = recType;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	@Override
	protected Serializable pkVal() {
		return this.uid;
	}

	@Override
	public String toString() {
		return "TCustomer{" +
			"uid=" + uid +
			", lid=" + lid +
			", name=" + name +
			", type=" + type +
			", industry=" + industry +
			", phone=" + phone +
			", email=" + email +
			", wechat=" + wechat +
			", qq=" + qq +
			", fax=" + fax +
			", province=" + province +
			", city=" + city +
			", district=" + district +
			", address=" + address +
			", avatar=" + avatar +
			", otherContact=" + otherContact +
			", paid=" + paid +
			", balance=" + balance +
			", recType=" + recType +
			", regDate=" + regDate +
			", status=" + status +
			", aid=" + aid +
			"}";
	}
}
