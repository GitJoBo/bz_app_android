package com.bizeng.lh.utils;

import androidx.core.content.FileProvider;

/**
 * @Desc: 第三方库也配置了同样的FileProvider, 可以通过继承FileProvider类来解决合并冲突的问题
 * @author: admin wsj
 * @Date: 2021/5/24 11:16 AM
 */
public class BuglyFileProvider extends FileProvider {
}
