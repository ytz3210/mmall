package cn.yang.tmall.controller.backend;

import cn.yang.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yangtz
 * @ClassName: UserManageController
 * @Description:
 * @create 2020-02-10 09:19
 */
@RestController
@RequestMapping("/manager/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

//    @PostMapping("/login")
//    public RestTO login(@RequestBody User user, HttpServletResponse httpResponse){
//        RestTO response = iUserService.login(user.getUsername(), user.getPassword(),httpResponse);
//        if(response.isSuccess()){
//            User user = response.getData();
//            if(user.getRole() == Const.ROLE_ADMIN){
//                httpSession.setAttribute(Const.CURRENT_USER,user);
//                return response;
//            }else {
//                return RestTO.error("不是管理员，无法登录");
//            }
//        }
//        return response;
//    }
}
