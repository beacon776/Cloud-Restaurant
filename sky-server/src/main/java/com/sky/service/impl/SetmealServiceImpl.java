package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
	@Autowired
	private SetmealMapper setmealMapper;

	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private SetmealDishMapper setmealDishMapper;

	@AutoFill(OperationType.INSERT)
	@Transactional
	@Override
	public void insert(SetmealDTO setmealDTO) {
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		setmealMapper.insert(setmeal);
		Long setmealId = setmeal.getId();

		List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
		dishes.forEach(dish -> {
			dish.setSetmealId(setmealId);
		});
		setmealDishMapper.insertBatch(dishes);
	}

	@Override
	public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
		PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
		Page<Setmeal> setmealVOS = setmealMapper.pageQuery(setmealPageQueryDTO);
		return new PageResult(setmealVOS.getTotal(), setmealVOS.getResult());
	}
}
