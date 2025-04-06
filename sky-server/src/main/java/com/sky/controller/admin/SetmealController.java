package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

	@Autowired
	private SetmealService setmealService;
	@Autowired
	private SetmealMapper setmealMapper;

	@PostMapping
	@ApiOperation("新增套餐")
	public Result insert(@RequestBody SetmealDTO setmealDTO) {
		setmealService.insert(setmealDTO);
		return Result.success();
	}

	@GetMapping("/page")
	@ApiOperation("分页查询套餐")
	public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
		PageResult page = setmealService.page(setmealPageQueryDTO);
		return Result.success(page);
	}

	@DeleteMapping
	@ApiOperation("批量删除套餐")
	public Result deleteBatch(@RequestParam List<Long> ids) {
		setmealService.deleteBatch(ids);
		return Result.success();
	}

	@PutMapping
	@ApiOperation("修改套餐")
	public Result update(@RequestBody SetmealDTO setmealDTO) {
		setmealService.update(setmealDTO);
		return Result.success();
	}

	@GetMapping("/{id}")
	@ApiOperation("根据id获取套餐")
	public Result<SetmealVO> getById(@PathVariable Long id) {
		SetmealVO setmealVO = setmealService.getById(id);
		return Result.success(setmealVO);
	}

	@PostMapping("/status/{status}")
	public Result updateStatus(@PathVariable String status, @RequestParam Long id) {
		setmealService.updateStatusById(id, status);
		return Result.success();
	}
}
