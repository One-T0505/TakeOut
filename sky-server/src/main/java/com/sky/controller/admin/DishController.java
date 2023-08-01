package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * ymy
 * 2023/7/30 - 13 : 39
 **/


@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管管类")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 新增菜品
    * @param dishDTO
    * @return
    */

    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 清理redis缓存  只删除受影响那部分的缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success("新增菜品成功");
    }


    /*
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /*
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids){
        log.info("删除菜品: {}", ids);

        cleanCache("dish_*");

        // 删除数据库
        dishService.deleteBatch(ids);
        return Result.success("删除菜品成功");
    }

    /*
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品: {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /*
     * 修改菜品  前端已经将新的数据封装到了 dishDTO 中
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品为: {}", dishDTO);

        // 为了方便，我们直接删除所有缓存，其实也可以更细粒度，但是目前没必要
        cleanCache("dish_*");

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }


    /**
     * 菜品启售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){

        cleanCache("dish_*");

        dishService.activateOrDeactivate(status,id);
        return Result.success();
    }


    private void cleanCache(String pattern){
        // 删除所有缓存   查找所有 dish_ 开头的key
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
