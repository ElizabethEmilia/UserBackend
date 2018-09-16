package org.ruoxue.backend.bean;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("t_shop_items")
public class TShopItems extends Model<TShopItems> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String name;
	private Double price;
	private String description;
	private String image;
	private Integer removable;
	private Integer addyear;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getRemovable() {
		return removable;
	}

	public void setRemovable(Integer removable) {
		this.removable = removable;
	}

	public Integer getAddyear() {
		return addyear;
	}

	public void setAddyear(Integer addyear) {
		this.addyear = addyear;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TShopItems{" +
			"id=" + id +
			", name=" + name +
			", price=" + price +
			", description=" + description +
			", image=" + image +
			", removable=" + removable +
			", addyear=" + addyear +
			"}";
	}
}
