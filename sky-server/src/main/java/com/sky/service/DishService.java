package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

	public void saveWithFlavor(DishDTO dishDTO);

	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

	void deleteBatch(List<Long> ids);

	DishVO get(Long id);

	void updateDishWithFlavor(DishDTO dishDTO);

	List<DishVO> listQuery(Long categoryId);
}
