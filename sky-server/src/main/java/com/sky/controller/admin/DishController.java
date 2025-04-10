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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
	@Autowired
	private DishService dishService;

	@Autowired
	private RedisTemplate redisTemplate;

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

		String key = "dish_" + dishDTO.getCategoryId();
		redisTemplate.delete(key);
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
		log.info("批量删除菜品的id为:{}", ids);
		dishService.deleteBatch(ids);
		cleanCache("dish_*");
		return Result.success();
	}

	@GetMapping("{id}")
	@ApiOperation("根据id查询菜品信息")
	public Result<DishVO> get(@PathVariable Long id) {
		log.info("根据id查询菜品信息:{}", id);
		DishVO dishVO = dishService.get(id);
		return Result.success(dishVO);
	}

	@PutMapping
	@ApiOperation("修改菜品")
	public Result update(@RequestBody DishDTO dishDTO) {
		log.info("修改菜品:{}", dishDTO);
		dishService.updateDishWithFlavor(dishDTO);
		cleanCache("dish_*");

		return Result.success();
	}

	@GetMapping("/list")
	@ApiOperation("根据分类id查询菜品")
	public Result<List<DishVO>> listQuery(@RequestParam Long categoryId) {
		String key = "dish_" + categoryId;
		List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
		if (list != null && list.size() > 0) {
			return Result.success(list);
		}
		List<DishVO> dishes = dishService.listQuery(categoryId);
		redisTemplate.opsForValue().set(key, dishes);

		return Result.success(dishes);
	}

	/**
	 * 清理缓存数据
	 * @param pattern
	 */
	private void cleanCache(String pattern) {
		// (暂时性的)将以dish_开头的缓存全删掉
		Set keys = redisTemplate.keys(pattern);
		redisTemplate.delete(keys);
	}
}
