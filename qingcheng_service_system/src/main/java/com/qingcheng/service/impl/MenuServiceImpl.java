package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.MenuMapper;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.system.Menu;
import com.qingcheng.service.system.MenuService;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import sun.nio.cs.ext.MacArabic;
import tk.mybatis.mapper.entity.Example;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 返回所有菜单
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findAllMenu() {
        List<Menu> all = findAll();
        //筛选菜单
        return this.findMenuListByParentId(all, "0");
    }

    /**
     * 根据parentId查找菜单
     *
     * @param list
     * @param parentId
     * @return
     */
    private List<Map<String, Object>> findMenuListByParentId(List<Menu> list, String parentId) {
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (Menu menu : list) {
            if (menu.getParentId().equals(parentId)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("path", menu.getId());
                map.put("title", menu.getName());
                map.put("icon", menu.getIcon());
                map.put("linkUrl", menu.getUrl());
                map.put("children", this.findMenuListByParentId(list, menu.getId()));
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 返回全部记录
     *
     * @return
     */
    public List<Menu> findAll() {
        return menuMapper.selectAll();
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Menu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Menu> menus = (Page<Menu>) menuMapper.selectAll();
        return new PageResult<Menu>(menus.getTotal(), menus.getResult());
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询条件
     * @return
     */
    public List<Menu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return menuMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Menu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Menu> menus = (Page<Menu>) menuMapper.selectByExample(example);
        return new PageResult<Menu>(menus.getTotal(), menus.getResult());
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Menu findById(String id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param menu
     */
    public void add(Menu menu) {
        menuMapper.insert(menu);
    }

    /**
     * 修改
     *
     * @param menu
     */
    public void update(Menu menu) {
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) {
        menuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 菜单ID
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andLike("id", "%" + searchMap.get("id") + "%");
            }
            // 菜单名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 图标
            if (searchMap.get("icon") != null && !"".equals(searchMap.get("icon"))) {
                criteria.andLike("icon", "%" + searchMap.get("icon") + "%");
            }
            // URL
            if (searchMap.get("url") != null && !"".equals(searchMap.get("url"))) {
                criteria.andLike("url", "%" + searchMap.get("url") + "%");
            }
            // 上级菜单ID
            if (searchMap.get("parentId") != null && !"".equals(searchMap.get("parentId"))) {
                criteria.andLike("parentId", "%" + searchMap.get("parentId") + "%");
            }


        }
        return example;
    }

}
