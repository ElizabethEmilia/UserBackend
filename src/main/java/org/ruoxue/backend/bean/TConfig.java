package org.ruoxue.backend.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengjb
 * @since 2018-09-16
 */
@TableName("t_config")
public class TConfig extends Model<TConfig> {

    private static final long serialVersionUID = 1L;

	private String name;
	private String value;
	private String friendlyname;
	private Integer visible;
	private Integer type;
	private String description;
	private Integer permission;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFriendlyname() {
		return friendlyname;
	}

	public void setFriendlyname(String friendlyname) {
		this.friendlyname = friendlyname;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	@Override
	protected Serializable pkVal() {
		return this.name;
	}

	@Override
	public String toString() {
		return "TConfig{" +
			"name=" + name +
			", value=" + value +
			", friendlyname=" + friendlyname +
			", visible=" + visible +
			", type=" + type +
			", description=" + description +
			", permission=" + permission +
			"}";
	}
}
