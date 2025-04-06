package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

	List<Long> getSetmealIdsByDishIds(@Param("dishIds") List<Long> dishIds);

	void insertBatch(List<SetmealDish> setmealDishes);

	void deleteBatchBySetmealIds(List<Long> setmealIds);

	List<SetmealDish> getSetmealDishesBySetmealId(Long setmealId);

	@Delete("DELETE FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
	void deleteBatchBySetmealId(Long setmealId);
}
