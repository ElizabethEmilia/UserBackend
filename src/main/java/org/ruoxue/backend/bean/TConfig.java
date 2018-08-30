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
 * @since 2018-08-30
 */
@TableName("t_config")
public class TConfig extends Model<TConfig> {

    private static final long serialVersionUID = 1L;

	private String name;
	private String value;


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

	@Override
	protected Serializable pkVal() {
		return this.name;
	}

	@Override
	public String toString() {
		return "TConfig{" +
			"name=" + name +
			", value=" + value +
			"}";
	}
}
