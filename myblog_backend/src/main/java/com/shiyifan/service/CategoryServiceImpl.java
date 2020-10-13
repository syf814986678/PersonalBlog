package com.shiyifan.service;

import com.shiyifan.dao.CategoryMapper;
import com.shiyifan.pojo.Mycategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public ArrayList<Mycategory> selectAllCategoryByPage(int userId, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return categoryMapper.selectAllCategoryByPage(userId, start, pageSize);
    }

    @Override
    public ArrayList<Mycategory> selectAllCategoryForBlog(int userId) {
        return categoryMapper.selectAllCategoryForBlog(userId);
    }

    @Override
    public int selectTotalCategoryNums(int userId) {
        return categoryMapper.selectTotalCategoryNums(userId);
    }

    @Override
    public void addCategory(int userId, String categoryName) {
        categoryMapper.addCategory(userId, categoryName);
    }

    @Override
    public Mycategory selectCategoryById(int userId, int categoryId) {
        return categoryMapper.selectCategoryById(userId, categoryId);
    }

    @Override
    public void updateCategory(int userId, String categoryName, int categoryId) {
        categoryMapper.updateCategory(userId, categoryName, categoryId);
    }

    @Override
    public void deleteCategory(int userId, int categoryId) {
        categoryMapper.deleteCategory(userId, categoryId);
    }

    @Override
    public int getCategoryRank(int userId, int categoryId) {
        return categoryMapper.getCategoryRank(userId, categoryId);
    }


    @Override
    public ArrayList<Mycategory> selectAllCategoryForCommon() {
        return categoryMapper.selectAllCategoryForCommon();
    }
}
