package EVC.Movie.controller.user;


import EVC.Movie.entity.*;
import EVC.Movie.services.*;
import EVC.Movie.validator.OnUpdate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private LoveListServices loveListServices;
    @Autowired
    private HistoryServices historyServices;
    @Autowired
    private RecommentServices recommentServices;
    @GetMapping("/user/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/user/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "/user/register";
    }

    @PostMapping("/user/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/user/register";
        }
        userServices.save(user);
        return "redirect:/user/login";
    }
    @GetMapping("/taikhoan")
    public String taiKhoan(Model model,Locale locale){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);
        List<LoveList> loveLists = loveListServices.getAllByUser(currentUser);
        List<Recomment> deXuats = recommentServices.getAllWithUser(currentUser);
        List<String> imageUrls = recommentServices.findFirstMovieImageByRecommentId(deXuats);

        List<History> historyList = historyServices.getAllByUser(currentUser);

        List<History> histories = new ArrayList<>(historyList);
        Collections.reverse(histories);
        model.addAttribute("histories", histories);
        model.addAttribute("dexuats",deXuats);
        model.addAttribute("listImageUrl",imageUrls);
        model.addAttribute("user",currentUser);
        model.addAttribute("lovelist",loveLists);
        model.addAttribute("dexuat", new Recomment());
        model.addAttribute("Premiumdate", currentUser.getPremiumDuration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("locale",locale);
        return "user/userInfo/account";
    }
    @GetMapping("/taikhoan/edit")
    public String showEditForm( Model model,Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServices.findByUsername(authentication.getName());
        if (currentUser != null  ) {
            model.addAttribute("user", currentUser);
            model.addAttribute("locale",locale);
            return "user/userInfo/edit";
        }
        return "redirect:/user/login";
    }
    @PostMapping("/taikhoan/edit")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult result,
                         @RequestParam("imageUrl") MultipartFile image,Model model,Locale locale) {
        if (result.hasFieldErrors("name")||result.hasFieldErrors("phone")
            ||result.hasFieldErrors("tuoi")) {
            model.addAttribute("locale",locale);
            return "user/userInfo/edit";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServices.findByUsername(authentication.getName());

        user.setId(currentUser.getId());

        user.setOauth(currentUser.isOauth());
        user.setPremiumDuration(currentUser.getPremiumDuration());
        if(user.getEmail()!= null){
            User checkingUser = userServices.findFirstbyEmail(user.getEmail());
            if (checkingUser != null){
                if(!Objects.equals(user.getUserName(), checkingUser.getUserName())){
                    result.rejectValue("email", "error.user", "Vui lòng nhập khác !");
                    model.addAttribute("locale",locale);
                    return "user/userInfo/edit";
                }
            }

        }else{
            user.setEmail(currentUser.getEmail());
        }
        if (image != null && !image.isEmpty() && image.getOriginalFilename() != null && !image.getOriginalFilename().isBlank()){
            if(currentUser.getImageUrl() != null ){
                deleteImage(currentUser.getImageUrl());
            }
            user.setImageUrl(saveImage(image));
        }else {
            user.setImageUrl(currentUser.getImageUrl());
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword());
        }
        else{
            if (!userServices.checkOldPassword(currentUser, user.getOldPassword())) {
                result.rejectValue("oldPassword", "error.user", "Mật khẩu cũ không đúng");
                model.addAttribute("locale",locale);
                return "user/userInfo/edit";
            }
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "error.user", "Mật khẩu mới và xác nhận mật khẩu không khớp");
                model.addAttribute("locale",locale);
                return "user/userInfo/edit";
            }
        }

        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
        if (isAdmin) {
            userServices.saveAdmin(user);
        } else {

            userServices.save(user);
        }
        return "redirect:/taikhoan";
    }
    private String saveImage(MultipartFile imageUrl) {
        try {
            String originalFileName = imageUrl.getOriginalFilename();
            String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;

            File file = new File("uploads/images");
            if (!file.exists()) {
                file.mkdirs();
            }
            Path endPath = Paths.get(file.getAbsolutePath() + "/" + newFileName);
            Files.copy(imageUrl.getInputStream(), endPath, StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + newFileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            if (imageUrl.startsWith("/images/")) {
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
    @GetMapping("/taikhoan/lichsu")
    public String lichSu(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);

        List<History> historyList = historyServices.getAllByUser(currentUser);

        List<History> histories = new ArrayList<>(historyList);
        Collections.reverse(histories);
        model.addAttribute("user",currentUser);
        model.addAttribute("histories", histories);

        return "user/userInfo/histories";
    }


    @PostMapping("/taikhoan/danhsachphat/add")
    public String addDanhSachPhat(@Valid @ModelAttribute("dexuat") Recomment recomment, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);
        recomment.setUser(currentUser);
        recommentServices.save(recomment);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @PostMapping("/taikhoan/danhsachphat/xoa/{id}")
    public String xoaDanhSach(Model model, @PathVariable Long id,HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        recommentServices.delete(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @GetMapping("/taikhoan/danhsachphat/phim/{danhsachId}")
    public String danhSachPhatVaPhim(Model model, @PathVariable Long danhsachId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        String username = authentication.getName();
        User currentUser = userServices.findByUsername(username);
        Recomment deXuat = recommentServices.getById(danhsachId);
        List<Movie> movies = recommentServices.getMoviesInDeXuat(danhsachId);

        model.addAttribute("movies", movies);
        model.addAttribute("dexuat", deXuat);
        model.addAttribute("user",currentUser);

        return "user/userInfo/recomments";
    }
    @PostMapping("/themdanhsach/{movieId}/{danhsachId}")
    public String themDanhSach(Model model, @PathVariable Long movieId, @PathVariable Long danhsachId,HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        recommentServices.addMovieToDeXuat(danhsachId, movieId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @PostMapping("/taikhoan/yeuthich/xoa/{listId}")
    public String xoaYeuThich(@PathVariable Long listId,HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        User currentUser = userServices.findByUsername(authentication.getName());
        LoveList loveList = loveListServices.getById(listId);
        if(loveList.getUser() != currentUser){
            return "error/403";
        }else {
            loveListServices.delete(listId);
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @PostMapping("/taikhoan/lichsu/xoa/{listId}")
    public String xoaLichSu(@PathVariable Long listId,HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "/user/login";
        }
        User currentUser = userServices.findByUsername(authentication.getName());
        History history = historyServices.getById(listId);
        if(history.getUser() != currentUser){
            return "error/403";
        }else {
            historyServices.delete(listId);
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @PostMapping("/taikhoan/danhsachphat/xoaphim/{danhsachId}/{movieId}")
    public String danhSachPhatXoaPhim(Model model, @PathVariable Long danhsachId, @PathVariable Long movieId,HttpServletRequest request){
        recommentServices.deleteMovieToDeXuat(danhsachId, movieId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
