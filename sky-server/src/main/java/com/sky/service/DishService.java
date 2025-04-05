package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {

	public void saveWithFlavor(DishDTO dishDTO);

	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
