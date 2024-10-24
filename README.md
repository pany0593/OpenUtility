1. 加载所有maven项目
2. 启动本地mysql服务(修改application.yml为你的username，password)
3. 创建名为OpenUtility的数据库
4. 执行schema.sql
5. 启动OpenUtilityApplication无报错即可

- ### 用户模块

    - 实现用户的注册登陆
    - 使用JWT令牌
    - 使用springsecurity或shiro安全框架
    - 实现用户头像功能并提供默认头像

- ### 账单模块

    - 实现账单增删改查
    - 水电费统计功能
        - 可改统计范围(宿舍~全校)
        - 可改统计时间(一个月~多年)

- ### 论坛模块

    - 实现发帖
        - 点赞
        - 评论(可嵌套)
    - 简单搜索功能
    - 文章增删改查