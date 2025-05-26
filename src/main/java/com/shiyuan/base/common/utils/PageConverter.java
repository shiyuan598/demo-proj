package com.shiyuan.base.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.function.Function;

public class PageConverter {

    /**
     * IPage<T> 转 IPage<R>
     *
     * @param source 原始分页对象
     * @param mapper 映射函数（如：userConverter::toVO）
     * @return 转换后的分页对象
     */
    public static <T, R> IPage<R> convert(IPage<T> source, Function<T, R> mapper) {
        Page<R> result = new Page<>(source.getCurrent(), source.getSize(), source.getTotal());
        result.setPages(source.getPages());
        result.setRecords(source.getRecords().stream().map(mapper).toList());
        return result;
    }
}
