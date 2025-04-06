package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
	/**
	 * 插入套餐
	 * @param setmealDTO
	 */
	void insert(SetmealDTO setmealDTO);

	/**
	 * 分页查询套餐
	 * @param setmealPageQueryDTO
	 * @return
	 */
	PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

	/**
	 * 批量删除套餐以及对应菜品
	 * @param setmealIds
	 */
	void deleteBatch(List<Long> setmealIds);

	/**
	 * 更新套餐
	 * @param setmealDTO
	 */
	void update(SetmealDTO setmealDTO);

	/**
	 * 通过id获取套餐
	 * @param id
	 * @return
	 */
	SetmealVO getById(Long id);

	/**
	 * 更新套餐在售停售状态
	 * @param id
	 * @param status
	 */
	void updateStatusById(Long id, String status);
}
