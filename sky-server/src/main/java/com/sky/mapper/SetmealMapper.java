package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 插入套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 通过分类id查询
     * @param categoryId
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE category_id = #{categoryId}")
    Setmeal getByCategoryId(Long categoryId);

    /**
     * 批量删除
     * @param setmealIds
     */
    void deleteBatch(List<Long> setmealIds);

    /**
     * 通过id查询套餐
     * @param setmealId
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE id = #{setmealId}")
    Setmeal getById(Long setmealId);

    /**
     * 更新套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);


    /**
     * 更新套餐状态
     * @param id
     * @param status
     */
    @Update("UPDATE setmeal SET status = #{status} WHERE id = #{id}")
    void updateStatusById(Long id, String status);
}
