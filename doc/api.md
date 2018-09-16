# 前端和后端API接口的说明

## 1. 标记说明
 
 ### 1.1 接口权限的标记
 
 【S】表示该接口超级管理员可用

 【G】表示该接口组管理员可用

 【A】表示该接口普通管理员可使用
 
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
 
 `~` 表示前述说明的URL
 
 ## 2. 接口实现说明
 
 说明：为了确保安全，所有的需要绑定用户进行更改的操作均需要在后端设置uid而不是通过接口传入，对于同时传入公司ID（cid）的，需要验证这个用户是否拥有该公司。
 
 ### 2.1 对于需要获取列表的接口
 
 该类型接口需要提供：
 
 1. 分页获取所有记录：`GET url?page=<当前页数>[&size=<每页个数>]`
 
 2. 记录总数：`GET url?count`  
 
 ### 2.2 对于需要批量删除的接口
 
 > 可以操作1个或多个记录。
 > 
 > POST方法体为：逗号间隔的ID序列。最后一个元素后无逗号
 
 # 需要实现的接口
 
 ## 1 登录和用户管理
 
## 1.1 【P】用户登录

* 登录 `POST /api/login`
    
>    参数
>    
>    `name` 手机号（对应管理员和客户表中的手机号）
>    
>    `password` 密码
>    
>    `code` 验证码
    
* 退出登录 `GET /api/logout`

* 获取用户是否登录 `GET /api/login/heartbeat`

返回  true/false

## 1.2 【P】客户注册

注册分为两个步骤：1.记录短信验证码 2.比较验证码并写入数据库

* [腾讯云短信API文档](https://github.com/qcloudsms/qcloudsms_java)

API Key保存在数据库的t_config表中

* 获取图片验证码 `GET /api/verifycode`

> 参数：无
> 
> 返回值：包含验证码图片Base64编码的字符串

* 发送短信验证码 `POST /api/sendmsg`

> 参数：`code` ：图片验证码
> 
> 返回值：发送成功、失败、验证码不正确

* 注册 `GET /api/register`

> 参数：`name`: 客户姓名  
> 
> `password`： 密码 
> 
> `phone`: 手机号码  
> 
> `industry`: 行业 
>  
> `type`: 客户类型    
> 
>  `msgcode`: 短信验证码
> 
> 返回值：注册结果/短信验证码错误

* 找回密码  `GET /api/forget`

> 参数：`phone` 手机号
>    
>   `msgcode` 短信验证码
>   
>  返回值：成功：发送一个找回密码的链接给客户（链接格式见下方）  失败：原因
 
 * 找回密码用户响应页（非API接口，属于UI，但写在这里）
 
> （需要在网页中注入window对象一个 window.findResult = true引导前端界面进入找回密码组件）
> 
> 格式：/forget?uid=<用户ID>&token=<Token>
 
 * 通过找回密码重设密码 `POST /api/resetpwd`
 
> 参数： `uid` 用户ID
> 
> `token` 登录表的Token
> 
    > `password` 用户设置的密码
> 
> 返回结果： uid或token不正确/密码设置（成功/失败）
 
 
 ## 2 用户账户模块
 
### 2.1 【A+】修改客户资料

**客户需求**  需要对管理员进行权限控制

#### 1. 基本信息

注意：不包含登录信息。

* 获取：`GET  /api/customer/{uid}/`

> 参数：无

> 9/11改动：需要判断该客户是否属于当前管理员
>
> 若不是，如果是组内其他管理员的客户，判断该管理员是否具有组内管理权限
>
> 若以上两项条件不同时符合，判断该管理员是否具有超级管理员权限
>
> 仅有超级管理员可以访问所有的客户信息
>
> 组管理员只能访问本组的信息
>
> 一般管理员只能访问属于自己客户的信息

* 修改：`POST /api/customer/{uid}/`

> 参数：Bean

* 【S】删除：`POST /api/customer/{uid}/delete`

> 参数： 无

* 【L】列表：POST `/api/customer`

* 修改密码： `POST /api/customer/{uid}/password`

* 获取客户列表（可以根据客户名称和id搜索）

1. 自己所属的客户：`GET /api/customer/list/self`以及`GET /api/customer`

2. 组内所属客户：`GET /api/customer/list/group`

3. 全部客户：`GET /api/customer/list/all`

搜索功能是在URL基础上加上参数`search=<id或客户名>`


#### 2. 客户公司相关

备注： 在本小姐，`uid`是冗余数据，`uid`用来判断`cid`是否属于这个用户，同时使url的结构清晰。

备注： `uid`若为_则不进行上述判断（方便前端偷懒）

* 客户的公司列表 `GET /api/customer/{uid}/company/list`

* 客户的某个公司信息  `GET /api/ccustomer/{uid}/company/{cid}`

* 修改客户的某个公司信息 `POST /api/ccustomer/{uid}/company/{cid}`

> 参数：  公司的bean

* 删除客户的某个公司 `POST /api/customer/{uid}/company/{cid}/delete`

* 删除客户的所有公司【S】 `POST /api/customer/{uid}/company/delete`

* 为客户新增公司 `POST /api/customer/{uid}/company/new`

* 查看客户公司的证件列表： `POST /api/customer/{uid}/company/{cid}/cert`

* 查看客户公司的某个证件： `GET /api/customer/{uid}/cert/{certid}/`

* 删除客户公司的某个证件： `POST /api/customer/{uid}/cert/{certid}/delete`

* 上传客户公司的证件  `POST /api/customer/{uid}/company/{cid}/newcert`
    
>    `certName`: 证件照名称
>    
>    `certNo`: 证件照编号(String)
>    
>    `certImg`: 证件照图片（Base64编码的String）

* 查看客户公司设立进度： `GET /api/customer/{uid}/company/{cid}/setup`

* 添加客户公司设立进度： `POST /api/customer/{uid}/company/{cid}/setup/new`

>    更改进度的业务逻辑实际上是添加一条新的记录，并不删除或修改旧的记录
>
>    `status`：新的公司进度 (String)
>    
>    `note`: 说明(String)

* 删除客户公司设立进度： `POST /api/customer/{uid}/company/{cid}/setup/delete`

* 新增设立公司进度

* 【LT】查看客户公司的开票申请： `GET /api/customer/{uid}/receipt`
>    `status`: 状态(String)
>    `type`: 类型(Integer)
>    `start`: 开始时间(Date)
>    `end`: 结束时间(Date)
>    `page`: 页面(Integer)
>    `size`: 条数(Integer)
>    `cid`: 公司id(Integer)


* 开票申请的相关操作：

由于开票申请的操作众多，前端作为状态机处理

状态转换图（后端开发可能不需要实现或者模拟一个状态机，但是有这个图更容易理解）：

![](./images/state.png)

所需的接口包括：

前端调用： `'POST /api/customer/{uid}}/receipt/{rid}/{action}'`

* 管理员对开票申请的 提交: `POST /api/customer/{uid}/receipt/{rid}/submit`

* 管理员对开票申请的 驳回: `POST /api/customer/{uid}/receipt/{rid}/refuse-submit`

* 管理员对开票申请的 通过: `POST /api/customer/{uid}/receipt/{rid}/accept`

* 管理员对开票申请的 分配（代开）: `POST /api/customer/{uid}/receipt/{rid}/distrib-dist`

* 管理员对开票申请的 分配（自取）: `POST /api/customer/{uid}/receipt/{rid}/distrib-self`

* 管理员对开票申请的 开票: `POST /api/customer/{uid}/receipt/{rid}/receipt`

* 管理员对开票申请的 作废: `POST /api/customer/{uid}/receipt/{rid}/discard`

* 管理员对开票申请的 打包: `POST /api/customer/{uid}/receipt/{rid}/pack`

* 管理员对开票申请的 核对并寄送: `POST /api/customer/{uid}/receipt/{rid}/send`

* 管理员对开票申请的 自取: `POST /api/customer/{uid}/receipt/{rid}/selfrecv`

* 管理员对开票申请的 驳回: `POST /api/customer/{uid}/receipt/{rid}/refuse-packing`

* 管理员对开票申请的 已签收: `POST /api/customer/{uid}/receipt/{rid}/recv`

* 查看客户各个公司的开票统计： `GET /api/customer/{uid}/receipt/stat`

```javascript
// 状态值
const states = {
    Saved: 0, // 已提交
    Submitted: 1, // 已提交，待审核
    Checked: 2, // 已审核，待分配
    DistributedDistrib: 3, // 已分配（分配），待开票
    DistributedSelf: 4, // 已分配（自取），待开票
    Receipted: 5, // 已开票。待打包
    Packed: 6, // 已打包,待核对
    VerifiedAndSent: 7, // 已核对，已寄送
    ReceivedCompleted: 8, // 已签收
    RefusedWaitingSubmit: 9, // 已驳回，待提交
    Abondoned: 10, // 已开票 已作废
    RefusedWaitingPacking: 11, // 已驳回，待打包
}
```


状态转换表：

```
Saved(0)
  ->Submitted(1)

Checked(2)
  ->DistributedDistrib(3)
  ->DistributedSelf(4)

DistributedDistrib(3)
  ->Receipted(5)

DistributedSelf(4)
  ->Receipted(5)

Receipted(5)
  ->Packed(6)
  ->Abondoned(10)

Packed(6)
  ->VerifiedAndSent(7)
  ->ReceivedCompleted(8)
  ->RefusedWaitingPacking(11)

VerifiedAndSent(7)
  ->ReceivedCompleted(8)

RefusedWaitingPacking(11)
  ->Packed(6)


```

> 业务逻辑说明：
> 
> 如果没有修改记录则统计，否则返回最新的修改记录，并且返回 edited 字段表示是否修改过。

* 【L】查看客户各个公司的开票统计修改记录： `GET /api/customer/{uid}/receipt/stat/log`

#### 3. 其他客户资料

* 【L】查看客户的充值记录：`GET /api/customer/{uid}/onlinecharge`

* 【L】查看客户的对公充值记录： `GET /api/customer/{uid}/publiccharge`

> 1. 全部：`GET ~/all`
> 2. 待确认：`GET ~/pending`
> 3. 已确认：`GET ~/confirmed`
> 4. 已取消：`GET ~/cancelled`

* 【L】查看客户的产品订单： `GET /api/customer/{uid}/orders`

> 1. 全部：`GET ~/all`
> 2. 待支付：`GET ~/pending`
> 3. 已支付：`GET ~/paid`
> 4. 已取消：`GET ~/cancelled`

* 确认客户的对公充值： `POST /api/customer/{uid}/publiccharge/{pid}/confirm`

* 取消客户的对公充值： `POST /api/customer/{uid}/publiccharge/{pid}/cancel`

### 2.2 【S】修改管理员资料

* 获取管理员列表： `GET /api/admin/list`

* 新增管理员：`GET /api/admin/add`

* 删除管理员：`POST /api/admin/{aid}/delete`

* 查看管理员信息：`GET /api/admin/{aid}/`

* 修改管理员信息：`POST /api/admin/{aid}/`

### 2.3 【A+】修改和查看自己的资料 

* 获取管理员资料 `GET /api/admin/basic` 

* 修改管理员资料 `POST /api/admin/basic` 

* 修改密码 `POST /api/admin/password` 

### 2.4 【S】角色和权限管理

* 【L】角色列表 `POST /api/role/list`

* 新增角色 `POST /api/role/new`

* 查看角色详情 `GET /api/role/{roleid}`

* 删除角色 `DELETE /api/role/{roleid}`

* 修改角色 `POST /api/role/{roleid}`

注意：权限管理使用一个整体权限的掩码来判断该用户是不是具有该模块的功能

### 2.5 组管理

备注：本小节的说明中，备注动态权限的前提是：该用户存在完整的或部分的查看其他用户资料的权限。

* 【L】组列表 `GET /api/group/list`
    - 备注：该功能实行动态权限：若该用户不具备查看所有用户的权限，则只能看到自己所属的组。

* 简化的组列表 `GET /api/group/list-simple`
   - 简化的组列表仅提供组名和ID信息。
   - 备注：该功能实行动态权限：若该用户不具备查看所有用户的权限，则只能看到自己所属的组。

* 添加组 `POST /api/group/add`
   - 备注：仅限可以修改所有用户资料的权限更改。

* 删除组 `POST /api/group/{gid}/delete`
   - 备注：仅限可以修改所有用户资料的权限更改。

* 修改组 `POST /api/group/{gid}/delete`
    - 备注：该功能实行动态权限：若该用户不具备查看所有用户的权限，则只能修改自己所属的组。

* 【L】查看组内的管理员  `GET /api/group/{gid}/user`
    - 备注：该功能实行动态权限：若包含查看组内权限，但不包含查看所有信息的权限，那么在查看本组时可以正常进行，但查看其他人的组时需要报错处理。

* 【L】查看组内管理员所属的客户 `GET /api/group/{gid}/customer`
    - 备注：该功能实行动态权限：若包含查看组内权限，但不包含查看所有信息的权限，那么在查看本组时可以正常进行，但查看其他人的组时需要报错处理。


## 3 账户信息模块【C】

### 3.0 登录时的信息检查

* 在登录的Controller中，在客户登录时检查登录信息：`GET /`

```
 需要检查的内容包含

1. 客户付费期限是否过期，如果已过期，将t_customer.paid改为0
2. 客户上次预选税金是否和当前时间是同一年，如果不是，增加税金预交数据 
```

* 客户信息脚本：`GET /user.js`

获取本客户信息
 
### 3.1 账户信息
 
* 获取：`GET  /api/account/basic`
 
* 修改：`POST /api/account/basic`
        
>    参数： Bean中的个字段（并非所有字段）
> 
>    注意：**不提供手机账号的修改（特别判断，防熊孩纸）**，虽然前端已经检查过了取值的正确性  但后端仍然应该检查各取值是否是Constant类中的可选范围。

* 修改：`POST /api/account/password`
    
```
参数： old 旧密码
     new  新密码

返回值  修改结果
```
    
* 修改头像：`POST /api/account/avatar`
 
>    参数：`img` 头像的Base64编码 

### 3.3 在线充值

见第10章
 
### 3.4 对公转账
 
* 新增申请： `POST /api/charge/public/new`

* 取消申请【M】：`POST /api/charge/public/{id}/cancel`
 
* 【LT(`t_public_charge:status`)】获取转账记录

> 1. 全部：`GET /api/charge/public/all`
> 2. 待确认：`GET /api/charge/public/pending`
> 3. 已确认：`GET /api/charge/public/confirmed`
> 4. 已取消：`GET /api/charge/public/cancelled`
 
筛选条件
> 时间段(start/end)

### 3.5 钱包明细

对于同时有多个筛选条件的情形，每个筛选参数可能都会传入后端，但可能这个值为空

* 获取最近交易信息（含收入和支出） `GET /api/exchange/recent`

```
返回值
{
            income: 总输入,
            lastIncome: 上次收入,
            outcome: 总支出,
            lastOutcome: 上次支出,
            balance: 余额
},
```

* 【LT(`t_exchange:type`)】获取钱包记录
 
> 1. 全部：`GET /api/exchange/all`
> 2. 收入: `GET /api/exchange/income`
> 3. 支出: `GET /api/exchange/outcome`

筛选条件
> 公司(cid)，支付方式(type)，时间段(start/end)
 
### 3.6 产品订单

* 【LET(`t_order:status`)】获取产品订单信息
 
> 额外参数说明：
> 
> `cid` 公司ID（可选，表示要查询的某个公司）
> 
> `type` 订单类型（可选，在不传参数时选择全部）
> 
> `time_start` 开始时间（可选）
> 
> `time_end` 结束时间（可选）
> 
> 1. 全部：`GET /api/order/all`
> 2. 待支付：`GET /api/order/pending`
> 3. 已支付：`GET /api/order/paid`
> 4. 已取消：`GET /api/order/cancelled`

筛选条件
> 公司(cid)，订单类型(type)，时间段(start/end)

## 4 公司管理模块【C】
 
### 4.1 公司信息获取
 
* 【L】获取公司列表 `GET /api/company/list?[search=<搜索关键词>]`
 
* 获取某一个公司的信息 `GET /api/company/{cid}/info`
 
* 获取某一个公司设立进度，并按照时间排序 `GET /api/company/{cid}/setup`
 
* 获取某一个公司证件照 `GET /api/company/{cid}/cert`

* 获取某一个公司的证件照信息 `GET /api/company/{cid}/cert/{id}`

* 获取该用户拥有的公司数量 `GET /api/company/count`

> 返回值： 一个纯数字

## 5 开票管理模块

* 申请开票 `POST /api/receipt/new`

* 【L】开票列表 `POST /api/receipt/list`
> 筛选条件： 公司(cid) 发票类型(type) 状态(status) 提交时间(start/end)

* 将开票状态设置为提交 `POST /api/receipt/{rid}/submit`

* 【M】删除开票申请 `POST /api/receipt/{rid}/delete`

* 获取全部记录 `GET /api/receipt/export`

* 开票情况统计（没有修改则统计，有则显示修改），以公司为分组：`GET /api/receipt/stat`

## 6 税金管理


### 6.1 年销售额预选

> 说明：本模块中，所有的状态更改均根据最后一条数据来进行。

1. 客户操作

* 【L】列出当年的税金选择操作历史记录表： `GET /api/company/{cid}/sales/list/current`

* 【L】列出所有的税金操作记录表：`GET /api/company/{cid}/sales/list/all`

需要实现搜索功能：规则和之前的挥则一样

参数： `status=<期望状态>&from=<查询起始时间>&to=<查询截止时间>`

* 获取最后预选一条记录： `GET /api/company/{cid}/sales/last`

* 选择年销售额范围（选择大范围）：`POST /api/company/{cid}/sales/preselect`

参数： ysaRange: 范围

> 限制：当且仅当status=0(未选定)的时候可以选择

> Side-Affects：同时更新公司列表中的范围和税率为选择范围对应的范围和税率

* 变更年销售额范围（选择小范围）：`POST /api/company/{cid}/sales/reselect`

> 限制：仅限已经选择过的时候可以选择

> 说明：名为“变更”，实为“添加”

> Side-Affects: 不更新公司列表的税率（因为档内选择税率不变）

* 税金差额补交：（待和客户确认）`POST /api/company/{cid}/sales/complement`

> 限制：仅限需要补交税金的情况，从余额扣除税金，并记录在案(需要计算补足的金额  这个地方只是定向)

> 副作用：更新公司信息中的补交税金字段

* 撤回变更：`POST /api/company/{cid}/sales/withdraw`

> 限制：仅限需要撤回的情况 

> 说明：名为“撤回”，实为“添加”

2. 管理员操作

* 【L】列出当年的税金选择操作历史记录表： `GET /api/customer/{uid|_}/company/{cid}/sales/list/current`

* 【L】列出所有的税金操作记录表：`GET /api/customer/{uid|_}/company/{cid}/sales/list/all`

* 自动更改税金状态：见3.0节说明

* 手动更改税金状态：`POST /api/customer/{uid|_}/company/{cid}/sales/{action}`

```
action: 
   request-back  要求撤回（仅当已提或已更改）
   preselect     代替客户预选
   reselect      重新选择
   
 注意：注意副作用
```

### 6.2 税金账户管理

税金管理模块设置有余额及明细查询功能，可以查询开票预交的税金、升档补交的税金、每期实际纳税额以及预交税金余额等。

1. 客户操作

* 【L】统计税金账户余额，按年按公司统计：`GET /api/tax/stat`

```
需要统计的内容包括
 - 每个公司每年的税金预交率（需要在6.1的表中查，不能只查公司表里面预交率，并且预交率只查询最后一条数据）
 - 税金余额：在tax_account_detail表中进行按年按公司的统计
 
搜索参数包括： cid=<公司ID>&yfrom=<查询起始年份>&yto=<查询截至年份>

关于年份：前端传入的年份仅包含数字（如2019）
```

* 【L】税金账户明细：`GET /api/tax/detail`
```
搜索参数包括： cid=<公司ID>&mfrom=<查询起始月份>&yto=<查询截至月份>

关于月份：前端传入的参数格式为 '2018-9' 仅包含月份
```

2. 管理员操作

* 【L】统计税金账户余额，按年按公司统计：`GET /api/customer/{cid}/tax/stat`

说明同上

* 【L】税金账户明细：`GET /api/customer/{cid}/tax/detail`

说明同上

* 管理员手动后台操作税金：`POST /api/tax/modify`

```
cid: 公司ID
tax； 税金收支
```

## 7 【S】系统管理

* 【L】系统日志 `GET /api/system/log`

* 系统设置列表 `GET /api/system/settings`

* 获取系统设置 `GET /api/system/settings/{key}`

关于系统设置的说明：

系统设置中设置类型包含以下类型，返回值应包含 { 名称, 类型（int）, 值(字符串原样返回，不做任何转换) }

|类型ID|类型名|        说明      |数据形式                | 控件形式 |
|-----|-----|-------------------|----------------------|--------|
|0    |int  | 32位有符号整数     | 32位有符号整数         |数字输入框([-2^31, 2^32-1])|
|1    |number|Number            |Number                |数字输入框|
|2    |string|字符串            | 字符串                |文本框    |
|3    |text  |长文本            | 字符串                | textarea |
|4    |date  |日期              | 表示日期的字符串       | datepicker |
|5    |bool  |布尔值            | true / false         | 单选框     |
|6    |enum  |枚举值            | `value`,,`enum`,[`enum`...] | 下拉菜单 |
|7    |senum| 可多选的枚举值      | `value`,[`value`,...],,`enum`,[`enum`...]| 多选的下拉框 |
|8    |uint  |32位无符号整数     | 32位无符号整数         |数字输入框 |
|9    |password|密码            | 加密的字符串           |密码输入框 |

> 参数： 无
>
> 返回值： { key不存在, 成功 }

* 修改系统设置 `POST /system/settings/{key}`

> 参数： `value` 值
> 
> 返回值： { 参数错误， 参数类型错误，key不存在，修改成功 }

* 清空回收站 `POST /system/settings/clear-cache`

> 参数： `table` 要清除的表名

## 8 【P】杂项功能

* 导出任意JSON对象数组到csv表格【C】：`GET /api/_/export`

* 获取省份列表：`GET /api/_/area/province`

* 获取城市列表：`GET /api/_/area/city`
  
> 参数： `province` 省

* 获取区列表：`GET /api/_/area/district`

> 参数： `province` 省 
>
> `city` 市

## 9 【P】服务器相关控制API

* 获取API字符串： `GET /api/sysctrl/apistr`

> 返回值：返回一个随机的16位字符串 

* 关闭服务器 `POST /api/sysctrl/shutdown`

> 参数：`key` 加密后的API字符串

* 备份并加密数据库 `POST /api/sysctrl/dbbackup`

## 10 支付相关

### 10.1 支付宝

交易表(t_exchange)

| Field     | Type          | Null | Key | Default | Extra          |
|-----------|---------------|------|-----|---------|----------------|
| id        | int(11)       | NO   | PRI | NULL    | auto_increment |
| running   | int(11)       | YES  |     | NULL    |                |
| uid       | int(11)       | YES  |     | NULL    |                |
| cid       | int(11)       | YES  |     | NULL    |                |
| amount    | decimal(10,0) | YES  |     | NULL    |                |
| paymethod | int(11)       | YES  |     | NULL    |                |
| note      | mediumtext    | YES  |     | NULL    |                |
| tm        | datetime      | YES  |     | NULL    |                |
| state     | int(11)       | YES  |     | NULL    |                |
| type      | int(11)       | YES  |     | NULL    |                |

订单表(t_order)

| Field     | Type          | Null | Key | Default | Extra          |
|-----------|---------------|------|-----|---------|----------------|
| id        | int(11)       | NO   | PRI | NULL    | auto_increment |
| cid       | int(11)       | YES  |     | NULL    |                |
| type      | varchar(32)   | YES  |     | NULL    |                |
| amount    | decimal(10,0) | YES  |     | NULL    |                |
| status    | int(11)       | YES  |     | NULL    |                |
| tm_create | datetime      | YES  |     | NULL    |                |
| tm_paid   | datetime      | YES  |     | NULL    |                |
| running   | int(11)       | YES  |     | NULL    | 交易表的订单号   |


* 提交付款【C】： `GET /api/pay/alipay/start`

此页面向支付宝付款页面发送post，在前端界面。

查找商品表中的item ID, 并获取商品信息。

在此页面提交前，在订单列表充值表中添加“待付款”状态，并且加入数据库，设置订单流水编号(`running`)并传给支付宝。

参数：`itemid` 商品表中的ID <可选，和`name`,`amount`互斥>

传入该参数时，表示这是来自于商品列表的付款请求

注意：当存在itemid时，需要先从商品表中获取商品信息记录，同时在订单表中添加一条记录，并且设置为该条目的流水号

参数：`name`<可选>和`amount`<可选> 商品的名称和价格 <可选，和`itemid`互斥>

（注意）对于购买商品的情况，若当余额足够时，就不发生付款充值流程，直接扣款并设置购买订单为已付款。

（注意）对于特殊商品，包含副作用，见商品管理。(若addyear不为0，则购买后按年增加用户的付费期限)

*** 忘了说  这个副作用在商品里面处理   支付只处理支付的逻辑

（1）若两个参数同时出现，表示这是一个自定义名称和价格的订单，同上面的，在订单表中添加一条记录，并且设置为该条目的流水号

（2）若只有amount参数，则表示该交易是充值。不需要插入订单表，只需要插入交易表。

（3）name参数不可单独出现，直接返回400 Bad Request

若其他参数组合，，直接返回400 Bad Request

（关于订单表的说明）订单表状态设置为“待支付”

返回值：form值（已实现，不需改动）
    
* 付款信息回调【P】： `GET /api/pay/alipay/finish`

参数：支付宝文档钦定，代码中已实现，不需改动。

支付宝在支付以后调用的回调界面，用于显示付款结果

在此页面被回调时，在订单充值表中修改订单状态为"等待付款机构处理结果"状态。

（关于订单表的说明）如果订单表存在对应记录，状态设置为“待确认”

* 付款信息通知回调【P】： `POST /api/pay/alipay/notify`

参数：支付宝文档钦定，代码中已实现并已验证，不需改动。

返回值：已实现。

支付宝在付款成功以后向客户端发送的通知，付款结果以此为准。在收到通知后，将状态设置为“充值成功”状态

在收到该通知时，如果存在订单，那么需要更改订单状态为已付款。

* 付款信息查询【C+】： `GET /api/pay/alipay/query`

参数：订单流水号 `running`

为没有及时更新的内容提供查询接口，若查询结果显示付款成功或失败，则更新为对应的状态。

### 10.2 微信

（注意）说明同支付宝，不再赘述。

* 提交付款【C】： `GET /api/pay/wepay/start`

* 付款信息回调【P】： `GET /api/pay/wepay/finish`

* 付款信息通知回调【P】： `POST /api/pay/wepay/notify`

* 付款信息查询【C+】： `GET /api/pay/wepay/query`

### 10.3 查询

* 查询最后一个订单状态：`GET /api/pay/last-order`

## 11 商品接口

商品表

| Field       | Type        | Null | Key | Default | Extra          |
|-------------|-------------|------|-----|---------|----------------|
| id          | int(11)     | NO   | PRI | NULL    | auto_increment |
| name        | varchar(64) | YES  |     | NULL    |                |
| price       | double      | YES  |     | NULL    |                |
| description | text        | YES  |     | NULL    |                |
| image       | text        | YES  |     | NULL    |                |
| removable   | int(11)     | NO   |     | 1       |                |
| addyear     | int         | NO   |     | 0       |                |

（说明） 默认存在商品若removable属性为0 则该商品无法删除

（说明） 若addyear不为0，则购买后按年增加用户的付费期限
    
* 【L】【C+】信息的商品列表 `GET /api/items/list`

返回数据表中所有信息

* 商品的添加【S】 `POST /api/items/add`

* 商品的修改【S】 `POST /api/items/{itemid}/modify`

* 商品的删除【S】 `POST /api/items/{itemid}/delete`

* 商品信息的获取【P】 `GET /api/items/{itemid}`

# 计划任务

系统定期执行的任务，包括

* 修改所有客户所有公司的年收入预选状态为“待选择0”

触发时间：每年1月1日0点后

说明: 在年收入预选的操作人设置为“系统”

* 更改服务包过期用户的状态以及服务包过期时间

触发时间：每天0点后