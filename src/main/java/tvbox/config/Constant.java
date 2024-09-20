package tvbox.config;

import java.net.PortUnreachableException;
import java.net.URI;

public class Constant {
    public static final String CONSTANT = "constant";

    //每个M3U文件第一行必须是这个tag，请标示作用
    public static final String EXT_M3U="#EXTM3U";

    //该属性可以没有
    public static final String EXT_X_VERSION="#EXT-X-VERSION";

    //每一个media URI在PlayList中只有唯一的序号，相邻之间序号+1,
    //一个media URI并不是必须要包含的，如果没有，默认为0
    public static final String EXT_X_MEDIA_SEQUENCE="#EXT-X-MEDIA-SEQUENCE";

    //指定最大的媒体段时间长（秒）。所以#EXTINF中指定的时间长度必须小于或是等于这
    //个最大值。这个tag在整个PlayList文件中只能出现一 次（在嵌套的情况下，一般有
    //真正ts url的m3u8才会出现该tag）
    public static final String EXT_X_TARGET_DURATION="#EXT-X-TARGETDURATION";

    //提供关于PlayList的可变性的信息，这个对整个PlayList文件有效，是可选的，格式
    //如下：#EXT-X-PLAYLIST-TYPE:：如果是VOD，则服务器不能改变PlayList 文件；
    //如果是EVENT，则服务器不能改变或是删除PlayList文件中的任何部分，但是可以向该
    //文件中增加新的一行内容。
    public static final String EXT_X_PLAYLIST_TYPE="#EXT-X-PLAYLIST-TYPE";

    //duration指定每个媒体段(ts)的持续时间（秒），仅对其后面的URI有效，title是下载资源的url
    public static final String EXTINF = "#EXTINF";

    //表示怎么对media segments进行解码。其作用范围是下次该tag出现前的所有media
    //URI，属性为NONE 或者 AES-128。NONE表示 URI以及IV（Initialization
    //Vector）属性必须不存在， AES-128(Advanced EncryptionStandard)表示URI
    //必须存在，IV可以不存在。
    public static final String EXT_X_KEY="#EXT-X-KEY";

    //将一个绝对时间或是日期和一个媒体段中的第一个sample相关联，只对下一个meida
    //URI有效，格式如#EXT-X-PROGRAM-DATE-TIME:
    //For example: #EXT-X-PROGRAM-DATETIME:2010-02-19T14:54:23.031+08:00
    public static final String EXT_X_PROGRAM_DATE_TIME="#EXT-X-PROGRAM-DATE-TIME";

    //是否允许做cache，这个可以在PlayList文件中任意地方出现，并且最多出现一次，作
    //用效果是所有的媒体段。格式如下：#EXT-X-ALLOW-CACHE:
    public static final String EXT_X_ALLOW_CACHE="#EXT-X-ALLOW-CACHE";

    //表示PlayList的末尾了，它可以在PlayList中任意位置出现，但是只能出现一个，格
    //式如下：#EXT-X-ENDLIST
    public static final String EXT_X_ENDLIST="#EXT-X-ENDLIST";
}
