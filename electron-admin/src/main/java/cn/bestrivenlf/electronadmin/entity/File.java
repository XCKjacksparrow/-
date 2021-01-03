/**
 * FileName: File
 * Author:   dinggc
 * Date:     2019/6/24 21:07
 * Description: 文件类
 * History:
 * <author>  <version>   <desc>
 * dinggc     1.0         描述
 */
package cn.bestrivenlf.electronadmin.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;

/**
 * 〈一句话功能简述〉<br>
 * 〈文件类〉
 *
 * @author dinggc
 * @create 2019/6/24
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class File extends Base{
    /*
    文件原始名称
     */
    private String initialName;
    /*
    新生成的uuid名称
     */
    private String uuidName;
    /*
    文件类型
     */
    private String type;
    /*
    文件地址
     */
    private String address;
    /*
    文件磁盘地址
     */
    private String diskAddress;
    /*
    文件流
     */
    private InputStream inputStream;

}