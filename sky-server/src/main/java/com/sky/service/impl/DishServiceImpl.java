package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

	@Autowired
	private DishMapper dishMapper;

	@Autowired
	private DishFlavorMapper dishFlavorMapper;

	@Autowired
	private SetmealDishMapper setmealDishMapper;

	/**
	 * 新增菜品和对应的口味
	 * @param dishDTO
	 */
	@Override
	@Transactional // 保证方法的原子性（全部成功 / 失败)
	public void saveWithFlavor(DishDTO dishDTO) {
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		// 菜品表插入一条数据
		dishMapper.insert(dish);
		Long dishId = dish.getId();
		// 口味表插入n条数据
		List<DishFlavor> flavors = dishDTO.getFlavors();
		if (flavors != null && flavors.size() > 0) {
			flavors.forEach(flavor -> {
				flavor.setDishId(dishId);
			});
			dishFlavorMapper.insertBatch(flavors);
		}
	}

	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
		PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
		Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Transactional
	@Override
	public void deleteBatch(List<Long> ids) {
		// 整批删除，先判断这一批里又没有不允许删除的菜品
		for (Long id : ids) {
			Dish dish = dishMapper.getById(id);
			// 在售菜品不允许删除
			if (dish.getStatus().equals(StatusConstant.ENABLE)) {
				throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
			}
		}
		// 判断当前菜品是否与套餐关联了，关联不能删除
		List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
		if (setmealIds != null && setmealIds.size() > 0) {
			throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
		}
		// 批量删除菜品
		dishMapper.deleteBatch(ids);
		// 批量删除菜品对应的口味数据
		dishFlavorMapper.deleteByDishIds(ids);

	}

	@Override
	public DishVO get(Long id) {
		DishVO dishVo = new DishVO();
		Dish dish = dishMapper.getById(id);
		List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
		BeanUtils.copyProperties(dish, dishVo);
		dishVo.setFlavors(dishFlavors);
		return dishVo;
	}
}
