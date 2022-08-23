package com.tuling.tulingmall.promotion.service.nginx;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
public class SftpUploadService {

    @Value("${secKillServerList}")
    private List<String> secKillServerList;

    /**sftp服务器ip地址*/
    @Value("${ftp.host}")
    private String host;

    /**端口*/
    @Value("${seckill.sftp.port}")
    private static int port;

    /**用户名*/
    @Value("${seckill.sftp.userName}")
    private static String userName;

    /**密码*/
    @Value("${seckill.sftp.password}")
    private static String password;

    /**存放图片的根目录*/
    @Value("${seckill.sftp.rootPath}")
    private static String rootPath;

    /**存放图片的路径*/
    @Value("${seckill.sftp.img.url}")
    private static String imgUrl;


    /** 获取连接 */
    private ChannelSftp getChannel() throws Exception{
        JSch jsch = new JSch();
        //->ssh root@host:port
        Session sshSession = jsch.getSession(userName,host,port);
        //密码
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }
    /**
     * ftp上传图片
     * @param inputStream 图片io流
     * @param imagePath 路径，不存在就创建目录
     * @param imagesName 图片名称
     * @return urlStr 图片的存放路径
     */
    public String putImages(InputStream inputStream, String imagePath, String imagesName){
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + imagePath + "/";
            createDir(path,sftp);
            //上传文件
            sftp.put(inputStream, path + imagesName);
            log.info("上传成功！");
            sftp.quit();
            sftp.exit();
            //处理返回的路径
            String resultFile;
            resultFile = imgUrl + imagePath + imagesName;
            return resultFile;
        } catch (Exception e) {
            log.error("上传失败：" + e.getMessage());
        }
        return "";
    }
    /**
     * 创建目录
     */
    private static void createDir(String path,ChannelSftp sftp) throws SftpException {
        String[] folders = path.split("/");
        sftp.cd("/");
        for ( String folder : folders ) {
            if ( folder.length() > 0 ) {
                try {
                    sftp.cd( folder );
                }catch ( SftpException e ) {
                    sftp.mkdir( folder );
                    sftp.cd( folder );
                }
            }
        }
    }

}
