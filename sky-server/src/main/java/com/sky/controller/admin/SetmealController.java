package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

	@Autowired
	private SetmealService setmealService;

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


}
