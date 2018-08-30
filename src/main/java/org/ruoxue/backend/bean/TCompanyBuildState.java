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
@TableName("t_company_build_state")
public class TCompanyBuildState extends Model<TCompanyBuildState> {

    private static final long serialVersionUID = 1L;

	private String status;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.status;
	}

	@Override
	public String toString() {
		return "TCompanyBuildState{" +
			"status=" + status +
			"}";
	}
}
