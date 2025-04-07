package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "用户端店铺相关")
@Slf4j
public class UserShopController {

	@Autowired
	private RedisTemplate redisTemplate;

	public static final String KEY = "SHOP_STATUS";
	/**
	 * 用户端获取店铺营业状态
	 * @return
	 */
	@GetMapping("/status")
	@ApiOperation("用户端获取店铺营业状态")
	public Result<Integer> getShopStatus() {
		Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
		log.info("获取店铺的营业状态为:{}", status == 1 ? "营业中" : "已打烊");
		return Result.success(status);
	}
}
