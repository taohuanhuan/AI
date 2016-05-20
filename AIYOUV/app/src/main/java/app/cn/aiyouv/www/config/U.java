package app.cn.aiyouv.www.config;

/**
 * Created by Administrator on 2016/1/14.
 */
public class U {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String WARN = "warn";
    //{"type":"eroor","content":"请先登陆！"}
    public static final String UNLOGIN = "error";
//    public static final String UNLOGIN_TIP = "设备已从其他地方登录";


    public static final String M = "member";
    public static final String S ="merchant";

    /**
     * 地址
     */
    public  static final String URL="http://61.172.235.162:8080/obj";
    /**
     * 高通下载地址
     * @param version
     *
     */
    public static final String VUFORIA_DOWNLOAD = "/rest/app/ar/maxVersion";
    /**
     * 高通识别成功按钮
     *  @param flag
     */
    public static final String VUFORIA_OK= "/rest/app/ar/arInfo";
    /**
     *获得广告
     * @param flag
     *
     */
    public static final String ADVERT="/rest/app/ad/list";
    /**
     * 详情
     * @param id
     *
     */
    public static final String ARTICLE_DETAIL="/rest/app/article/info";
    /**
     * 注册
     * @param phone,password,code,pristineName
     */
    public static final String IUV_REGISTER = "/rest/app/register/submit";
    /**
     * 发送验证码
     * @param phone
     */
    public static final String SEND_VAL = "/rest/app/register/sendSms";
    /**
     * 登录
     * @param phone,password
     */
    public static final String IUV_LOGIN = "/rest/app/login/submit";
    /**
     * 列表
     * @param pageNumber，pageSize
     *                  createDate    hits   modifyDate
     */
    public static final String IUV_LIST = "/rest/app/article/list/";
    /**
     * 分类
     */
    public static final String IUV_CATE = "/rest/app/articleCategory/list";
    //-------------
    /**
     * 登录信息@param userId,appKey
     */
    public static final String IUV_LOGIN_STATUS = "/rest/app/member/info";
    /**
     * 发布@param userId,appKey,title,longitude,latitude,content
     */
    public  static final String PUBLIC_SEND = "/rest/app/member/saveArticle";
    /**
     * 点赞@param appKey,userId,artclieId
     */
    public static final String IUV_DIANZAN="/rest/app/member/dianzan";
    /**
     * 我的点赞列表@param appKey,pageNumber，pageSize,
     */
    public static final String IUV_MY_DIANZAN = "/rest/app/member/myDianzan";
    /**
     * 我的关注列表@param userId appKey ,pageNumber，pageSize,
     */
    public static final String IUV_MY_GUANZHU = "/rest/app/member/myAttention";
    /**
     * 关注@param appKey,userId,artclieId
     */
    public static final String IUV_GUANZHU="/rest/app/member/attention";
    /**
     * 信息修改file=，password=，cender=(说明：male：男，female：女),address
     */
    public static final String IUV_EDIT = "/rest/app/member/save";
    /**
     * 查看粉丝  userId appKey,,pageNumber，pageSize,
     */
    public static final String IUV_FS_LIST = "/rest/app/member/myFans";

    /**
     * 查找粉丝@param appKey,userId,phone
     */
    public static final String IUV_FIND_FS = "/rest/app/member/lookFans";
    /**
     * 添加粉丝@param appKey,userId,id
     */
    public static final String IUV_ADD_FS = "/rest/app/member/addFans";
    /**
     * 查看聊天记录@param appKey,userId,id,pageNumber，pageSize
     */
    public static final String IUV_TALK_HIS = "/rest/app/member/message/lookMsgByMember";
    /**
     * 回复@param appKey,userId,id,content
     */
    public static final String IUV_RECY_="/rest/app/member/message/reply";
    /**
     * 我的发布列表 pageNumber，pageSize,appKey,userId
     */

    public static final String IUV_ORIGINAL="/rest/app/member/myOriginal";

    /**
     * 我的券包 pageNumber，pageSize,appKey,userId
     *    status=no    可以使用
          status=yes   已使用
          status=share 可分享
     *
     */
    public static final String IUV_TICKETS = "/rest/app/member/myPackage";


    /**
     * 记事本 pageNumber，pageSize,appKey,userId
     */
    public static final String IUV_EDITS = "/rest/app/member/myTicketBook";

    /**
     *      分享用户列表
     *   pageNumber，pageSize,appKey,userId,phone
             phone = 为空   当前用户的好友
            phone =模糊查询   系统可匹配的会员
     */
    public static final String IUV_FX_LIST = "/rest/app/member/lookMember";
    /**
     *   分享站内用户
     ticketId=  分享券id
     memberId=将要会享的会员id
     */
    public static final String IUV_FX_SEND = "/rest/app/member/shareInvestMember";
    /**
     * 优惠券详情 appKey,userId,id
     */
    public static final String IUV_Q_DETAIL = "/rest/app/member/ticketInfo";
    /**
     * 商家，我的账单 appKey,userId
     */
    public static final String MY_BILL = "/rest/app/merchant/myBills";
    /**
     * 从账单进来的，统计处理（我的账单那五个按钮）
     * @param pageNumber,pageSize,startDate,endDate,appKey,userId
     *
     */
    public static final String MY_BILL_LIST = "/rest/app/merchant/list/";
    /**
     * 商户中心，打赏列表
     * appKey,userId,pageNumber,pageSize
     */
    public static final String MY_REWARD_LIST = "/rest/app/member/awardList/";
    /**
     * 文章详情部分，打赏(商家打赏文章)
     * articleId,appKey,userId,money
     */
    public static final String Send_Reward_ = "/rest/app/merchant/awardMoney";
    /**
     * 文章详情部分，打赏（VIP打赏文章）
     * ticketId,articleId,appKey,userId,
      */
    public static final String Send_Reward_User="/rest/app/member/awardTicket";
    /**
     * 删除记事本
     * id ,appKey,userId,
     */
    public static final String DELETE_EDIT = "/rest/app/member/deletTicketBook";
    /**
     * 隐藏券
     *id ,userId,,appKey,ticketId,  visible(固定值hide)  如果是显示那么就是取值show
     */
    public static final String VG_TICKET="/rest/app/member/optVisible";
    /**
     * 图表
     * /rest/app/merchant/chart/localMember
     *userId,,appKey date
     */
    public static final String Xcharts = "/rest/app/merchant/chart/";
    /**
     * 搜索
     *  key
     */
    public static final String SEARCH="/rest/app/article/search";
    /**
     * 评论列表 articleId
     */
    public static final String RELIY_LIST= "/rest/app/article/reviewList";
    /**
     * 回复评论  parentId,content,articleId
     */
    public static final String REPLY_="/rest/app/member/addReview";
    /**
     * 升级VIP
     */
    public static final String VIP = "/rest/app/member/order/upVip";
    /**
     * 消息中心
     */
    public static final String MES = "/rest/app/member/messageList";
    /**
     * 修改
     */
    public static final String  MOD = "/rest/app/register/lookPassword";
    /**
     * 重设
     */
    public static final String RESET = "/rest/app/register/validationPassword";
}
