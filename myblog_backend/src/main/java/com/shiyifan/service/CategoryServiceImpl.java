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
    public ArrayList<Mycategory> selectAllCategoryByPage(int userid, int pageNow, int pageSize) {
        int start = (pageNow-1)*pageSize;
        return categoryMapper.selectAllCategoryByPage(userid, start, pageSize);
    }

    @Override
    public ArrayList<Mycategory> selectAllCategoryForBlog(int userid) {
        return categoryMapper.selectAllCategoryForBlog(userid);
    }

    @Override
    public int selectTotalCategoryNums(int userid) {
        return categoryMapper.selectTotalCategoryNums(userid);
    }

    @Override
    public void addCategory(int userid,String categoryname) {
        categoryMapper.addCategory(userid,categoryname);
    }

    @Override
    public Mycategory selectCategoryById(int userid, int categoryid) {
        return categoryMapper.selectCategoryById(userid, categoryid);
    }

    @Override
    public void updateCategory(int userid,String categoryname,int categoryid) {
        categoryMapper.updateCategory(userid, categoryname, categoryid);
    }

    @Override
    public void deleteCategory(int userid, int categoryid) {
        categoryMapper.deleteCategory(userid,categoryid);
    }

    @Override
    public int getCategoryRank(int userid, int categoryid) {
        return categoryMapper.getCategoryRank(userid, categoryid);
    }


    @Override
    public ArrayList<Mycategory> selectAllCategoryForCommon() {
        return categoryMapper.selectAllCategoryForCommon();
    }
}
