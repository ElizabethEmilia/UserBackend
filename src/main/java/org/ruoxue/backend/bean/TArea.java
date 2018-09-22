package org.ruoxue.backend.bean;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2018-09-22
 */
@TableName("t_area")
public class TArea extends Model<TArea> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer code2;
	private Integer code4;
	private Integer code6;
	@TableField("province_full_name")
	private String provinceFullName;
	private String province;
	private String city;
	private String district;
	private String fullname;
	private Integer revoked;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode2() {
		return code2;
	}

	public void setCode2(Integer code2) {
		this.code2 = code2;
	}

	public Integer getCode4() {
		return code4;
	}

	public void setCode4(Integer code4) {
		this.code4 = code4;
	}

	public Integer getCode6() {
		return code6;
	}

	public void setCode6(Integer code6) {
		this.code6 = code6;
	}

	public String getProvinceFullName() {
		return provinceFullName;
	}

	public void setProvinceFullName(String provinceFullName) {
		this.provinceFullName = provinceFullName;
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

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getRevoked() {
		return revoked;
	}

	public void setRevoked(Integer revoked) {
		this.revoked = revoked;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TArea{" +
			"id=" + id +
			", code2=" + code2 +
			", code4=" + code4 +
			", code6=" + code6 +
			", provinceFullName=" + provinceFullName +
			", province=" + province +
			", city=" + city +
			", district=" + district +
			", fullname=" + fullname +
			", revoked=" + revoked +
			"}";
	}
}
