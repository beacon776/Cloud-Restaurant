package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequestMapping("/admin/common")
@RestController
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
	@Autowired
	private AliOssUtil aliOssUtil;

	@PostMapping("/upload")
	@ApiOperation("文件上传")
	public Result<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
		log.info("文件上传：{}", file);
		try {
			String fileName = file.getOriginalFilename();
			String uuidName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
			String url = aliOssUtil.upload(file.getBytes(), uuidName);
			return Result.success(url);

		} catch (Exception e) {
			log.error("文件上传失败：{}", e);
		}
		return Result.error(MessageConstant.UPLOAD_FAILED);
	}
}
