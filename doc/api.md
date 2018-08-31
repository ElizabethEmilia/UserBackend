# 前端和后端API接口的说明

##1. 标记说明
 
 ### 1.1 接口权限的标记
 
 【S】表示该接口超级管理员可用

 【A】表示该接口管理员可使用
 
 【C】表示该接口客户可使用
 
 【P】表示该接口可供任何人使用（包括未登录用户）
 
  权限的排序（高到低）：【S】>【A】>【C】>【P】
 
 【-】表示等于或低于某个权限的用户均可使用
 
 【+】表示等于或高于某个权限的用于均可使用

### 1.2 接口API种类的标记
 
 【L】表示该接口需要获取分页列表（见2.1）
 
 【T(`Table:Column`)】表示该接口需要实现若干个子类别，子类别和和数据库中的Table表Column字段对应
 
 【E】接口存在额外的参数，与【L】共并且不冲突
 
 【D】需要实现删除接口
 
 【M】该接口是批量操作接口(见2.2)
 
 ### 1.3 其他标记说明
 
 `key=<说明>` 是对参数含义的说明
 
 `[key=<说明>]` 表示可选参数
 
 ## 2. 接口实现说明
 
 说明：为了确保安全，所有的需要绑定用户进行更改的操作均需要在后端设置uid而不是通过接口传入，对于同时传入公司ID（cid）的，需要验证这个用户是否拥有该公司。
 
 ### 2.1 对于需要获取列表的接口
 
 该类型接口需要提供：
 
 1. 分页获取所有记录：`GET url?page=<当前页数>[&size=<每页个数>]`
 
 2. 记录总数：`GET url?count`  
 
 ### 2.2 对于需要批量删除的接口
 
 可以操作1个或多个记录。
 
 POST方法体为：逗号间隔的ID序列。最后一个元素后无逗号
 
 #需要实现的接口
 
 ## 1 登录和用户管理
 
## 1.1 【P】用户登录

* 登录 `POST /api/login`
    
    参数
    
    `name` 手机号（对应管理员和客户表中的手机号）
    
    `password` 密码
    
    `code` 验证码
    
* 退出登录 `GET /api/logout`

## 1.2 【P】客户注册

注册分为两个步骤：1.记录短信验证码 2.比较验证码并写入数据库

* 获取图片验证码 `GET /api/verifycode`

参数：无

返回值：包含验证码图片Base64编码的字符串

* 发送短信验证码 `POST /api/sendmsg`

参数：`code` ：图片验证码

返回值：发送成功、失败、验证码不正确

* 注册 `GET /api/register`

参数：`name`: 客户姓名  

`password`： 密码 

`phone`: 手机号码  

`industry`: 行业 
 
`type`: 客户类型    

 `msgcode`: 短信验证码

返回值：注册结果/短信验证码错误

* 找回密码  `GET /api/forget`

参数：`phone` 手机号
   
  `msgcode` 短信验证码 
  
 返回值：成功：发送一个找回密码的链接给客户（链接格式见下方）  失败：原因
 
 * 找回密码用户响应页（非API接口，属于UI，但写在这里）
 
 （需要在网页中注入window对象一个 window.findResult = true引导前端界面进入找回密码组件）
 
 格式：/forget?uid=<用户ID>&token=<Token>
 
 * 通过找回密码重设密码 `POST /api/resetpwd`
 
 参数： `uid` 用户ID
 
 `token` 登录表的Token
 
 `password` 用户设置的密码
 
 返回结果： uid或token不正确/密码设置（成功/失败）
 
 
 ## 2 用户账户模块
 
暂无

 ## 3 账户信息模块
 
 ### 3.1 【C】账户信息
 
 * 获取：`GET  /api/account/basic`
 * 修改：`POST /api/account/basic`
    
    POST参数： Bean中的个字段
    
### 3.2 【C+】修改密码

 * 修改：`POST /api/login/password`
    
    POST参数： 加密后的密码
    
### 3.3 【C】在线充值

 * 暂时不做
 
 ### 3.4 【C+】对公转账
 
 * 新增申请： `POST /api/public/new`
 * 【LT(`t_public_charge:status`)】
  获取转账记录
 
 1. 全部：`GET /api/public/all`
 2. 待确认：`GET /api/public/pending`
 3. 已确认：`GET /api/public/confirmed`
 4. 已取消：`GET /api/public/cancelled`
 
 * 更改申请
 
 1. 取消申请【M】：`POST /api/public/cancel`
 2. 通过申请【M】【A】：`POST /api/public/accept`
 3. 驳回申请【M】【A】：`POST /api/public/refuse`
 
 ### 3.5 【C】钱包明细
 
 * 【LT(`t_exchange:type`)】获取钱包记录
 
 1. 全部：`GET /api/exchange/all`
 2. 收入: `GET /api/exchange/income`
 3. 支出: `GET /api/exchange/outcome`
 
 ### 3.6 【C】产品订单
 
 * 【LET(`t_order:status`)】获取产品订单信息
 
 额外参数说明：
 
 `cid` 公司ID（可选，表示要查询的某个公司）
 
 `type` 订单类型（可选，在不传参数时选择全部）
 
 `time_start` 开始时间（可选）
 
 `time_end` 结束时间（可选）
 
 1. 全部：`GET /api/order/all`
 2. 待支付：`GET /api/order/pending`
 3. 已支付：`GET /api/order/paid`
 4. 已取消：`GET /api/order/cancelled`
 
 ## 4 公司管理模块
 
 ### 4.1 公司信息获取【C+】
 
 * 【L】获取公司列表 `GET /api/company/list?[search=<搜索关键词>]`
 * 获取某一个公司的信息 `GET /api/company/info?cid=<公司ID>`
 * 获取公司设立进度，并按照时间排序 `GET /api/company/setup?cid=<公司ID>` 
 * 获取公司证件照 `GET /api/company/cert?cid=<公司ID>` 
 
 ### 4.2 公司信息的修改【A】
 
 * 添加公司 `POST /api/company/add`
    
      POST方法体为公司对应的Bean
      
 * 删除公司 `POST /api/company/delete`
 
      POST方法体为公司对应的ID
      
 * 修改信息 `POST /api/company/modify`
        
      POST方法体为公司对应的Bean
      
 * 添加(更改)公司的设立进度  `POST /api/company/progress`
 
      `cid`: 公司ID (int)
      
      `status`：新的公司进度 (String)
      
      `note`: 说明(String)
      
     更改进度的业务逻辑实际上是添加一条新的记录，并不删除或修改旧的记录
     
  * 上传公司证件照  `POST /api/company/addcert`
  
    `cid`: 公司ID (int)
    
    `certName`: 证件照名称
    
    `certNo`: 证件照编号(String)
    
    `certImg`: 证件照图片（Base64编码的String）
    
 * 删除公司证件照    `POST /api/company/rmcert`
   
    `cid`: 公司ID (int)
    
    