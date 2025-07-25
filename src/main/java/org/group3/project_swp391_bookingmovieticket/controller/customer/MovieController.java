package org.group3.project_swp391_bookingmovieticket.controller.customer;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Comment;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Review;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.ICommentReactionService;
import org.group3.project_swp391_bookingmovieticket.service.ICommentService;
import org.group3.project_swp391_bookingmovieticket.service.impl.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.*;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieActorService movieActorService;

    @Autowired
    private DirectorService directorService;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ICommentReactionService reactionService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public String displayMovieAll() {
        return "home";
    }

    @GetMapping("/search")
    public String displayAllMovies(@RequestParam(value = "movieNameSearch", required = false) String movieNameSearch,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "6") int size,
                                   Model model) {
        if (movieNameSearch == null || movieNameSearch.trim().isEmpty() || movieNameSearch.trim().equalsIgnoreCase(" ")) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        } else {
            Pageable pageable = PageRequest.of(page, size);
            model.addAttribute("movieSearch", movieService.findByMovieName(movieNameSearch, pageable));
            model.addAttribute("movieNameSearch", movieNameSearch);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());

        }
        return "movie_search";
    }

    @GetMapping(value = "/detail")
    public String displayMovieDetail(@RequestParam(value = "movieId", required = false) Integer movieId,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "6") int size,
                                     HttpSession session,
                                     Model model) {
        if (movieId == null) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> commentPageResult = commentService.getCommentsByMovieId(movieId, pageable);
        Page<Review> reviewPageResult = reviewService.getReviewsForMovie(movieId, pageable);

        List<Comment> comments = commentPageResult.getContent();
        List<Review> reviews = reviewPageResult.getContent();

        // Like/Dislike
        Map<Integer, Long> likesMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> reactionService.countLikes(c)));

        Map<Integer, Long> dislikesMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> reactionService.countDislikes(c)));
        model.addAttribute("comments", comments);
        model.addAttribute("reviews", reviews);
        model.addAttribute("commentPage", commentPageResult);
        model.addAttribute("reviewPage", reviewPageResult);
        model.addAttribute("likesMap", likesMap);
        model.addAttribute("dislikesMap", dislikesMap);
        User user = (User) session.getAttribute("userLogin");
        boolean hasOrdered = false;
        boolean isFavorite = false;

        if (user != null) {
            hasOrdered = paymentLinkService.existsByUser_IdAndSchedule_Movie_Name(user.getId(), movieService.findMovieById(movieId).getName());
            isFavorite = favoriteService.isFavorite(user, modelMapper.map(movieService.findMovieById(movieId), Movie.class));
        }
        model.addAttribute("hasOrdered", hasOrdered);
        model.addAttribute("isFavorite", isFavorite);



        MovieDTO movieDetail = movieService.findMovieById(movieId);
        model.addAttribute("movieSameCategory", movieService.findByCategory(movieDetail.getCategories(), pageable));
        model.addAttribute("actorByMovie", movieActorService.findAllActorByMovieId(movieId));
        model.addAttribute("directorByMovie", directorService.findDirectorByMovieId(movieId));
        model.addAttribute("movieDetail", movieDetail);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());

        return "movie_detail";
    }

    @GetMapping(value = "/rating")
    public String rating(@RequestParam(value = "ratingId", required = false) Integer ratingId,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "6") int size,
                         Model model) {
        if (ratingId == null) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        }
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("movieRates", movieService.findMovieByRatingId(ratingId, pageable));
        model.addAttribute("ratingId", ratingId);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "movie_rating";
    }

    @GetMapping(value = "/category/return-view")
    public String getMoviesByCategoryReturnView(@RequestParam(value = "categoryName", required = false) String categoryName,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "6") int size,
                                                Model model) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("movieByCategory", movieService.getMovieByCategory(categoryName.trim(), pageable));
        model.addAttribute("categoryName", categoryName);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());

        return "movie_category";
    }

    @GetMapping(value = "/showing")
    public String getMoviesShowing(@RequestParam(value = "status", required = false) String status,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "6") int size,
                                   Model model) {
        if (status == null || status.trim().isEmpty()) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        } else {
            Pageable pageable = PageRequest.of(page, size);
            if (status.equalsIgnoreCase("now-showing")) {
                model.addAttribute("movieShowing", movieService.findMovieNowShowing(pageable));
                model.addAttribute("status", "Đang chiếu");
            } else if (status.equalsIgnoreCase("coming-soon")) {
                model.addAttribute("movieShowing", movieService.findMovieComingSoon(pageable));
                model.addAttribute("status", "Sắp chiếu");
            }
        }
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "movie_showing";
    }
}
