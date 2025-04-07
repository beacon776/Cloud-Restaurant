package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺状态相关")
@Slf4j
public class ShopController {
	@Autowired
	private RedisTemplate redisTemplate;

	public static final String KEY = "SHOP_STATUS";

	/**
	 * 商家端设置店铺状态
	 * @param status
	 * @return
	 */
	@PutMapping("/{status}")
	@ApiOperation("商家端设置店铺状态")
	// 因为商家只有本店一家，所以就不用传入商家的id了
	public Result setShopStatus(@PathVariable Integer status) {
		log.info("设置本店铺的营业状态为：{}", status == 1 ? "营业中" : "已打烊");
		redisTemplate.opsForValue().set(KEY, status);
		return Result.success();
	}

	/**
	 * 获取店铺营业状态
	 * @return
	 */
	@GetMapping("/status")
	@ApiOperation("获取店铺营业状态")
	public Result<Integer> getShopStatus() {
		Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
		log.info("获取店铺的营业状态为:{}", status == 1 ? "营业中" : "已打烊");
		return Result.success(status);
	}
}
