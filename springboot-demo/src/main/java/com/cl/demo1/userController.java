package com.cl.demo1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: chenl
 * @since: 2025/9/3 17:32
 * @description: 用户信息控制器
 */
@RestController
@RequestMapping("/api/users")
public class userController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取用户信息（从数据库获取）
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/info/{userId}")
    public Map<String, Object> getUserInfo(@PathVariable Long userId) {
        try {
            // 从MySQL数据库查询用户信息
            Map<String, Object> userInfo = jdbcTemplate.queryForObject(
                    "SELECT id, username, email, created_time, updated_time FROM user WHERE id = ?",
                    new Object[]{userId},
                    (rs, rowNum) -> {
                        Map<String, Object> user = new HashMap<>();
                        user.put("id", rs.getLong("id"));
                        user.put("username", rs.getString("username"));
                        user.put("email", rs.getString("email"));
                        user.put("created_time", rs.getString("created_time"));
                        user.put("updated_time", rs.getString("updated_time"));
                        return user;
                    }
            );

            // 构造成功响应
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", userInfo);
            return result;

        } catch (Exception e) {
            // 处理用户不存在或数据库异常
            Map<String, Object> result = new HashMap<>();
            if (e.getMessage().contains("Incorrect result size")) {
                // 用户不存在
                result.put("code", 404);
                result.put("message", "用户不存在");
            } else {
                // 其他数据库错误
                result.put("code", 500);
                result.put("message", "数据库查询失败: " + e.getMessage());
            }
            return result;
        }
    }

    /**
     * 获取所有用户信息
     * @return 用户列表
     */
    @GetMapping("/list")
    public Map<String, Object> getAllUsers() {
        try {
            // 查询所有用户
            var users = jdbcTemplate.query(
                    "SELECT id, username, email, created_time FROM user",
                    (rs, rowNum) -> {
                        Map<String, Object> user = new HashMap<>();
                        user.put("id", rs.getLong("id"));
                        user.put("username", rs.getString("username"));
                        user.put("email", rs.getString("email"));
                        user.put("created_time", rs.getString("created_time"));
                        return user;
                    }
            );

            // 构造成功响应
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", users);
            return result;

        } catch (Exception e) {
            // 处理数据库异常
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "数据库查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/info/username/{username}")
    public Map<String, Object> getUserInfoByUsername(@PathVariable String username) {
        try {
            // 根据用户名查询用户信息
            Map<String, Object> userInfo = jdbcTemplate.queryForObject(
                    "SELECT id, username, email, created_time, updated_time FROM user WHERE username = ?",
                    new Object[]{username},
                    (rs, rowNum) -> {
                        Map<String, Object> user = new HashMap<>();
                        user.put("id", rs.getLong("id"));
                        user.put("username", rs.getString("username"));
                        user.put("email", rs.getString("email"));
                        user.put("created_time", rs.getString("created_time"));
                        user.put("updated_time", rs.getString("updated_time"));
                        return user;
                    }
            );

            // 构造成功响应
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", userInfo);
            return result;

        } catch (Exception e) {
            // 处理用户不存在或数据库异常
            Map<String, Object> result = new HashMap<>();
            if (e.getMessage().contains("Incorrect result size")) {
                // 用户不存在
                result.put("code", 404);
                result.put("message", "用户不存在");
            } else {
                // 其他数据库错误
                result.put("code", 500);
                result.put("message", "数据库查询失败: " + e.getMessage());
            }
            return result;
        }
    }
}

