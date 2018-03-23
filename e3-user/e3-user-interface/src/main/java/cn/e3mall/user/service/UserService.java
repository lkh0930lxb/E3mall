package cn.e3mall.user.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/20 17:44
 */
public interface UserService {

    public E3Result dataCheck(String param,Integer type);

    //用户注册
    public E3Result register(TbUser user);

    //用户登录
    public  E3Result login(String username,String password);

    //根据token查询登录信息

    public E3Result findUserByToken(String token);
}
