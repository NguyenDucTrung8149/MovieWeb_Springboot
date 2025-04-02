package EVC.Movie.controller.admin;

import EVC.Movie.entity.User;
import EVC.Movie.services.UserServices;
import EVC.Movie.validator.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/account")
public class AccountManageController {
    @Autowired
    public UserServices userServices;

    @GetMapping()
    public String index(Model model, @RequestParam(required = false) String tenTaiKhoan,
                                     @RequestParam(defaultValue = "0") int adminPage,
                                     @RequestParam(defaultValue = "0") int userPage,
                                     @RequestParam(defaultValue = "12") int size){
        if (adminPage < 0) {
            adminPage = 0;
        }
        if (userPage < 0) {
            userPage = 0;
        }
        Page<User> adminsPage = userServices.findByRoleNameAndUsername("ADMIN", tenTaiKhoan, PageRequest.of(adminPage, size));
        Page<User> guestsPage = userServices.findByRoleNameAndUsername("USER", tenTaiKhoan, PageRequest.of(userPage, size));
        long totalAdmins = userServices.countByRoleName("ADMIN");
        model.addAttribute("tenTaiKhoan", tenTaiKhoan);
        model.addAttribute("adminsPage", adminsPage);
        model.addAttribute("guestsPage", guestsPage);
        model.addAttribute("adminPage", adminPage);
        model.addAttribute("userPage", userPage);
        model.addAttribute("totalAdmins", totalAdmins);
        return "admin/account/list";
    }
    @PostMapping("/lock/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String lockUser(@PathVariable Long id) {
        userServices.lockUser(id);
        return "redirect:/admin/account";
    }

    @PostMapping("/unlock/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String unlockUser(@PathVariable Long id) {
        userServices.unlockUser(id);
        return "redirect:/admin/account";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/account/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String add(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/account/add";
        }
        userServices.saveAdmin(user);
        return "redirect:/admin/account";
    }


    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userServices.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin/account/edit";
        }
        return "redirect:/admin/account";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasFieldErrors("name")||result.hasFieldErrors("phone")
                ||result.hasFieldErrors("tuoi")) {
            return "admin/account/edit";
        }
        User currentUser = userServices.findById(id);
        user.setId(id);
        user.setOauth(currentUser.isOauth());
        if(user.getEmail()!= null){
            User checkingUser = userServices.findFirstbyEmail(user.getEmail());
            if (checkingUser != null){
                if(!Objects.equals(user.getUserName(), checkingUser.getUserName())){
                    result.rejectValue("email", "error.user", "Vui lòng nhập khác !");
                    return "admin/account/edit";
                }
            }

        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword());
        }
        else{
            if (!userServices.checkOldPassword(currentUser, user.getOldPassword())) {
                result.rejectValue("oldPassword", "error.user", "Mật khẩu cũ không đúng");
                return "admin/account/edit";
            }
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "error.user", "Mật khẩu mới và xác nhận mật khẩu không khớp");
                return "admin/account/edit";
            }
        }
        userServices.saveAdmin(user);
        return "redirect:/admin/account";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteAdmin(@PathVariable Long id) {
        List<User> userAdList = userServices.findUserByRoleName("ADMIN");
        User user = userServices.findByIdWithRoleUser(id);
        if(userAdList.size()>1 && user == null) {
            User existUser = userServices.findById(id);
            if (existUser.getImageUrl() != null) {
                deleteImage(existUser.getImageUrl());
            }
            userServices.deleteById(id);
        }
        return "redirect:/admin/account";
    }
    private void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                // Đường dẫn tới file trong thư mục uploads
                Path imagePath = Paths.get("uploads" + imageUrl);
                // Kiểm tra nếu file tồn tại
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath); // Xóa file
                }
            } catch (IOException e) {
                System.out.println("Could not delete image: " + e.getMessage());
            }
        }
    }
}
