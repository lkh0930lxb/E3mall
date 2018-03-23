package cn.e3mall.user.service.impl;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.user.jedis.JedisDao;
import cn.e3mall.user.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/20 17:47
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisDao jedisDao;

    @Value("${SESSION_KEY}")
    private String SESSION_KEY;

    @Value("${SESSION_KEY_EXPIRE_TIME}")
    private Integer SESSION_KEY_EXPIRE_TIME;

    /**
     * 需求:用户注册数据校验
     * 参数:String param,Integer type
     * 参数说明:
     * type==1,校验用户名
     * type==2,校验手机号
     * type==3,校验邮箱
     * 返回值:
     * {
     status: 200 //200 成功
     msg: "OK" // 返回信息消息
     data: false // 返回数据，true：数据可用，false：数据不可用
     }

     */
    @Override
    public E3Result dataCheck(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //type==1,校验用户名
        if (type==1){
            criteria.andUsernameEqualTo(param);
        }else if (type == 2){
            criteria.andPhoneEqualTo(param);
        }else if (type ==3){
            criteria.andEmailEqualTo(param);
        }
        List<TbUser> list = userMapper.selectByExample(example);
        if(list.isEmpty()){
            return E3Result.ok(true);
        }
        return E3Result.ok(false);
    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/21 11:04
     * @Description: 用户注册
     * @Param:       [user]
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @Override
    public E3Result register(TbUser user) {
        try {
            //密码加密
            String password = user.getPassword();
            String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
            user.setPassword(md5Pwd);
            Date date = new Date();
            user.setCreated(date);
            user.setUpdated(date);
            userMapper.insert(user);
            return E3Result.build(200,"注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(400,"注册失败，请检查数据后重新提交");
        }

    }

    /**
     * @Author:      Likh
     * @Date:        2018/3/21 11:34
     * @Description: 用户登录
     * @Param:       [username, password]
     * @return:      cn.e3mall.common.pojo.E3Result
     */
    @Override
    public E3Result login(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        if (list == null || list.size()==0) {
            return E3Result.build(400,"用户名不存在");
        }
        TbUser tbUser = list.get(0);
        String password1 = tbUser.getPassword();
        if (!password1.equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            return E3Result.build(400,"用户名或密码错误");
        }
        String token = UUID.randomUUID().toString();
        //存入redis缓存
        jedisDao.set(SESSION_KEY+":"+token, JsonUtils.objectToJson(tbUser));
        //设置过期时间
        jedisDao.expire(SESSION_KEY+":"+token,SESSION_KEY_EXPIRE_TIME);
        return E3Result.ok(token);
    }

    @Override
    public E3Result findUserByToken(String token) {
        String userJson = jedisDao.get(SESSION_KEY + ":" + token);
        if(StringUtils.isNotBlank(userJson)){
            TbUser tbUser = JsonUtils.jsonToPojo(userJson, TbUser.class);
            //重置过期时间
            jedisDao.expire(SESSION_KEY+":"+token,SESSION_KEY_EXPIRE_TIME);
            return E3Result.ok(tbUser);
        }
       return E3Result.build(201,"用户名已经过期");
    }
}
