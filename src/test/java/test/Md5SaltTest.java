package test;

import org.ruoxue.backend.util.Md5SaltTool;
import org.ruoxue.backend.util.XunBinKit;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Md5SaltTest {

    private static Map users = new HashMap();

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String sixNum = XunBinKit.generateSixNum();
        System.out.println("------------------six: " + sixNum);
        sixNum = Md5SaltTool.getEncryptedPwd(sixNum);
        System.out.println("------------------six: " + sixNum);

        String userName = "fengjb";
        String password = "123456";
        registerUser(userName,password);

        String loginUserId = "fengjb";
        String pwd = "123456";
        try {
            if(loginValid(loginUserId,pwd)){
                System.out.println("欢迎登陆！！！");
            }else{
                System.out.println("口令错误，请重新输入！！！");
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 注册用户
     *
     * @param userName
     * @param password
     */
    public static String registerUser(String userName,String password){
        String encryptedPwd = null;
        try {
            encryptedPwd = Md5SaltTool.getEncryptedPwd(password);
            System.out.println("\n" + "------------password: " + password);
            System.out.println("------------encryptedPwd: " + encryptedPwd);
            users.put(userName, encryptedPwd);

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encryptedPwd;
    }

    /**
     * 验证登陆
     *
     * @param userName
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static boolean loginValid(String userName,String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException{
             /*String loginUserId = "zyg";
               String pwd = "1232";*/
        String pwdInDb = (String)users.get(userName);
        if(null!=pwdInDb){ // 该用户存在
            return Md5SaltTool.validPassword(password, pwdInDb);
        }else{
            System.out.println("不存在该用户！！！");
            return false;
        }
    }


}
