package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
	@Autowired
	private DishService dishService;

	/**
	 * 新增菜品
	 * @param dishDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增菜品")
	public Result save(@RequestBody DishDTO dishDTO) {
		log.info("新增菜品：{}", dishDTO);
		dishService.saveWithFlavor(dishDTO);
		return Result.success();
	}

	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("菜品分页查询")
	public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
		PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
		return Result.success(pageResult);
	}

	@DeleteMapping
	@ApiOperation("菜品批量删除")
	public Result delete(@RequestParam List<Long> ids) {
		dishService.deleteBatch(ids);
		return Result.success();
	}

	@GetMapping("{id}")
	@ApiOperation("根据id查询菜品信息")
	public Result<DishVO> get(@PathVariable Long id) {
		DishVO dishVO = dishService.get(id);
		return Result.success(dishVO);
	}

	@PutMapping
	@ApiOperation("修改菜品")
	public Result update(@RequestBody DishDTO dishDTO) {
		dishService.updateDishWithFlavor(dishDTO);
		return Result.success();
	}

	@GetMapping("/list")
	@ApiOperation("根据分类id查询菜品")
	public Result<List<Dish>> listQuery(@RequestParam Long categoryId) {
		List<Dish> dishes = dishService.listQuery(categoryId);
		return Result.success(dishes);
	}
}
