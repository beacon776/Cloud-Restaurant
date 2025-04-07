package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
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

import javax.swing.text.StringContent;
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

	/**
	 * 插入套餐
	 * @param setmealDTO
	 */
	@AutoFill(OperationType.INSERT)
	@Transactional
	@Override
	public void insert(SetmealDTO setmealDTO) {
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		// 新增的套餐默认不起售
		setmeal.setStatus(StatusConstant.DISABLE);
		setmealMapper.insert(setmeal);
		Long setmealId = setmeal.getId();
		List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
		dishes.forEach(dish -> {
			dish.setSetmealId(setmealId);
		});
		setmealDishMapper.insertBatch(dishes);
	}

	/**
	 * 分页查询套餐
	 * @param setmealPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
		PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
		Page<Setmeal> setmealVOS = setmealMapper.pageQuery(setmealPageQueryDTO);
		return new PageResult(setmealVOS.getTotal(), setmealVOS.getResult());
	}

	/**
	 * 批量删除套餐以及对应菜品
	 * @param setmealIds
	 */
	@Override
	public void deleteBatch(List<Long> setmealIds) {

		// 起售中的套餐不能删除
		for (Long setmealId : setmealIds) {
			Setmeal setmeal = setmealMapper.getById(setmealId);
			if (setmeal.getStatus() == 1) {
				throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
			}
		}
		// 删除套餐表 + 套餐菜品关系表的数据
		setmealMapper.deleteBatch(setmealIds);
		setmealDishMapper.deleteBatchBySetmealIds(setmealIds);

	}

	/**
	 * 更新套餐
	 * @param setmealDTO
	 */
	@Override
	public void update(SetmealDTO setmealDTO) {
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		setmealMapper.update(setmeal);
		List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
		List<Long> dishIds = new ArrayList<>();
		dishes.forEach(dish -> {
			dish.setSetmealId(setmeal.getId());
			dishIds.add(dish.getId());
		});
		Long setmealId = setmeal.getId();
		setmealDishMapper.deleteBatchBySetmealId(setmealId);
		setmealDishMapper.insertBatch(dishes);

	}

	/**
	 * 通过id获取套餐
	 * @param id
	 * @return
	 */
	@Override
	public SetmealVO getById(Long id) {
		Setmeal setmeal = setmealMapper.getById(id);
		Long setmealId = setmeal.getId();
		List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishesBySetmealId(setmealId);
		SetmealVO setmealVO = new SetmealVO();
		BeanUtils.copyProperties(setmeal, setmealVO);
		setmealVO.setSetmealDishes(setmealDishes);
		return setmealVO;
	}

	/**
	 * 更新套餐在售停售状态
	 * @param id
	 * @param status
	 */
	@Override
	public void updateStatusById(Long id, String status) {
		setmealMapper.updateStatusById(id, status);
	}
}
