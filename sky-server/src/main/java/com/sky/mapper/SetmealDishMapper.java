package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

	List<Long> getSetmealIdsByDishIds(@Param("dishIds") List<Long> dishIds);

	void insertBatch(List<SetmealDish> setmealDishes);
}
