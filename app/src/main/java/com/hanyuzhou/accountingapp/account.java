package com.hanyuzhou.accountingapp;

public interface account {
    /*
    通过设置数据库名称新建/选择数据库。达到改变account的目的
    @param db_NAME ： 数据库名称
    在新建/选择账户的时候调用
     */
    public void setDbName(String db_NAME);
    /*
    彻底删除数据库（文件）。
    @param db_NAME ： 数据库名称
    在删除账户的时候调用
     */
    public void DeletDatabase(String db_NAME);
}
