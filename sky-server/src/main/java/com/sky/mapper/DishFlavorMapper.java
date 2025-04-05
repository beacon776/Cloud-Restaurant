package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

	void insertBatch(List<DishFlavor> flavors);

	/**
	 * 根据菜品id来删除对应的口味数据
	 * @param DishId
	 */
	@Delete("DELETE FROM dish_flavor WHERE dish_id = #{dishId}")
	void deleteByDishId(Long DishId);

	void deleteByDishIds(@Param("dishIds") List<Long> dishIds);

	@Select("SELECT * FROM dish_flavor WHERE dish_id = #{dishId}")
	List<DishFlavor> getByDishId(Long dishId);
}
